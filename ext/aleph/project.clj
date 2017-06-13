;; Copyright © 2014-2017, JUXT LTD.

(def VERSION "1.2.6")

(defproject yada/aleph VERSION
  :description "yada integration with Aleph (and Netty)"
  :license {:name "The MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :pedantic? :abort
  :dependencies [[aleph "0.4.1" :exclusions [io.aleph/dirigiste]]
                 [yada/core ~VERSION]])
