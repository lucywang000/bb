## BB - Backup Batteries

Some utils, that are useful sometimes.

[![Clojars Project](https://img.shields.io/clojars/v/bb.svg)](https://clojars.org/bb)

## `bb.clojure/cond*`

This is an alias to `better-cond.core/cond` in the [better-cond](https://github.com/Engelberg/better-cond) library 

Other funtions in better-cond family is also included:
- `bb.clojure/when-let*`
- `bb.clojure/when-some*`
- `bb.clojure/if-some*`
- `bb.clojure/if-let*`

## `bb.debux`

Setting up [debux](https://github.com/philoskim/debux) intgration for the better-cond family of macros.

## `bb.meander`

Some custom [meander](https://github.com/noprompt/meander/) syntax sugars.


## `bb.scope-capture`

Add a data reader `#sp` for [scope-capture](https://github.com/vvvvalvalval/scope-capture)

## `bb.path`

Provides path manipulation functions like:
- `bb.path/join`
- `bb.path/basename`
- `bb.path/dirname`
- `bb.path/exists?`

A better alternative is [babashka.process](https://github.com/babashka/fs).


## `bb.subprocess`

A few utils functions for spawning a child process. A better alternative is [babashka.process](https://github.com/babashka/process).

## `bb.str`

Some string utils.
