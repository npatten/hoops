(ns hoops.core
  (:require [liberator.core :as lib :refer (defresource)]
            [compojure.core :refer (routes ANY)]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.multipart-params :refer (wrap-multipart-params)]
            [clojure.tools.reader :as rdr]
            [compojure.handler :refer (api)]))

(def hooper-map
  {:fname ["Hooper" "Luna" "Dizzy" "Hooped" "Tinker"]
   :lname ["Star" "Hoops" "Galloopsie"]})

(defn full-hoop-name [fname lname]
  (str fname " " lname))

(defn rand-name [t] (rand-nth (t hooper-map)))

(defn matching-name? [name first-or-last]
  (seq (filter #(= (first %) (first fname))
)))

(defn build-hooper-name [{fname :fname, lname :lname
                          :or {fname (rand-fname), lname (rand-lname)}}]
  (full-hoop-name fname lname))

(defn determine-hoop-name [fname]
  (let [matching-hoop-names (filter #(= (first %) (first fname)) hooper-first-names)]
    (if (matching-name?)
      (full-hoop-name (-> matching-hoop-names rand-nth) (rand-lname))
      (build-hooper-name fname))))

(defn query-params [ctx]
  (get-in ctx [:request :query-params]))

(defresource hoop-name
  :available-media-types ["text/plain"]
  :handle-ok (fn [ctx]
               (let [params (query-params ctx)
                     {:strs [firstname]} params]
                 (if firstname
                   (determine-hoop-name firstname)
                   (generate-hooper-name {:edn true})))))

(defn all-routes []
  (routes (ANY "/" {} hoop-name)))

(def handler

  (-> (all-routes)
      api
      wrap-multipart-params))

(defn start [opts]
  (jetty/run-jetty #'handler (assoc opts :join? false)))

(defn -main []
  (start {:port 3000}))

(comment
  (generate-hooper-name)
  (require '[cemerick.pomegranate :as pom])
  (require '[cemerick.pomegranate.aether :as aether])
  (pom/add-dependencies :coordinates '[[clojure.tools.reader "0.8.3"]]
                        :repositories (merge aether/maven-central {"clojars" "http://clojars.org/repo"}))
)
