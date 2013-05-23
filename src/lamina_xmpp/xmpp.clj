(ns lamina-xmpp.xmpp
  (:import [org.jivesoftware.smack
            XMPPConnection XMPPException ConnectionConfiguration
            PacketListener]
           [org.jivesoftware.smack.filter PacketFilter PacketTypeFilter]
           [org.jivesoftware.smack.packet Message]))


(defn message-filter []
  (PacketTypeFilter. Message))


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


(defn open-connection
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
