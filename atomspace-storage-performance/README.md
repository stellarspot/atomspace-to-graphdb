# AtomSpace backing storage performance


## Data

### Ontology
* [Animal Natural History and Life History Ontology](http://aber-owl.net/ontology/ADW/)
* [Ontology Java parser OWLAPI](https://github.com/owlcs/owlapi/)
* [Introduction OWLAPI](http://syllabus.cs.manchester.ac.uk/pgt/2017/COMP62342/introduction-owl-api-msc.pdf)


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