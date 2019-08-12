// Alice
CREATE (alice:Alice)-[:LIKES]->(:ice_cream)
CREATE (alice)-[:LIKES]->(:apple)
CREATE (alice)-[:DISLIKES]->(:pear)
// Bob
CREATE (bob:Bob)-[:LIKES]->(:apple)

MATCH (:Alice)-[:LIKES]->(what) RETURN what;