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
