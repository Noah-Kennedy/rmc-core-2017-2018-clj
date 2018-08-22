(defproject rmc-core-2018-2019 "0.1.0-SNAPSHOT"
   :description "The MSOE RMC Core Control Module Code"
   :url "http://example.com/FIXME"
   :license {:name "Eclipse Public License"
             :url  "http://www.eclipse.org/legal/epl-v10.html"}
   :dependencies [[org.clojure/clojure "1.9.0"]
                  [aleph "0.4.5-alpha6"]
                  [com.github.purejavacomm/purejavacomm "1.0.2.RELEASE"]
                  [org.clojure/core.async "0.4.474"]
                  [org.clojure/core.specs.alpha "0.1.24"]
                  [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                     javax.jms/jms
                                                     com.sun.jdmk/jmxtools
                                                     com.sun.jmx/jmxri]]]
   :bootclasspath true
   :global-vars {*warn-on-reflection* true
                 *assert*             true}
   :plugins [[lein-cloverage "1.0.11-SNAPSHOT"]
             [lein-kibit "0.1.6"]]
   :source-paths ["src/clojure"]
   :java-source-paths ["src/java"]
   :test-paths ["test"]
   :main ^:skip-aot rmc_core_2018_2019.core
   :target-path "target/%s"
   :profiles {:uberjar {:aot :all}
              :dev     {:dependencies []}})