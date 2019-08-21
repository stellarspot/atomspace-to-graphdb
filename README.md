# Storing AtomSpace in different GraphDB storages

Questions for GraphDB:
1. Store unique nodes
1. Simple query: What does Alice like?
1. Store Atomese atoms
1. Store binary values

## GraphDB comparison table

| GraphDB  | Data Model|
|----------|-----------|
| AtomSpace| Hypergraph|
| Neo4j    | Property  |
| Dgraph   | RDF       |


Atomspace is hypergraph GraphDB where edge (link in Atomspace) can join any number of edges (nodes).
RDF - Resource Description Framework

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
## Store Atomese atoms


Sample: Alice likes ice-cream:

Atomese:
```text
EvaluationLink
    PredicateNode "likes"
    ListLink
        ConceptNode "Alice"
        ConceptNode "ice-cream"
```

Property graph:
```cypher
CREATE (el:EvaluationLink)-[:LINK {index: "0"}]->(:PredicateNode {name: "likes"})
CREATE (el)-[:LINK {index: "1"}]->(ll:ListLink)
CREATE (ll)-[:LINK {index: "0"}]->(:ConceptNode {name: "Alice"})
CREATE (ll)-[:LINK {index: "1"}]->(:ConceptNode {name: "ice-cream"})
```

### Store binary values

Neo4j does not support binary data as values.
It is recommended to story binary values in a separate storage.

[Neo4j Properties](https://neo4j.com/docs/java-reference/current/javadocs/org/neo4j/graphdb/PropertyContainer.html)
```text
Properties are key-value pairs. The keys are always strings. Valid property value types are
all the Java primitives (int, byte, float, etc), java.lang.Strings, the Spatial and Temporal types
and arrays of any of these.

 Please note that Neo4j does NOT accept arbitrary objects as property values.
 setProperty() takes a java.lang.Object only to avoid an explosion of overloaded setProperty() methods.
```

[Worst Practices for BLOBs in Neo4j](https://neo4j.com/blog/dark-side-neo4j-worst-practices/)
```text
Using BLOB data in Neo4j is one of the very few real anti-patterns for graph databases, in my opinion.
If you have to deal with BLOB data, choose an appropriate store for that use case and use Neo4j
to store the URL that points you to the binary data.
```

## Dgraph

Docs:
* [Tutorial](https://tour.dgraph.io/)

### Store unique nodes

```javascript
{
  set {
   _:SampleNode <name> "Sample" .
  }
}

{
  set {
   _:SampleNode <name> "Sample" .
  }
}

{
  unique_nodes(func: allofterms(name, "Sample")) {
    name
  }
}
```

Two nodes are stored

### Simple query: What does Alice like?

```
 {
  set {

   _:icecream <name> "Ice-cream" .
   _:apple <name> "Apple" .
   _:pear <name> "Pear" .

   _:alice <name> "Alice" .
   _:bob <name> "Bob" .

   _:alice <likes> _:icecream .
   _:alice <likes> _:apple .
   _:alice <dislikes> _:pear .

   _:bob <likes> _:apple .
  }
}

{
  unique_nodes(func: eq(name, "Alice")) {
    name
    likes {
      name
    }
  }
}
```

Result:
```json
  "data": {
    "unique_nodes": [
      {
        "name": "Alice",
        "likes": [
          {
            "name": "Ice-cream"
          },
          {
            "name": "Apple"
          }
        ]
      }
    ]
  }
```

### Store binary values

[Storing blobs in Dgraph](https://discuss.dgraph.io/t/storing-blobs-in-dgraph/1927)
```text
Binary blobs aren’t supported right now (although I think it’s a good idea to in the future).
As a work around though, you could always do something like Base 64 encoding in a string.

There are some (arbitrary) hardcoded limits in badger for value sizes (2GB). These are in place for technical reasons
to aid recovery when the data directory in case the data directory becomes corrupted for some reason.
```

### Run Dgraph locally

```bash
docker pull dgraph/dgraph

mkdir -p ~/dgraph

# Run dgraphzero
docker run -it -p 5080:5080 -p 6080:6080 -p 8080:8080 -p 9080:9080 -p 8000:8000 -v ~/dgraph:/dgraph --name dgraph dgraph/dgraph dgraph zero

# In another terminal, now run dgraph
docker exec -it dgraph dgraph alpha --lru_mb 2048 --zero localhost:5080

# And in another, run ratel (Dgraph UI)
docker exec -it dgraph dgraph-ratel
```
