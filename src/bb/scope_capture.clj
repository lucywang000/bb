(ns bb.scope-capture
  (:require [sc.api]))

(defn sc-reader [form]
  `(sc.api/spy ~form))
