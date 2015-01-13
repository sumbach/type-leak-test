(ns warning.core)

(defprotocol Foo
  (foo [x]))

(extend-protocol Foo
  string
  (foo [x] x))

(foo "bar")
