(ns lamina-xmpp.core
  (:require [lamina.executor :refer [task]])
  (:require [lamina.core :refer [channel-pair run-pipeline enqueue close
                                 receive-all success-result]])
  (:require [lamina-xmpp.xmpp :as xmpp]
            [lamina-xmpp.xmpp.presence :as presence]
            [lamina-xmpp.xmpp.listeners :as listeners]))


(defn get-xmpp-connection
  "Gets the XMPPConnection instance from the handle yielded by xmpp-client."
  [client]
  client)


(defn xmpp-client [& args]
  "Returns a task that yields the client/connection to the XMPP server."
  (task (apply xmpp/open-connection args)))


; Takes a XMPP client and returns a client for sending and receiving presence data
(defn xmpp-presence [xmpp-client]
  (let [[client server] (channel-pair)]
    ; Process messages from client on server channel, pass through to client
    (receive-all server (fn [presence-message]
                      (xmpp/send-packet (get-xmpp-connection xmpp-client)
                                        (presence/map->presence presence-message))))
    ; Listen for messages from xmpp-client, pass through to server channel
    (listeners/packet-listener xmpp-client
                               (fn [packet]
                                 (enqueue server (presence/presence->map packet)))
                               (presence/presence-filter))
    (success-result client)))


; Takes a XMPP client, and a target JID or conversation ID,
; returns a client for sending and receiving IMs in a conversation.
(defn xmpp-conversation
  ([xmpp-client destination thread]
   (let [[client server] (channel-pair)
         ; Listen for messages from xmpp-client, pass through to server channel
         chat (xmpp/create-chat
                (get-xmpp-connection xmpp-client)
                destination
                thread
                (fn [chat message]
                  (enqueue server message)))]
     ; Process messages from client on server channel, pass through to xmpp-client
     (receive-all server (fn [message]
                       (xmpp/send-message chat message)))
     (success-result client)))
  ([xmpp-client destination]
   (xmpp-conversation xmpp-client destination nil)))


(defn push-xmpp-presence
  ([xmpp-client presence-type presence-options]
   (let [presence-message (merge presence-options {:type presence-type})]
     (run-pipeline (xmpp-presence xmpp-client)
                   (fn [ch]
                     (enqueue ch presence-message)
                     (close ch)))))
  ([xmpp-client presence-type]
   (push-xmpp-presence xmpp-client presence-type {})))
