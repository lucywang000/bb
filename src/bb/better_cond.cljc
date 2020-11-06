(ns bb.better-cond
  "A collection of variations on Clojure's core macros. Let's see which features
   end up being useful."
  {:author "Christophe Grand and Mark Engelberg"}
  (:refer-clojure :exclude [cond when-let if-let when-some if-some])

  #?(:cljs (:require-macros [bb.better-cond])))

#?(:clj
   (defmacro if-let
     "A variation on if-let where all the exprs in the bindings vector must be true.
   Also supports :let."
     ([bindings then]
      `(bb.better-cond/if-let ~bindings ~then nil))
     ([bindings then else]
      ;; (println 'bindings '=> bindings)
      (if (seq bindings)
        (if (or (= :let (bindings 0)) (= 'let (bindings 0)))
          `(let ~(bindings 1)
             (bb.better-cond/if-let ~(subvec bindings 2) ~then ~else))
          `(let [test# ~(bindings 1)]
             (if test#
               (let [~(bindings 0) test#]
                 (bb.better-cond/if-let ~(subvec bindings 2) ~then ~else))
               ~else)))
        then))))

#?(:clj
   (defmacro when-let
     "A variation on when-let where all the exprs in the bindings vector must be true.
   Also supports :let."
     [bindings & body]
     `(bb.better-cond/if-let ~bindings (do ~@body))))

#?(:clj
   (defmacro if-some
   "A variation on if-some where all the exprs in the bindings vector must be non-nil.
   Also supports :let."
   ([bindings then]
    `(bb.better-cond/if-some ~bindings ~then nil))
   ([bindings then else]
    (if (seq bindings)
      (if (or (= :let (bindings 0)) (= 'let (bindings 0)))
        `(let ~(bindings 1)
           (bb.better-cond/if-some ~(subvec bindings 2) ~then ~else))
        `(let [test# ~(bindings 1)]
           (if (nil? test#)
             ~else
             (let [~(bindings 0) test#]
               (bb.better-cond/if-some ~(subvec bindings 2) ~then ~else)))))
      then))))

#?(:clj
   (defmacro when-some
   "A variation on when-some where all the exprs in the bindings vector must be non-nil.
   Also supports :let."
   [bindings & body]
   `(bb.better-cond/if-some ~bindings (do ~@body))))

#?(:clj
   (defmacro cond
   "A variation on cond which sports let bindings, do and implicit else:
     (cond
       (odd? a) 1
       :do (println a)
       :let [a (quot a 2)]
       (odd? a) 2
       3).
   Also supports :when-let and :when-some.
   :let, :when-let, :when-some and :do do not need to be written as keywords."
   [& clauses]
   (bb.better-cond/when-let [[test expr & more-clauses] (seq clauses)]
     (if (next clauses)
       (if (or (= :do test) (= 'do test))
         `(do ~expr (bb.better-cond/cond ~@more-clauses))
         (if (or (= :let test) (= 'let test))
           `(let ~expr (bb.better-cond/cond ~@more-clauses))
           (if (or (= :when test) (= 'when test))
             `(when ~expr (bb.better-cond/cond ~@more-clauses))
             (if (or (= :when-let test) (= 'when-let test))
               `(bb.better-cond/when-let ~expr (bb.better-cond/cond ~@more-clauses))
               (if (or (= :when-some test) (= 'when-some test))
                 `(bb.better-cond/when-some ~expr (bb.better-cond/cond ~@more-clauses))
                 (if (or (= :j/let test) (= 'j/let test))
                   `(applied-science.js-interop/let ~expr (bb.better-cond/cond ~@more-clauses))
                   `(if ~test ~expr (bb.better-cond/cond ~@more-clauses))))))))
       test))))
