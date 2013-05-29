(ns lamina-xmpp.xmpp
  (:require [lamina-xmpp.xmpp.listeners :refer [message-listener-proxy]])
  (:require [lamina-xmpp.xmpp.message :refer [map->message message->map]])
  (:require [lamina.trace :refer [defn-instrumented]])
  (:import [org.jivesoftware.smack
            XMPPConnection XMPPException ConnectionConfiguration]))


(defn build-configuration
  ([proxy-info host port service-name]
    (if proxy-info
      (ConnectionConfiguration. host port service-name proxy-info)
      (ConnectionConfiguration. host port service-name)))

  ([proxy-info host port]
    (if proxy-info
      (ConnectionConfiguration. host port proxy-info)
      (ConnectionConfiguration. host port)))

  ([proxy-info service-name]
    (if proxy-info
      (ConnectionConfiguration. service-name proxy-info)
      (ConnectionConfiguration. service-name))))


;; TODO add support for the callback that allows for client SSL certs
(defn build-connection
  [config]
  (XMPPConnection. config))


(defn-instrumented connect [connection]
  (.connect connection))


(defn-instrumented login
  [connection username password resource]
    (if resource
      (.login connection username password resource)
      (.login connection username password)))


(defn-instrumented open-connection
  [username password & {:keys [host port service-name proxy-info resource]}]
  (let [connection (->> [host port service-name]
                     (remove nil?)
                     (apply build-configuration proxy-info)
                     (build-connection))]
    (try
      (connect connection)
      (login connection username password resource)
      (catch XMPPException e
        (println "login threw exception")
        (throw e)))
    connection))


(defn-instrumented close-connection
  [connection]
  (.disconnect connection))


(defn-instrumented send-packet
  [connection packet]
  (.sendPacket connection packet))


(defn-instrumented send-message
  [chat message]
  (.sendMessage chat (map->message message)))


(defn-instrumented create-chat
  ([connection destination]
   (create-chat connection destination nil nil))
  ([connection destination listener]
   (create-chat connection destination nil listener))
  ([connection destination thread listener]
   (let [listener (message-listener-proxy (fn [chat message] (listener chat (message->map message))))]
     (if thread
       (.createChat (.getChatManager connection) destination thread listener)
       (.createChat (.getChatManager connection) destination listener)))))


(defn get-error [error]
  (condp instance? error
    XMPPException :xmpp-exception
    :unknown-error))
