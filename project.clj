(defproject rmc-core-2018-2019 "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [venantius/pyro "0.1.2"]
                 [aleph "0.4.4"]
                 [com.github.purejavacomm/purejavacomm "1.0.2.RELEASE"]]
  :main ^:skip-aot rmc-core-2018-2019.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[jonase/eastwood "0.2.5" :exclusions [org.clojure/clojure]]]}})
