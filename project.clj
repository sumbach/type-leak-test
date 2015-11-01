(defproject type-leak "0.2.17-SNAPSHOT"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.145"]]
  :plugins [[lein-cljsbuild "1.1.0"]]
  :hooks [leiningen.cljsbuild]
  :cljsbuild {:builds [{:id "test"
                        :source-paths ["src"]
                        :compiler {:output-to "target/test/warning.js"
                                   :output-dir "target/test"
                                   :optimizations :advanced
                                   :pretty-print true}}]})
