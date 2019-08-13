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

### Store binary values

Neo4j does not support binary data as values.
It is recommended to story binary values in a separate storage.

[Neo4j Properties](https://neo4j.com/docs/java-reference/current/javadocs/org/neo4j/graphdb/PropertyContainer.html)
```cypher
Properties are key-value pairs. The keys are always strings. Valid property value types are
all the Java primitives (int, byte, float, etc), java.lang.Strings, the Spatial and Temporal types
and arrays of any of these.

 Please note that Neo4j does NOT accept arbitrary objects as property values.
 setProperty() takes a java.lang.Object only to avoid an explosion of overloaded setProperty() methods.
```

## Dgraph

Docs:
* [Tutorial](https://tour.dgraph.io/)