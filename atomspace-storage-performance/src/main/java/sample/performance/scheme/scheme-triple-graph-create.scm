(use-modules (opencog) (opencog exec))

(define ct1 (current-time))
(load "triple-graph-create.scm")
(define ct2 (current-time))


(cog-prt-atomspace)

(display "elapsed time: ")
(display (- ct2 ct1))
(display "s")
(newline)