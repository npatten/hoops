(defproject hoops "0.1.0-SNAPSHOT"
  :description "Hooper name generator"
  :url ""
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0-alpha3"]
                 [liberator "0.10.0"]
                 [ring/ring-jetty-adapter "1.1.6"]
                 [compojure "1.0.2"]
                 [ring/ring-devel "1.1.6"]
                 [org.clojure/tools.reader "0.8.3"]]
  :plugins [[lein-ring "0.7.1" :exclusions [org.clojure/clojure]]]
  :profiles {:dev {:dependencies [[com.cemerick/pomegranate "0.2.0"]]}})
