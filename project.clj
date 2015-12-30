(defproject rss-finder "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://seabears.net"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [http-kit "2.1.18"]]
  :main ^:skip-aot net.seabears.rss-finder.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
