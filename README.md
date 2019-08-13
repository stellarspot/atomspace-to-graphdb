# Storing AtomSpace in different GraphDB storages

Questions for GraphDB:
1. Store unique nodes
1. Simple query: What does Alice like?
1. Store binary values

## Neo4j

Docs:
* [Cypher](https://neo4j.com/docs/cypher-manual/current/introduction/)
* [Sandbox](https://neo4j.com/sandbox-v2/)

### Store unique nodes

Neo4j allows storing different nodes with the same label:
```cypher
CREATE (:SampleNode)
CREATE (:SampleNode)
MATCH (n:SampleNode) RETURN count(n) as count;
```

Result: 2

### Simple query: What does Alice like?

```cypher
// Alice
CREATE (alice:Alice)-[:LIKES]->(:ice_cream)
CREATE (alice)-[:LIKES]->(:apple)
CREATE (alice)-[:DISLIKES]->(:pear)
// Bob
CREATE (bob:Bob)-[:LIKES]->(:apple)

MATCH (:Alice)-[:LIKES]->(what) RETURN what;
```

## Dgraph

Docs:
* [Tutorial](https://tour.dgraph.io/)