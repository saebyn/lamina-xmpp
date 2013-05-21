(ns lamina-xmpp.demo
  (:gen-class)
  (:require [lamina-xmpp.core :as core])
  (:require [lamina-xmpp.xmpp :as xmpp])
  (:import [org.jivesoftware.smack
                        Chat ChatManager ConnectionConfiguration MessageListener
                  SASLAuthentication XMPPConnection XMPPException PacketListener]
               [org.jivesoftware.smack.packet
                            Message Presence Presence$Type Message$Type]))

(defn -main
  "Application entry point"
  [& [username password destination]]
  (SASLAuthentication/supportSASLMechanism "PLAIN" 0)
  (let [connection (xmpp/open-connection
                     username
                     password
                     :host "talk.google.com"
                     :port 5222
                     :service-name "gmail.com")
        presence (Presence. Presence$Type/available)]
    (.sendPacket connection presence)
    (if (.isAuthenticated connection)
      (let [chat (.createChat (.getChatManager connection) destination nil)]
        (try
          (.sendMessage chat "My XMPP chat demo successfully sent an IM to you!")
          (catch XMPPException e
            (println "sendMessage threw exception")
            (throw e))))
      (println "Failed to login"))
    (Thread/sleep 10000)
    (.disconnect connection)))
