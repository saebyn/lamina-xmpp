(ns lamina-xmpp.demo
  (:gen-class)
  (:require [clojure.pprint :refer [pprint]])
  (:require [lamina-xmpp.core :refer :all])
  (:require [lamina-xmpp.xmpp :as xmpp])
  (:require [lamina.core :refer :all])
  (:require [lamina.trace :refer [with-instrumentation]])
  (:import [org.jivesoftware.smack SASLAuthentication]))


(def message-sequence
  (pipeline
    (wait-stage 3000)
    (fn [ch]
      (println "Sending first IM")
      (enqueue ch "My XMPP chat demo successfully sent an IM to you!")
      ch)
    (fn [ch]
      (run-pipeline (read-channel ch)
                    #(println "Got message: " %)
                    (fn [_] ch)))
    (fn [ch]
      (println "Sending second IM" ch)
      (enqueue ch "What do you think?")
      ch)))


(def presence-sequence
  (pipeline
    (fn [ch]
      (println "Setting status to available")
      (enqueue ch {:type :available :mode :available :status "Test"})
      ch)
    (wait-stage 10000)
    (fn [ch]
      (println "Setting status to away")
      (enqueue ch {:type :available :mode :away :status "Hiding"})
      ch)))


(defn -main
  "Demo entry point"
  [& [username password destination]]
  (pprint (with-instrumentation 
            (wait-for-result (run-pipeline (xmpp-client
                            username
                            password
                            :host "talk.google.com"
                            :port 5222
                            :service-name "gmail.com")
                          (fn [client]
                            (run-pipeline (merge-results
                                            (run-pipeline (xmpp-presence client) presence-sequence)
                                            (run-pipeline (xmpp-conversation client destination) message-sequence))
                                          (fn [_] (println "finished main stuff") client)))
                          (wait-stage 3000)
                          (fn [client]
                            (xmpp/close-connection (get-xmpp-connection client))
                            (println "closed connection"))))))
  ; I'm not 100% sure why the demo hangs here without the exit call.
  (System/exit 0))
