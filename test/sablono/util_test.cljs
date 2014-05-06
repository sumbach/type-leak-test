(ns sablono.util-test
  (:require-macros [cemerick.cljs.test :refer [are deftest]])
  (:require [cemerick.cljs.test :as t]))

(defprotocol Foo
  (foo [x]))

(extend-protocol Foo
  string
  (foo [x] x))

(deftest test-foo
  (are [x y] (= x (foo y))
       "foo" "foo"
       "foo" "foo"
       "foo" "foo"
       "foo" "foo"
       "foo" "foo"))
