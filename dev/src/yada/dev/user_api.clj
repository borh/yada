;; Copyright © 2015, JUXT LTD.

(ns yada.dev.user-api
  (:require
   [clojure.tools.logging :refer :all]
   yada.resources.collection-resource
   [yada.yada :refer (yada) :as yada]
   [bidi.bidi :refer (RouteProvider tag)]
   [bidi.ring :refer (make-handler)]
   [ring.mock.request :refer (request)]
   [yada.swagger :refer (swaggered)]
   [cheshire.core :refer (decode)]
   [com.stuartsierra.component :refer (Lifecycle)]
   [schema.core :as s]
   ))

;; {"swagger":"2.0","info":{"title":"API","version":"0.0.1"},"produces":["application/json","application/x-yaml","application/edn","application/transit+json","application/transit+msgpack"],"consumes":["application/json","application/x-yaml","application/edn","application/transit+json","application/transit+msgpack"],"basePath":"/","paths":{"/api/users":{"post":{"tags":["registration"],"summary":"Register a user","parameters":[{"in":"body","name":"UserRegistrationSchema","description":"","required":true,"schema":{"$ref":"#/definitions/UserRegistrationSchema"}}],"responses":{"default":{"description":""}}},"get":{"tags":["registration"],"summary":"List users","responses":{"default":{"description":""}}}}},"definitions":{"UserRegistrationSchema":{"type":"object","properties":{"email":{"type":"string"},"password":{"type":"string"}},"required":["email","password"]}}}

(defrecord VerboseUserApi []
  Lifecycle
  (start [component]
    (assoc component
           :db {:users
                {"alice" {:email "alice@example.org"}
                 "bob" {:email "bob@example.org"}
                 }}))
  (stop [component] component)

  RouteProvider
  (routes [{:keys [db]}]
    ["/api"
     (->
      (swaggered
       {:info {:title "User API"
               :version "0.0.1"
               :description "Example user API"}
        :basePath "/api"}
       ["" {"/users"
            {""
             (yada
              (:users db)
              :swagger {:get {:summary "Get users"
                              :description "Get a list of all known users"}})

             ["/" :username]
             {"" (yada
                  ;; TODO What does this function even mean?  Looks like the
                  ;; intent is the final fetch, but this means that all the
                  ;; methods, produces, etc. need to be defined up front.
                  (fn [ctx]
                    (when-let [user (get {"bob" {:name "Bob"}}
                                         (-> ctx :parameters :username))]
                      {:user user}))
                  :swagger {:get {:summary "Get user"
                                  :description "Get the details of a known user"}}
                  :parameters {:get {:path {:username s/Str}}})

              "/posts" (yada
                        "Posts"          ; TODO
                        :swagger {:post {:summary "Create a new post"}}
                        :post (fn [ctx] nil))}}}])
      ;; TODO: Might be able to unresolve-handler on yada's Endpoint and
      ;; not have to tag like this, that would be nice!
      (tag ::user-api))]))

(defn new-verbose-user-api []
  (->VerboseUserApi))
