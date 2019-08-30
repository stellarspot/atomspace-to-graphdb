(use-modules (opencog) (opencog exec))

(define (display-elapsed-time msg t1 t2)
 (display msg)
 (newline)
 (display (- t2 t1))
 (display "s")
 (newline)
)

(define create-time-1 (current-time))
(load "triple-graph-create.scm")
(define create-time-2 (current-time))


(cog-prt-atomspace)

(display-elapsed-time "create time:" create-time-1 create-time-2)

(define query-time-1 (current-time))
(load "triple-graph-query.scm")
(define query-time-2 (current-time))

(display-elapsed-time "query time:" query-time-1 query-time-2)
