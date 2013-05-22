(ns lamina-xmpp.demo
  (:gen-class)
  (:require [lamina-xmpp.core :refer :all])
  (:require [lamina-xmpp.xmpp :as xmpp])
  (:require [lamina.core :refer :all])
  (:import [org.jivesoftware.smack
                        Chat ChatManager ConnectionConfiguration MessageListener
                  SASLAuthentication XMPPConnection XMPPException PacketListener]
               [org.jivesoftware.smack.packet
                            Message Presence Presence$Type Message$Type]))

(defn -main
  "Demo entry point"
  [& [username password destination]]
  (SASLAuthentication/supportSASLMechanism "PLAIN" 0)
  (let [connection (wait-for-result
                     (xmpp-client
                        username
                        password
                        :host "talk.google.com"
                        :port 5222
                        :service-name "gmail.com"))
        presence (Presence. Presence$Type/available)]
    (xmpp-presence-client connection)
    (.sendPacket (get-xmpp-connection connection) presence)
    (let [chat (.createChat (.getChatManager (get-xmpp-connection connection)) destination nil)]
      (try
        (.sendMessage chat "My XMPP chat demo successfully sent an IM to you!")
        (catch XMPPException e
          (println "sendMessage threw exception")
          (throw e))))
    (Thread/sleep 5000)
    (.disconnect (get-xmpp-connection connection))))
