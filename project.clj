(defproject lamina-xmpp "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lamina "0.5.0-rc2"]
                 [jivesoftware/smack "3.1.0"]]
  :main lamina-xmpp.demo
  :profiles {:dev {:dependencies [[midje "1.5.0"]]}})
