(ns lamina-xmpp.core
  (:require [lamina.executor :refer [task]])
  (:require [lamina.core :refer [channel-pair run-pipeline enqueue]])
  (:require [lamina-xmpp.xmpp :as xmpp]))


(defn get-xmpp-connection
  "Gets the XMPPConnection instance from the handle yielded by xmpp-client."
  [connection]
  connection)


(defn xmpp-client [& args]
  "Returns a task that yields the connection to the XMPP server."
  (task (apply xmpp/open-connection args)))


; xmpp-presence: takes a connection and returns a connection for sending and receiving presence data
(defn xmpp-presence-client [connection]
  (let [[client server] (channel-pair)]
    ; Process messages from client channel, pass through to connection
    ; Listen for messages from connection, pass through to server channel
    (xmpp/packet-listener connection
      (fn [packet]
        (println packet))
      (xmpp/presence-filter))
    ))


(defn push-xmpp-presence [connection presence-type presence-options]
  (let [presence-message (merge (or presence-options {}) {:type presence-type})]
    (run-pipeline (xmpp-presence-client connection)
      (fn [ch]
        (enqueue ch presence-message)))))


; xmpp-conversation takes a connection, and a target JID or conversation ID,
;                   returns a connection for sending and receiving IMs in a conversation
