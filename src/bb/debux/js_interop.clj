(ns bb.debux.js-interop
  (:require [debux.core]
            [bb.debux]
            [applied-science.js-interop :as j]
            [debux.cs.core]))

(defn register-debux-macros! []
    (debux.cs.core/register-macros!
      :let-type [j/let])
    (debux.cs.core/register-macros!
        :skip-all-args-type [j/get-in])
    (debux.cs.core/register-macros!
      :skip-all-args-type [j/lit])
    (debux.cs.core/register-macros!
      :skip-arg-2-type [j/call-in j/assoc-in! j/update-in! j/apply-in])

    (debux.core/register-macros!
      :let-type [j/let])
    (debux.core/register-macros!
        :skip-all-args-type [j/get-in])
    (debux.core/register-macros!
      :skip-all-args-type [j/lit])
    (debux.core/register-macros!
      :skip-arg-2-type [j/call-in j/assoc-in! j/update-in! j/apply-in]))

(register-debux-macros!)

(comment

;; ;; js-interop
;; j/in? j/unchecked-get j/!get j/unchecked-set j/!set j/contains? j/get
;; j/get-in j/!get-in j/select-keys j/some-or j/assoc! j/assoc-in! j/!assoc-in!
;; j/!update j/update! j/update-in! j/push! j/unshift! j/call j/call-in j/apply
;; j/apply-in j/obj j/lit j/fn j/defn

  ())
