(defproject bb "0.1.0"
  :description "Some utils, that are useful sometimes."
  :url "https://github.com/lucywang000/bb"
  :min-lein-version "2.5.0"

  :dependencies [[org.clojure/clojure "1.10.3" :scope "provided"]
                 #_[vvvvalvalval/scope-capture "0.3.3-s1"]
                 #_[medley "1.3.0"]
                 #_[cprop "0.1.18"
                  :exclusions [org.clojure/clojure]]]

  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :username      :env/clojars_user
                                    :password      :env/clojars_token
                                    :sign-releases false}]]

  :target-path "target/%s")
