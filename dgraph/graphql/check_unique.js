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
