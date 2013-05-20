(ns lamina-xmpp.xmpp
  (:import [org.jivesoftware.smack XMPPConnection XMPPException ConnectionConfiguration]))


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


(defn build-connection
  [config]
  (XMPPConnection. config))


(defn connect [connection]
  (.connect connection))


(defn login
  [connection username password resource]
    (if resource
      (.login connection username password resource)
      (.login connection username password)))
