(defproject type-leak "0.2.17-SNAPSHOT"
  :description "Lisp style templating for Facebook's React."
  :url "http://github.com/r0man/sablono"
  :author "Roman Scherer"
  :min-lein-version "2.0.0"
  :lein-release {:deploy-via :clojars}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2202"]]
  :plugins [[com.cemerick/clojurescript.test "0.3.0"]
            [lein-cljsbuild "1.0.3"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:id "test"
                        :source-paths ["test"]
                        :compiler {:output-to "target/test/sablono.js"
                                   :output-dir "target/test"
                                   :optimizations :advanced
                                   :pretty-print true}}]
              :test-commands {"phantom" ["phantomjs" :runner "target/test/sablono.js"]}}
  :resource-paths ["test-resources"])
