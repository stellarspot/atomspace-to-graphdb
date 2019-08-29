# AtomSpace backing storage performance

## Neo4j

The following 3 models are used to represent a triple (subject, predicate, object) in Neo4j graph:
* Native Model
* Predicate Model
* Evaluation Model

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
MERGE (:Atom:Link { id: {id}, type: {type}})
MATCH (a1:Atom {id: {id1}}), (a2:Atom {id: {id2}})
MERGE (a1)-[r:ARG {position: {position}}] ->(a2)
```

![](docs/images/predicate_graph.png)

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

![](docs/images/evaluation_graph.png)


### Triple graph creation and query performance 

Triple graph with parameter N consists of a list of triples (subject, predicate, object)
where number of  
subjects: N  
objects: N  
predicates: N / 2  
predicates per subject: N / 4


Number in columns is the N parameter in the triple graph.

Create time (ms)

|Model     |  20   |  40   | 60    | 80     |
|----------|-------|-------|-------|--------|
|Native    |69.25  |106.00 |216.25 |399.75  |
|Predicate |82.00  |549.00 |2291.25|6539.75 |
|Evaluation|159.75 |1614.50|7217.75|22171.00|

![Create requests](docs/images/time_create.png)

Query time (ms):

|Model     |  20  |  40  | 60   | 80    |
|----------|------|------|------|-------|
|Native    |13.75 |15.75 |18.50 |33.75  |
|Predicate |17.50 |47.75 |110.50|219.75 |
|Evaluation|47.25 |198.50|549.75|1266.50|

![Query requests](docs/images/time_query.png)

## Storages

### Neo4j

* [Java API](https://neo4j.com/docs/api/java-driver/current)

Run docker:
```bash
docker run \
    --publish=7474:7474 --publish=7687:7687 \
    --volume=$HOME/neo4j/data:/data \
    --rm -d neo4j
```

Open Neo4j in browser: [http://localhost:7474/browser](http://localhost:7474/browser)

Commands:
```cypher
// get all nodes
MATCH (n) RETURN n;

// delete all nodes
MATCH (n) DELETE n;

// count nodes
MATCH (n) RETURN count(n);
```

Store triples (subject, predicate, object):
```cypher
MERGE (:Person {name: {subject}})
MERGE (:Item   {name: {object}})
MATCH (a:Person {name: {subject}}), (b:Item {name: {object}})
    CREATE (a)-[r:PREDICATE {name: {predicate}}] ->(b)
```

Query triples (subject, predicate, object):
```cypher
MATCH (:Person {name: {subject}})-[:PREDICATE {name: {predicate}}]->(s:Item) RETURN s.name
MATCH (o:Person)-[p:PREDICATE]->(s:Item) WHERE o.name = 'subject-0' and p.name = 'predicate-0'  RETURN s

```

## Data

### Ontology
* [Animal Natural History and Life History Ontology](http://aber-owl.net/ontology/ADW/)
* [Ontology Java parser OWLAPI](https://github.com/owlcs/owlapi/)
* [Introduction OWLAPI](http://syllabus.cs.manchester.ac.uk/pgt/2017/COMP62342/introduction-owl-api-msc.pdf)
