(ns bb.str
  (:require [clojure.string :as str]))

(defn remove-suffix [s suffix]
  (if (str/ends-with? s suffix)
    (subs s 0 (- (.length s) (.length suffix)))
    s))

#_(remove-suffix "/" "/")

(defn ensure-prefix [s prefix]
  (if (str/starts-with? s prefix)
    s
    (str prefix s)))

#_(ensure-prefix "abc" "/")

(defn ensure-suffix [s prefix]
  (if (str/ends-with? s prefix)
    s
    (str s prefix)))

#_(ensure-suffix "abc" "/")
