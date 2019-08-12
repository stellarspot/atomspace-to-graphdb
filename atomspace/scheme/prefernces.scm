(use-modules (opencog) (opencog exec))

(Evaluation
 (Predicate "likes")
 (List
  (Concept "Alice")
  (Concept "ice-cream")))

(Evaluation
 (Predicate "likes")
 (List
  (Concept "Alice")
  (Concept "apple")))

(Evaluation
 (Predicate "dislikes")
 (List
  (Concept "Alice")
  (Concept "pear")))

(Evaluation
 (Predicate "likes")
 (List
  (Concept "Bob")
  (Concept "apple")))

; What does Alice like?
(display
 (cog-execute!
  (Get
   (Evaluation
    (Predicate "likes")
    (List
     (Concept "Alice")
     (Variable "$WHAT"))))))