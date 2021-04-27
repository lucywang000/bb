(ns bb.debux
  (:require [debux.core]
            [bb.better-cond :as bc]
            [bb.clojure :as b]
            [debux.cs.core]))

(defn register-debux-macros! []
  (debux.cs.core/register-macros!
   :expand-type [b/cond* bc/cond])

  (debux.core/register-macros!
   :expand-type [b/cond* bc/cond])

  (debux.cs.core/register-macros!
   :if-let-type [b/when-some* bc/when-some b/when-let* bc/when-let
                 b/if-some* bc/if-some b/if-let* bc/if-let])
  (debux.core/register-macros!
   :if-let-type [b/when-some* bc/when-some b/when-let* bc/when-let
                 b/if-some* bc/if-some b/if-let* bc/if-let]))

(register-debux-macros!)

(comment
  (register-debux-macros!)

  ())
