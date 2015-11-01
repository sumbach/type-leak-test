(ns warning.generator
  (:require [clojure.string :as string]
            [clojure.pprint :refer [pprint]]))

(def types
  [['StringBufferWriter ['(StringBufferWriter. nil)]]
   ['Symbol [(symbol "'abcd") '(symbol "abcd") '(symbol "")]]
   ['Var [(symbol "#'cljs.core/identity")]]
   ['ES6Iterator ['(es6-iterator nil) '(es6-iterator []) '(es6-iterator [:a :b])]]
   ['ES6IteratorSeq ['(es6-iterator-seq (es6-iterator [:a :b]))]]
   ['Reduced ['(reduced nil)]]
   ['IndexedSeq ['(seq (array :a :b)) '(seq "abcd")]]
   ['IndexedSeqIterator ['(-iterator (seq (array :a :b))) '(-iterator (seq "abcd"))]]
   ['RSeq ['(reverse [:a :b])]]
   ['MetaFn ['(with-meta js/parseInt {})]]
   ['List ['(list :a :b)]]
   ['EmptyList ['(empty '(:a :b)) '()]]
   ['Cons ['(cons :a nil) '(cons :a '()) '(cons :a [:b])]]
   ['Keyword [:abcd '(keyword "abcd") '(keyword "")]]
   ['LazySeq []]
   ['ChunkBuffer ['(chunk-buffer 0) '(chunk-buffer 2)]]
   ['ArrayChunk ['(array-chunk []) '(array-chunk [:a :b]) '(array-chunk (array)) '(array-chunk (array :a :b))]]
   ['ChunkedCons ['(chunk-cons [:a] [:b])]]
   ['StringIter ['(string-iter "") '(string-iter "abcd")]]
   ['ArrayIter ['(array-iter (array)) '(array-iter (array :a :b))]]
   ['SeqIter ['(seq-iter nil) '(seq-iter []) '(seq-iter [:a :b])]]
   ['Stepper ['(stepper identity [:a :b])]]
   ['MultiStepper ['(multi-stepper identity [[:a] [:b]])]]
   ['LazyTransformer ['(lazy-transformer (stepper identity [:a :b]))]]
   ['Atom ['(atom nil) '(atom 1)]]
   ['Volatile ['(volatile! nil) '(volatile! 1)]]
   ['VectorNode ['(.-EMPTY-NODE PersistentVector)]]
   ['RangedIterator ['(ranged-iterator [] 0 0) '(ranged-iterator [:a :b] 0 0)]]
   ['PersistentVector ['(empty [:a :b]) '[] '[:a :b]]]
   ['ChunkedSeq ['(chunked-seq [:a :b] 1 0)]]
   ['Subvec ['(subvec [:a :b] 1)]]
   ['TransientVector ['(transient []) '(transient [:a :b])]]
   ['PersistentQueue ['(.-EMPTY PersistentQueue) '(into (.-EMPTY PersistentQueue) [:a :b]) (symbol "#queue [:a :b]")]]
   ['PersistentQueueSeq ['(seq (into (.-EMPTY PersistentQueue) [:a :b]))]]
   ['NeverEquiv [(symbol "@#'cljs.core/never-equiv")]]
   ['ObjMap ['(empty (obj-map :a :b)) '(obj-map) '(obj-map :a :b)]]
   ['ES6EntriesIterator ['(es6-entries-iterator nil) '(es6-entries-iterator {}) '(es6-entries-iterator {:a :b})]]
   ['ES6SetEntriesIterator ['(es6-set-entries-iterator nil) '(es6-set-entries-iterator []) '(es6-set-entries-iterator [:a :b])]]
   ['PersistentArrayMapSeq ['(seq (array-map :a :b))]]
   ['PersistentArrayMapIterator ['(iter (array-map)) '(iter (array-map :a :b))]]
   ['PersistentArrayMap ['(empty (array-map :a :b)) '(array-map) '(array-map :a :b)]]
   ['TransientArrayMap ['(transient (array-map :a :b))]]
   ['Box []]
   ['BitmapIndexedNode ['(.-EMPTY BitmapIndexedNode)]]
   ['ArrayNode []]
   ['ArrayNodeSeq []]
   ['HashCollisionNode []]
   ['NodeSeq []]
   ['PersistentHashMap ['(empty (hash-map :a :b)) '(hash-map) '(hash-map :a :b)]]
   ['TransientHashMap ['(transient (hash-map :a :b))]]
   ['BlackNode []]
   ['RedNode []]
   ['PersistentTreeMap ['(sorted-map) '(sorted-map :a :b)]]
   ['PersistentTreeMapSeq ['(seq (sorted-map :a :b))]]
   ['KeySeq ['(keys {:a :b})]]
   ['ValSeq ['(vals {:a :b})]]
   ['PersistentHashSet ['(empty (hash-set :a :b)) '(hash-set) '(hash-set :a :b)]]
   ['TransientHashSet ['(transient (hash-set)) '(transient (hash-set :a :b))]]
   ['PersistentTreeSet ['(empty (sorted-set :a :b)) '(sorted-set) '(sorted-set :a :b)]]
   ['ArrayList ['(array-list)]]
   ['Range ['(range)]]
   ['RangeIterator ['(iter (range))]]
   ['Delay ['(->Delay (constantly :a) nil)]]
   ['Eduction ['(eduction)]]
   ['MultiFn []]
   ['UUID ['(uuid "550e8400-e29b-41d4-a716-446655440000") (symbol "#uuid \"550e8400-e29b-41d4-a716-446655440000\"")]]
   ['TaggedLiteral ['(tagged-literal 'a :b)]]
   ['cljs.reader/StringPushbackReader ['(cljs.reader/push-back-reader "abcd")]]
   ['clojure.core.reducers/Cat ['(clojure.core.reducers/cat [:a] [:b])]]])

