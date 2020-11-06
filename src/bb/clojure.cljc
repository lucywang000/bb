(ns bb.clojure
  "Some helpers that I hope clojure could provide out of the box."
  (:require [bb.better-cond :as b])
  #?(:cljs (:require-macros [bb.clojure])))

#?(:clj
   (defmacro if-let* [& body]
     `(b/if-let ~@body)))

#?(:clj
   (defmacro when-let* [& body]
     `(b/when-let ~@body)))

#?(:clj
   (defmacro when-some* [& body]
     `(b/when-some ~@body)))

#?(:clj
   (defmacro when-let* [& body]
     `(b/when-let ~@body)))

#?(:clj
   (defmacro cond* [& body]
     `(b/cond ~@body)))

#?(:clj
   (defmacro prog1 [expr & body]
     `(let [~'<> ~expr]
        (do
          ~@body)
        ~'<>)))

(defn dict
  "Like dictionary comprehension in python: {k: f(k) for k in coll}"
  [coll f]
  (zipmap coll (map f coll)))

(defn ensure-vector [x]
  (if (vector? x)
    x
    [x]))
