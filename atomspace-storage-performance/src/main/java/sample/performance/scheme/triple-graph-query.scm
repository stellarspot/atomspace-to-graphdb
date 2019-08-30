(display
 (cog-execute!
  (Get
   (EvaluationLink (PredicateNode "likes") (ListLink (ConceptNode "Alice") (VariableNode "$WHAT"))))))

(display
 (cog-execute!
  (Get
   (EvaluationLink (PredicateNode "likes") (ListLink (ConceptNode "Bob") (VariableNode "$WHAT"))))))
