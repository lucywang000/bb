(ns bb.path
  (:require [bb.str :refer [ensure-prefix remove-suffix]]
            [clojure.java.io :as io]))

(defn exists? [path]
  (some-> path
          io/file
          .exists))

(defn join
  ([a]
   a)
  ([a b & rest]
   (let [joined (str (remove-suffix a "/")
                     (ensure-prefix b "/"))]
     (if (seq rest)
       (apply join joined rest)
       joined))))

;; backward compactibility
(def path-join join)

(defn basename [path]
  (-> path
      io/file
      .getName))

(defn dirname [path]
  (-> path
      io/file
      .getParent))

(defn abspath
  [path]
  (-> path
      io/file
      .getAbsolutePath
      io/file
      ;; get the abspath, turn to java.nio.file.Path, which could
      ;; resolve "." and ".." in its normalize method.
      .toPath
      .normalize
      .toString))

(defn get-project-dir []
  (-> (some-> *file*
              io/file
              .getParent)
      (or
       ;; *file* is null when running bb in `bb --nrepl-server 1667`
       ;; or running with --main
       (System/getProperty "user.dir"))
      (abspath)))

(comment

  (get-project-dir)

  ())
