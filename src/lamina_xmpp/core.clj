(ns lamina-xmpp.core
  (:require [lamina.executor :refer [task]])
  (:require [lamina.core :refer [channel-pair run-pipeline enqueue close
                                 receive success-result]])
  (:require [lamina-xmpp.xmpp :as xmpp]
            [lamina-xmpp.xmpp.presence :as xmpp-presence]
            [lamina-xmpp.xmpp.listeners :as xmpp-listeners]))


(defn get-xmpp-connection
  "Gets the XMPPConnection instance from the handle yielded by xmpp-client."
  [connection]
  connection)


(defn xmpp-client [& args]
  "Returns a task that yields the connection to the XMPP server."
  (task (apply xmpp/open-connection args)))


; xmpp-presence: takes a connection and returns a connection for sending and receiving presence data
;; TODO when the client channel is closed, remove the listener
;; TODO if the server disconnects or throws an error, propagate it to the channel
(defn xmpp-presence-client [connection]
  (let [[client server] (channel-pair)]
    ; Process messages from client on server channel, pass through to connection
    (receive server (fn [presence-message]
                      (xmpp/send-packet (get-xmpp-connection connection)
                                        (xmpp-presence/map->presence presence-message))))
    ; Listen for messages from connection, pass through to server channel
    (xmpp-listeners/packet-listener connection
      (fn [packet]
        (enqueue server (xmpp-presence/presence->map packet)))
      (xmpp-presence/presence-filter))
    (success-result client)))


(defn push-xmpp-presence
  ([connection presence-type presence-options]
   (let [presence-message (merge presence-options {:type presence-type})]
     (run-pipeline (xmpp-presence-client connection)
       (fn [ch]
         (enqueue ch presence-message)
         (close ch)))))
  ([connection presence-type]
   (push-xmpp-presence connection presence-type {})))


; xmpp-conversation takes a connection, and a target JID or conversation ID,
;                   returns a connection for sending and receiving IMs in a conversation
