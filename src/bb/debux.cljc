(ns bb.debux
  (:require [debux.core]
            #?(:cljs [applied-science.js-interop :as j])
            [bb.better-cond :as bc]
            [bb.clojure :as b]
            [debux.cs.core]))

(defn register-debux-macros! []
  #?(:cljs
     (do
       (debux.cs.core/register-macros!
        :let-type [j/let]))

     (debux.cs.core/register-macros!
      :skip-all-args-type [j/get-in])

     (debux.cs.core/register-macros!
      :skip-all-args-type [j/lit]

      (debux.cs.core/register-macros!
       :skip-arg-2-type [j/call-in j/assoc-in! j/update-in! j/apply-in]))

     (debux.cs.core/register-macros!
      :expand-type [b/cond* bc/cond]))

  (debux.core/register-macros!
   :expand-type [b/cond* bc/cond])

  (debux.cs.core/register-macros!
   :if-let-type [b/when-some* bc/when-some b/when-let* bc/when-let
                 b/if-some* bc/if-some b/if-let* bc/if-let])
  (debux.core/register-macros!
   :if-let-type [b/when-some* bc/when-some b/when-let* bc/when-let
                 b/if-some* bc/if-some b/if-let* bc/if-let])

  ())

(comment
  (register-debux-macros!)

;; ;; js-interop
;; j/in? j/unchecked-get j/!get j/unchecked-set j/!set j/contains? j/get
;; j/get-in j/!get-in j/select-keys j/some-or j/assoc! j/assoc-in! j/!assoc-in!
;; j/!update j/update! j/update-in! j/push! j/unshift! j/call j/call-in j/apply
;; j/apply-in j/obj j/lit j/fn j/defn

  ())
