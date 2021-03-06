# AtomSpace backing storage performance

## Triple models and triple graph
The following 3 models are used to represent a triple (subject, predicate, object) in graph:
* Native Model
* Predicate Model
* Evaluation Model

Native model connects two nodes subject and object by predicate link: 
```cypher
(subject)-[predicate]->(object)
```
Predicate model stores subject and object nodes in a hyper-edge represented by predicate link:
```text
Predicate(Subject, Object)
```
Evaluation model stores triple in Evaluation hyper-edge in a way:
```text
Evaluation
    Predicate "predicate"
    List
        Subject "subject"
        Object  "object"
```

Triple graph with number of nodes N and number of triples S consists of a list of triples (subject, predicate, object)
where number of
```text
subjects: N  
objects: N  
predicates: N / 4
number of triples: S
```

Example of a triple graph with N = 8 and S = 4:
```text
subjects: subject-0,...,subject-7  
objects: object-0,...,object-7  
predicates: predicate-0,predicate-2
```
triples:
```text
(subject-4, predicate-0, object-4)
(subject-0, predicate-1, object-7)
(subject-3, predicate-1, object-5)
(subject-6, predicate-0, object-7)
```
Triple graph with N = 8 and S = 12 in Native model:
![Triple graph](docs/images/triple_graph.png)


## Atomspace

### Predicate Model

Stores triple (subject, predicate, object) in a way that predicate becomes 'PredicateLink' link
with subject and object nodes.
For example triple (Alice, likes, ice-cream) is stored as:
```scheme
(LikesLink
  (Subject "Alice")
  (Object "ice-cream"))
```
query to object:
```scheme
(LikesLink
  (Subject "Alice")
  (Variable "$WHAT"))
```
### Evaluation Model

Stores triple (subject, predicate, object) in EvaluationLink like:
```scheme
(EvaluationLink
  (PredicateNode "likes")
    (ListLink
      (ConceptNode "Alice")
      (ConceptNode "ice-cream")))
```
query to object:
```scheme
(EvaluationLink
  (PredicateNode "likes")
    (ListLink
      (ConceptNode "Alice")
      (VariableNode "$WHAT")))
```
### Creation and query performance

Number in columns is the number of triples (parameter S in the triple graph).
Parameter N is 500 (number of subjects 500, objects 500, predicates 125).

Create time (ms)

|Model     |  100  |  200  | 300   | 400   |
|----------|-------|-------|-------|-------|
|Predicate |3.34   |7.87   |10.57  |14.36  |
|Evaluation|5.61   |10.19  |15.44  |18.73  |

![Atomspace time create](docs/images/perf/time_create_atomspace_small.png)

Number in columns is the number of queries.
Parameter N is 500 (number of subjects 500, objects 500, predicates 125).
Parameter S is 500 (number of triples).

Query time (ms):

|Model     |1000  |2000  |3000 |4000 |
|----------|------|------|-----|-----|
|Predicate |74    |162   |240  |325  |
|Evaluation|105   |200   |331  |399  |

![Atomspace time query](docs/images/perf/time_query_atomspace_small.png)

## Neo4j

### Native Model

Native model stores subject and object as nodes and predicate as link between them.  
For example triple (Alice, likes, ice-cream) is stored as:
```cypher
(:Subject {name: "Alice"}) - [:LIKES] -> (:Object {name: "ice-cream"})
```
![Native graph](docs/images/native_graph.png)

Query to the object: What does Alice like?
```cypher
MATCH (Subject {name: "Alice"}) - [:LIKES] -> (obj:Object)
RETURN obj.name
```

### Predicate Model

Predicate model represents triple (subject, predicate, object) as
a hyper-edge 
```text
PredicateLink
    SubjectNode "subject"
    ObjectNode "object"
```
and is represented in property graph as   
Node:
```cypher
MERGE (:Atom:Node { id: {id}, type: {type}, value: {value}})  
```

Link:
```cypher
MATCH (a1:Atom {id: {id1}}), (a2:Atom {id: {id2}})
MERGE (a1)-[r:ARG {position: {position}}] ->(a2)
```

![Predicate graph](docs/images/predicate_graph.png)

Query to the object: What does Alice like?
```cypher
MATCH
 (p:Atom:Link {type: "LIKES_LINK"})-[{position: 0}]-> (:Atom:Node {type: "Subject", value: "Alice"}),
 (p)-[{position: 1}]-> (o:Atom:Node {type: "Object"})
 RETURN o.value
```

### Evaluation Model

Evaluation model represents triple (subject, predicate, object) as
a hyper-edge 
```text
EvaluationLink
    PredicateNode "predicate"
    ListLink
        SubjectNode "subject"
        ObjectNode "object"
```

![Evaluation graph](docs/images/evaluation_graph.png)

The code to create en evaluation link in Neo4j is the same as in the Predicate model.

