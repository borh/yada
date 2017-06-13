;; Copyright © 2014-2017, JUXT LTD.

(def VERSION "1.2.6")

(defproject yada/json-html VERSION
  :description "HTML rendering of JSON"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :pedantic? :abort
  :dependencies [[yada/core ~VERSION]
                 [json-html "0.4.0" :exclusions [hiccups]]
                 ])
