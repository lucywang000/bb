(ns bb.git
  (:require [clojure.java.io :as io]
            [bb.str :refer [ensure-prefix remove-suffix ensure-suffix]]
            [bb.subprocess :refer [get-command-output]]
            [clojure.string :as str]
            [clojure.java.shell :as shell])
  (:import [java.lang ProcessBuilder$Redirect]
           [java.nio.file Path]))

(defn get-git-version []
  (-> (get-command-output "git rev-parse --short HEAD")))
