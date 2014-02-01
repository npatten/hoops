(ns hoops.core
  (:require [liberator.core :as lib :refer (defresource)]
            [compojure.core :refer (routes ANY)]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.multipart-params :refer (wrap-multipart-params)]
            [clojure.tools.reader :as rdr]
            [compojure.handler :refer (api)]))

;;; Hooper Name Generator
;;; 
;;; 



(def hooper-map
  ;; Map of hooper first and last names
  {:fname ["Hooper" "Luna" "Dizzy" "Hooped" "Tinker"]
   :lname ["Star" "Hoops" "Galloopsie"]})


(defn full-hoop-name [fname lname]
  (str fname " " lname))

;; rand-name: fname | lname -> String
(defn rand-name [t] (rand-nth (t hooper-map)))

 
(defn matching-name? [name]
  ;; Randomly pick a name from the subset of hooper first names that begin with the same-
  ;;  letter as [name]
  (seq (filter #(= (first %) (first name)) (hooper-map :fname))))


(defn build-hooper-name [{fname :fname, lname :lname
                          :or {fname (rand-name :fname), lname (rand-name :lname)}}]
;; Some fancyness to default to a random first or last hoop name if one isn't provided.
  (full-hoop-name fname lname))


(defn determine-hoop-name [names]
  (let [matching-hoop-names (matching-name? (:fname names))]
       (if matching-hoop-names
         (full-hoop-name (-> matching-hoop-names rand-nth) (rand-name :lname))
         (build-hooper-name names))))

(defn query-params [ctx]
  (get-in ctx [:request :query-params]))

(defresource hoop-name
  :available-media-types ["text/plain"]
  :handle-ok (fn [ctx]
               (let [params (query-params ctx)
                     {:strs [firstname]} params]
                 (if firstname
                   (determine-hoop-name  {:fname firstname})
                   (build-hooper-name {})))))


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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(comment
  (generate-hooper-name)
  (require '[cemerick.pomegranate :as pom])
  (require '[cemerick.pomegranate.aether :as aether])
  (pom/add-dependencies :coordinates '[[clojure.tools.reader "0.8.3"]]
                        :repositories (merge aether/maven-central {"clojars" "http://clojars.org/repo"}))
  )
