(ns bb.subprocess
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.java.shell :as shell])
  (:import [java.lang ProcessBuilder$Redirect]))

(def ^:dynamic *cwd* nil)

(defmacro with-cwd
  [dir & forms]
  `(binding [*cwd* ~dir
             shell/*sh-dir* ~dir]
     ~@forms))

(defn get-cmd [cmd]
  (if (string? cmd)
    ["bash" "-c" cmd]
    (do
      (assert (sequential? cmd))
      cmd)))

(defn get-command-output [cmd]
  (str/trim (:out (apply shell/sh (get-cmd cmd)))))

(defn call
  [cmd & {:keys [input cwd verbose]}]
  (when verbose
    (println (format "cmd = %s, cwd = %s" cmd (or cwd *cwd*))))
  (let [set-proc-dir (fn [proc]
                       (if-let [cwd (or cwd *cwd*)]
                         (.directory proc (io/file cwd))
                         proc))
        cmd (get-cmd cmd)
        proc (-> (ProcessBuilder. cmd)
                 (.redirectOutput ProcessBuilder$Redirect/INHERIT)
                 (.redirectError ProcessBuilder$Redirect/INHERIT)
                 set-proc-dir
                 (.start))
        proc-input (.getOutputStream proc)]
    (when input
      (with-open [w (io/writer proc-input)]
        (binding [*out* w]
          (print input)
          (flush))))
    (.waitFor proc)))

#_(call "grep foo" :input "foo\nbar")
#_(call "ls -1 |head -3" :cwd "/tmp/")

(defn rsync [src dst]
  (let [cmd ["rsync" "-av" "--exclude" ".git/" src dst]]
    (call cmd)))
