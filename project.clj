(defproject type-leak "0.2.17-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]  ;; bad (requires lein-cljsbuild 1.1.1)
                 #_[org.clojure/clojurescript "1.7.166"]  ;; bad (requires lein-cljsbuild 1.1.1)
                 #_[org.clojure/clojurescript "1.7.145"]  ;; bad
                 #_[org.clojure/clojurescript "1.7.122"]  ;; bad
                 #_[org.clojure/clojurescript "1.7.107"]  ;; bad
                 #_[org.clojure/clojurescript "1.7.58"]   ;; bad
                 #_[org.clojure/clojurescript "1.7.48"]   ;; bad
                 #_[org.clojure/clojurescript "1.7.28"]   ;; bad
                 #_[org.clojure/clojurescript "1.7.10"]   ;; bad
                 #_[org.clojure/clojurescript "0.0-3308"] ;; bad
                 #_[org.clojure/clojurescript "0.0-3297"] ;; bad
                 #_[org.clojure/clojurescript "0.0-3291"] ;; bad
                 #_[org.clojure/clojurescript "0.0-3269"] ;; bad
                 #_[org.clojure/clojurescript "0.0-3264"] ;; bad
                 #_[org.clojure/clojurescript "0.0-3263"] ;; bad; NPE in compilation after the warning
                 #_[org.clojure/clojurescript "0.0-3255"] ;; bad; NPE in compilation after the warning
                 #_[org.clojure/clojurescript "0.0-3211"] ;; bad
                 #_[org.clojure/clojurescript "0.0-3208"] ;; bad
                 #_[org.clojure/clojurescript "0.0-3196"] ;; bad
                 #_[org.clojure/clojurescript "0.0-3195"] ;; bad
                 ]
  :plugins [[lein-cljsbuild "1.1.1"]
            [com.cemerick/clojurescript.test "0.3.3" :exclusions [org.clojure/clojure]]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src"]
                        :compiler {:output-to "target/test/warning.js"
                                   :output-dir "target/test"
                                   :optimizations :advanced
                                   :pretty-print true}}]
              :test-commands {"test" ["phantomjs" :runner "target/test/warning.js"]}})