(def protocols
  [;;['Fn ['identity '(fn [x] x) (symbol "#(%)")]] ;; TODO
   ;;['IFn ['identity '(fn [x] x) (symbol "#(%)")]] ;; TODO
   ['ICloneable []]
   ['ICounted []]
   ['IEmptyableCollection []]
   ['ICollection []]
   #_['IOrdinal []]
   ['IIndexed []]
   ['ASeq []]
   ['ISeq []]
   ['INext []]
   ['ILookup []]
   ['IAssociative []]
   ['IMap []]
   ['IMapEntry []]
   ['ISet []]
   ['IStack []]
   ['IVector []]
   ['IDeref []]
   ['IDerefWithTimeout []]
   ['IMeta []]
   ['IWithMeta []]
   ['IReduce []]
   ['IKVReduce []]
   ['IEquiv []]
   ['IHash []]
   ['ISeqable []]
   ['ISequential []]
   ['IList []]
   ['IRecord []]
   ['IReversible []]
   ['ISorted []]
   ['IWriter []]
   ['IPrintWithWriter []]
   ['IPending []]
   ['IWatchable []]
   ['IEditableCollection []]
   ['ITransientCollection []]
   ['ITransientAssociative []]
   ['ITransientMap []]
   ['ITransientVector []]
   ['ITransientSet []]
   ['IComparable []]
   ['IChunk []]
   ['IChunkedSeq []]
   ['IChunkedNext []]
   ['INamed []]
   ['IAtom []]
   ['IReset []]
   ['ISwap []]
   ['IVolatile []]
   ['IIterable []]
   ['IEncodeJS []]
   ['IEncodeClojure []]
   ['IMultiFn []]
   ['cljs.pprint/IPrettyFlush []]
   ['cljs.reader/PushbackReader []]
   ['clojure.core.reducers/CollFold []]
   ['clojure.data/EqualityPartition []]
   ['clojure.data/Diff []]])

(def others
  ;; TODO: why doesn't `instance?` work for Number, Boolean, String (but does for Function, Array, Object)
  [[(symbol "nil") ^{:type-check (symbol "nil")} [(symbol "nil") ;; TODO: warning about `clj-nil`
                                                  'js/undefined ;; from ClojureScript's perspective, `js/undefined` is the same as `nil`
                                                  ]]
   ['number ^{:type-check 'js/Number} [(symbol "0") (symbol "0.0") (symbol "12") (symbol "12.0") (symbol "12.012") (symbol "-1") (symbol "-1.0") (symbol "-1.01") (symbol "0.314e1") (symbol "314E-2") ;; TODO: warning about `number`
                                       'js/NaN 'js/Infinity '(js/parseInt "12.34") ;; NOTE: these do *not* generate a warning about `number`
                                       ]]
   ['boolean ^{:type-check 'js/Boolean} ['true 'false]]
   ['string ^{:type-check 'js/String} ["" "abcd" ;; TODO: warning about `string`
                                       '((constantly "abcd"))
                                       ]]
   ['function ^{:instance-check 'js/Function} ['identity
                                               '(fn [x] x) (symbol "#(%)") ;; TODO: warning about `function`
                                               'js/isNaN 'js/parseInt 'js/parseFloat]]
   ['array ^{:instance-check 'js/Array} ['(clj->js []) '(clj->js [:a :b]) '(make-array 0) '(make-array 2) '(array) '(array :a :b) (symbol "#js [:a :b]")]]
   ['object ^{:instance-check 'js/Object} ['(clj->js {}) '(clj->js {:a :b}) 'js/Math
                                           (symbol "#js {:a :b}") ;; TODO: warning about `object`
                                           ]]
   ['ExceptionInfo ['(ex-info "msg" {})]]
   ['default []] ;; what's this?

   ;; other js/*
   ['js/Date ['(js/Date. "2010-11-12T13:14:15.666-05:00") (symbol "#inst \"2010-11-12T13:14:15.666-05:00\"")]]
   ['js/RegExp ['(re-pattern ".")]]
   #_['js/Symbol []] ;; ?
   ['js/Error ['(js/Error. "msg")]]
   ['js/TypeError ['(js/TypeError "msg")]]
   #_['js/undefined ['js/undefined]] ;; tries to access (void 0).prototype
   #_['js/null ['js/null]] ;; tries to access null$.prototype
   ])

(defn proto-for-types
  [protocol-name method-name types]
  (for [t types]
    `(~'extend-type ~t
       ~protocol-name
       (~method-name [~'_] ~(str t)))))

(defn tests-for-types [method-name types-with-examples]
  (for [[type examples] types-with-examples]
    (let [string-name (str type)
          pretty-name (symbol (str (string/replace string-name #"[\/.]" "-") "-test"))
          type (or (-> examples meta :instance-check) type)
          type-check (-> examples meta :type-check)]
      `(~'deftest ~pretty-name
         ~@(for [ex examples]
             (if (or (-> ex       meta :skip-instance-check)
                     (-> examples meta :skip-instance-check))
               `(~'is (~'= ~string-name (~method-name ~ex)))
               `(~'let [~'ex ~ex]
                  ~(if type-check
                     `(~'is (~'= ~type-check (~'type ~'ex)))
                     `(~'is (~'or (~'instance? ~type ~'ex) (~'satisfies? ~type ~'ex))))
                  (~'is (~'= ~string-name (~method-name ~ex))))))))))

(defn -main [& args]
  (let [protocol-name 'Foo
        method-name 'foo
        all-types-with-examples (concat others types protocols)
        all-types (map first all-types-with-examples)
        protocol-extensions (proto-for-types protocol-name method-name all-types)
        test-usages (tests-for-types method-name all-types-with-examples)]
    (pprint
     '(ns warning.generated
        (:require cljs.reader
                  clojure.core.reducers
                  cljs.pprint
                  clojure.data
                  cemerick.cljs.test)
        (:require-macros [cemerick.cljs.test :refer [is deftest testing]])))
    (pprint
     `(~'defprotocol ~protocol-name
        (~method-name [~'x])))
    (doseq [x protocol-extensions]
      (pprint x))
    (doseq [x test-usages]
      (pprint x))))
