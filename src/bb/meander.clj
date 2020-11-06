(ns bb.meander
  (:require [meander.strategy.epsilon :as m*]
            [clojure.string :as str]
            [com.rpl.specter :as sp]
            [meander.epsilon :as me]))

(me/defsyntax maybe-scan
  "Do we scan with optional match, a.k.a left-joining.

    (me/search {:a [{:id 1}
                    {:id 2}]
                :b [{:id 1 :age 10}]}
    {:a (me/scan {:id ?id})
    :b (maybe-scan {:id ?id} {:age ?age})}
    {:id ?id :age ?age})
    ;; => ({:id 1, :age 10} {:id 2, :age nil})"
  [required optional]
  (if (me/match-syntax? &env)
    `(me/or
       (me/scan ~(merge required optional))
       (me/let ~(-> optional
                    vals
                    (interleave (repeat nil))
                    vec)
         (me/not (me/scan ~required))))
    &form))

(me/defsyntax expr
  "Express the relationship between two matches.

    (search [[1 2] [1 3] [2 3]]
      (scan [?x (expr (+ 1 ?x) :as ?y)])
      [?x ?y])
    ;; =>
    ([1 2] [2 3])"
  ([_expr]
   (expr _expr :as (gensym "?result")))
  ([_expr _ lvr]
   (if (me/match-syntax? &env)
     `(me/let [~lvr ~_expr]
        ~lvr)
     &form)))

(defn logic-variable? [x]
  (and (symbol? x)
       (str/starts-with? (name x) "?")))

(def eliminate-maybe
  (m*/rewrite
   ('maybe _) nil))

(def remove-nested-maybes
  (m*/bottom-up
   (m*/attempt eliminate-maybe)))

(defn extract-logic-variables [m]
  (-> m
      ;; remove-nested-maybes
      (me/search (me/$ (me/pred logic-variable? ?lvr)) ?lvr)))


(me/defsyntax maybe
  "Mark the following match as optional.

    (me/search [{:a 1}
                {:a 1 :b [2 3]}]
    (me/scan {:a ?a
                :b (maybe [?b1 ?b2])})
    {:a ?a :b1 ?b1 :b2 ?b2})
    ;; => ({:a 1, :b1 nil, :b2 nil} {:a 1, :b1 2, :b2 3})

    (me/search [{:a 1}
                {:a 1 :b {:c 1 :d 2}}]
    (me/scan {:a ?a
              :b (maybe {:c ?c
                         :d ?d})})
    {:a ?a :c ?c :d ?d})
    ;; => ({:a 1, :c nil, :d nil} {:a 1, :c 1, :d 2})"
  [m]
  (if (me/match-syntax? &env)
    `(me/or
       (me/some ~m)
       ~(concat '(me/and nil) (extract-logic-variables m)))
    &form))

(defn not-contains [m k]
  (not (contains? m k)))

(defn extract-nil-bindings [x]
  (-> x
      (extract-logic-variables)
      (interleave (repeat nil))
      vec))

(defn maybe-map* [kpattern vpattern]
  ;; :bonuses (me/or {?id (me/some ?bonus)}
  ;;                 (me/let [?bonus nil]
  ;;                   (me/not {?id _})))}


    ;; :bonuses (me/or {?id (me/some ?bonus)}
    ;;                 (me/let [?bonus nil]
    ;;                   (me/pred #(-> (contains? % ?id) not))))}
  `(me/or
     {(me/some ~kpattern) (me/some ~vpattern)}
     (me/let ~(extract-nil-bindings vpattern)
         (me/pred #(not (contains? % ~kpattern))))))

(me/defsyntax maybe-map
  "Mark the following match as optional."
  [kpattern vpattern]
  (if (me/match-syntax? &env)
    (maybe-map* kpattern vpattern)
    &form))


(me/defsyntax gen-map
  "Shorthand for the map spread syntax

   (gen-map !k !v) would expand to:

   {& ([!k !v] ...)}

   (me/rewrite [1 2 3 4]
   [!xs !ys ...]
   (gen-map !xs !ys))
   ;; => {1 2, 3 4}"
  [!k !v]
  `{~'& ([~!k ~!v] ~'...)})

#_(me/search [{:a 1}
            {:a 1 :b {:b1 1}}
            {:a 1 :b {:b1 1 :b2 {:value 2}}}]
  (me/scan {:a ?a
            :b (maybe {:b1 ?b1
                       :b2 (maybe {:value ?b2})})})
  {:a ?a :b1 ?b1 :b2 ?b2})

#_(me/search [{:a 1}
           {:a 1 :b {:b1 1}}
           {:a 1 :b {:b1 1 :b2 {:value 2}}}]
  (me/scan {:a ?a
           :b (me/or (me/and nil ?b1 ?b2)
                     {:b1 ?b1
                      :b2 (me/or (me/and nil ?b2)
                                 ?b2)
                      })})
  {:a ?a :b1 ?b1 :b2 ?b2})

(comment
  (logic-variable? '?x)
  (extract-logic-variables '{:a ?a :b ?b :c "?c" :d :?d})
  (extract-logic-variables '{:a ?a :b ?b :c (maybe [?c1 ?c2])})
  (remove-nested-maybes '{:a ?a
                          :b ?b
                          :c (maybe [?c1 ?c2])
                          :d (maybe [?d1 ?d2])})
  (extract-logic-variables '?x)
  (extract-logic-variables '{:a ?a
                             :b ?b
                             :c (maybe [?c1 ?c2])
                             :d (maybe [?d1 ?d2])})

  (me/search [{:a 1}
              {:a 1 :b {:c 1 :d 2}}]
    (me/scan {:a ?a
              :b (maybe {:c ?c
                         :d ?d})})
    {:a ?a :c ?c :d ?d})

  (me/search {:persons [{:id 1}
                        {:id 2}]
              :bonuses {1 100}}
    {:persons (me/scan {:id ?id})
     :bonuses (maybe-map ?id ?bonus)}
    {:id ?id
     :bonus ?bonus})

  (me/search {:persons [{:id 1}
                        {:id 2}]
              :bonuses {1 100}}
    {:persons (me/scan {:id ?id})
     :bonuses {?id (me/or (me/some ?bonus)
                          (me/and nil ?bonus))}}
    {:id ?id
     :bonus ?bonus})

  (me/search {:persons [{:id 1}
                        {:id 2}]
              :bonuses {1 100}}
    {:persons (me/scan {:id ?id})
     :bonuses (me/or {?id (me/some ?bonus)}
                     (me/let [?bonus nil]
                       (me/pred #(-> (contains? % ?id) not))))}
    {:id ?id
     :bonus ?bonus})

  maybe ?id ?bonus


  (me/search {:a [{:id 1}
                  {:id 2}]
              :b [{:id 1 :age 10}]}
    {:a (me/scan {:id ?id})
     :b (maybe-scan {:id ?id} {:age ?age})}
    {:id ?id :age ?age})
  ())
