## BB - Backup Batteries

Some utils, that are useful sometimes.

[![Clojars Project](https://img.shields.io/clojars/v/bb.svg)](https://clojars.org/bb)

## [`bb.clojure/cond*`](https://github.com/lucywang000/bb/blob/master/src/bb/clojure.cljc)

This is the same as `better-cond.core/cond` in the [better-cond](https://github.com/Engelberg/better-cond) library, but with support of running in [babashka](https://github.com/babashka/babashka) (by removing the dependency on `clojure.spec`).

Usage is exactly the same as `better-cond`:

```clojure
(ns example.core
   (:require [bb.clojure :as bb]))

 (bb/cond*
   (odd? a) 1
   :let [a (quot a 2)]
   :when-let [x (fn-which-may-return-falsey a),
              y (fn-which-may-return-falsey (* 2 a))]
   :when-some [b (fn-which-may-return-nil x),
               c (fn-which-may-return-nil y)]
   :when (seq x)
   :do (println x)
   (odd? (+ x y)) 2
   3)

```

Other funtions in better-cond family are also included:

- `bb.clojure/when-let*`
- `bb.clojure/when-some*`
- `bb.clojure/if-some*`
- `bb.clojure/if-let*`

## [`bb.debux`](https://github.com/lucywang000/bb/blob/master/src/bb/debux.clj)

Setting up [debux](https://github.com/philoskim/debux) intgration for the better-cond family of macros in clojure/clojurescript.

Usage: simply require the `bb.debux` namespace somewhere in your code. (Even if you use it in clojurescript, you still have to import it in a .clj file, e.g. `dev/src/user.clj`.)

## [`bb.meander`](https://github.com/lucywang000/bb/blob/master/src/bb/meander.clj)

Some custom [meander](https://github.com/noprompt/meander/) syntax sugars.

## [`bb.scope-capture`](https://github.com/lucywang000/bb/blob/master/src/bb/scope_capture.clj)

Add a [data reader](https://github.com/lucywang000/bb/blob/master/src/bb/data_readers.clj) `#sp` for [scope-capture](https://github.com/vvvvalvalval/scope-capture)

## [`bb.path`](https://github.com/lucywang000/bb/blob/master/src/bb/path.clj)

Provides path manipulation functions like:

- `bb.path/join`
- `bb.path/basename`
- `bb.path/dirname`
- `bb.path/exists?`

A better alternative is [babashka.fs](https://github.com/babashka/fs).

## [`bb.subprocess`](https://github.com/lucywang000/bb/blob/master/src/bb/subprocess.clj)

A few utils functions for spawning a child process.

A better alternative is [babashka.process](https://github.com/babashka/process).

## [`bb.str`](https://github.com/lucywang000/bb/blob/master/src/bb/str.clj)

Some string utils.
