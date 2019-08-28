# AtomSpace backing storage performance


## Data

### Ontology
* [Animal Natural History and Life History Ontology](http://aber-owl.net/ontology/ADW/)
* [Ontology Java parser OWLAPI](https://github.com/owlcs/owlapi/)
* [Introduction OWLAPI](http://syllabus.cs.manchester.ac.uk/pgt/2017/COMP62342/introduction-owl-api-msc.pdf)

## Performance

### Neo4j

Triple graph with parameter N  
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

Query time (ms):

|Model     |  20  |  40  | 60   | 80    |
|----------|------|------|------|-------|
|Native    |13.75 |15.75 |18.50 |33.75  |
|Predicate |17.50 |47.75 |110.50|219.75 |
|Evaluation|47.25 |198.50|549.75|1266.50|


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