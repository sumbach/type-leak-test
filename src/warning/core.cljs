(ns warning.core
  (:require cemerick.cljs.test)
  (:require-macros [cemerick.cljs.test :refer [is deftest testing]]))

(defprotocol Foo
  (foo [x]))

(extend-protocol Foo
  nil
  (foo [_] "nil")

  number
  (foo [_] "number")

  string
  (foo [_] "string")

  boolean
  (foo [_] "boolean")

  function
  (foo [_] "function")

  object
  (foo [_] "object")

  array
  (foo [_] "array"))

(deftest clojurescript-compiler-warnings
  (is (= "nil"      (foo nil)))        ;; WARNING: Use of undeclared Var warning.core/clj-nil
  (is (= "nil"      (foo js/undefined)))
  (is (= "number"   (foo 0)))          ;; WARNING: Use of undeclared Var warning.core/number
  (is (= "string"   (foo "bar")))      ;; WARNING: Use of undeclared Var warning.core/string
  (is (= "boolean"  (foo true)))
  (is (= "boolean"  (foo false)))
  (is (= "function" (foo identity)))
  (is (= "function" (foo (fn [x] x)))) ;; WARNING: Use of undeclared Var warning.core/function
  (is (= "function" (foo #(%))))       ;; WARNING: Use of undeclared Var warning.core/function
  (is (= "object"   (foo #js {})))     ;; WARNING: Use of undeclared Var warning.core/object
  (is (= "array"    (foo #js []))))
