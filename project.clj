(defproject appengine-t "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :aot [appengine-t.app_servlet]
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojars.intronic/appengine-magic "0.5.1-SNAPSHOT"]
                 [appengine-magic "0.5.1-SNAPSHOT"]
                 [compojure "1.1.6"]
                 [lib-noir "0.7.5"]
                 [hiccup "1.0.4"]]
  :plugins [[org.clojars.intronic/appengine-magic "0.5.1-SNAPSHOT"]]
  :compile-path "war/WEB-INF/classes"
  :library-path "war/WEB-INF/lib"
  )