Query to the object: What does Alice like?
```cypher
MATCH
(e:Atom:Link {type: 'EvaluationLink'}) - [:ARG {position: 0}] -> (:Atom:Node {value: {predicate}}),
(e) - [:ARG {position: 1}] -> (l:Atom:Link {type: 'ListLink'}),
(l) - [:ARG {position: 0}] -> (:Atom:Node {type: 'ConceptNode', value: {subject}}),
(l) - [:ARG {position: 1}] -> (o:Atom:Node {type: 'ConceptNode'})
RETURN o.value
```

### Creation and query performance using Cypher

The following indices are used in Neo4j:
```cypher
CREATE INDEX ON :Atom(id)
CREATE INDEX ON :Atom(type)
CREATE INDEX ON :ARG(position)
CREATE INDEX ON :Subject(value)
CREATE INDEX ON :Object(value)
```

Number in columns is the number of triples (parameter S in the triple graph).
Parameter N is 500 (number of subjects 500, objects 500, predicates 125).

Create time (ms)

|Model     |  100  |  200  | 300   | 400   |
|----------|-------|-------|-------|-------|
|Native    |18     |31     |37     |42     |
|Predicate |24     |48     |66     |84     |
|Evaluation|42     |76     |112    |138    |

![Neo4j create requests](docs/images/perf/time_create_neo4j_cypher_small.png)


Number in columns is the number of queries.
Parameter N is 500 (number of subjects 500, objects 500, predicates 125).
Parameter S is 500 (number of triples).

Query time (ms):

|Model     |100   |200   |300  |400 |
|----------|------|------|-----|----|
|Native    |19    |35    |52   |73  |
|Predicate |25    |46    |65   |95  |
|Evaluation|645   |1286  |1937 |2543|


![Neo4j query requests](docs/images/perf/time_query_neo4j_cypher_small.png)

### Creation and query performance using Java API

Number in columns is the number of triples (parameter S in the triple graph).
Parameter N is 500 (number of subjects 500, objects 500, predicates 125).

Create time (ms)

|Model     |  100  |  200  | 300   | 400   |
|----------|-------|-------|-------|-------|
|Native    |12     |20     |37     |49     |
|Predicate |15     |32     |50     |60     |
|Evaluation|25     |53     |93     |128    |

![Neo4j create requests](docs/images/perf/time_create_neo4j_api_small.png)

Number in columns is the number of queries.
Parameter N is 500 (number of subjects 500, objects 500, predicates 125).
Parameter S is 500 (number of triples).

Query time (ms):

|Model     |1000  |2000  |3000 |4000 |
|----------|------|------|-----|-----|
|Native    |57    |130   |187  |256  |
|Predicate |64    |142   |213  |269  |
|Evaluation|69    |151   |219  |287  |

![Neo4j query requests](docs/images/perf/time_query_neo4j_api_small.png)


## JanusGraph

### Creation and query performance using Java API

Number in columns is the number of triples (parameter S in the triple graph).
Parameter N is 500 (number of subjects 500, objects 500, predicates 125).

Create time (ms)

|Model     |  100  |  200  | 300   | 400   |
|----------|-------|-------|-------|-------|
|Native    |139    |392    |715    |1076   |
|Predicate |160    |467    |824    |1242   |
|Evaluation|235    |665    |1187   |1782   |

![JanusGraph create requests](docs/images/perf/time_create_janusgraph_small.png)


Number in columns is the number of queries.
Parameter N is 500 (number of subjects 500, objects 500, predicates 125).
Parameter S is 500 (number of triples).

Query time (ms):

|Model     |1000  |2000  |3000 |4000 |
|----------|------|------|-----|-----|
|Native    |45    |73    |105  |133  |
|Predicate |69    |111   |156  |195  |
|Evaluation|199   |365   |513  |635  |

![JanusGraph query requests](docs/images/perf/time_query_janusgraph_small.png)

## Conclusion

![Compare create requests](docs/images/perf/time_create_compare.png)

All three storages AtomSpace, Neo4j with Java API, and JanusGraph show nearly linear
time dependency for atoms creation in Native, Predicate and Evaluation models.  

However, the creation time is different for each model.
AtomsSpace is the fastest one just because it stores all data in memory.

The difference between time creation for Neo4j and JanusGaph needs to be further investigated.
Both storages were deployed locally and they should be able to properly cache their data.

![Compare query requests](docs/images/perf/time_query_compare.png)


The query requests also are described nearly linear time dependency.

The Evaluation model for JanusGraph shows significant time increasing for larger number requests.
The similar results are shown by the Neo4j with Cypher query requests.
Reasonable explanation could be that query requests for Native and Predicate models require
only one path traversal whereas Evaluation model contains fork when it is necessary to
check both paths to predicate and object nodes.

It should be noted that the current work does not emulate incoming set creation and traversing
through it as it is used by pattern matcher in AtomSpace. Incoming set emulation
can introduce additional impact on both for data creation and query time performance.