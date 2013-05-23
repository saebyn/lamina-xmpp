(ns lamina-xmpp.xmpp.listeners
  (:import [org.jivesoftware.smack PacketListener]
           [org.jivesoftware.smack.filter PacketFilter]))


(defn- packet-listener-proxy
  [processor]
  (proxy [PacketListener] []
    (processPacket [packet]
      (processor packet))))


(defn- packet-filter-proxy
  [filter-accept]
  (if (fn? filter-accept)
    (proxy [PacketFilter] []
      (accept [packet]
        (filter-accept packet)))
    filter-accept))


(defn- add-packet-listener
  ([connection listener]
   (.addPacketListener connection listener))
  ([connection listener packet-filter]
   (.addPacketListener connection listener packet-filter)))


(defn packet-listener
  ([connection listener]
    (let [listener-proxy (packet-listener-proxy listener)]
      (add-packet-listener connection listener-proxy)
      listener-proxy))
  ([connection listener packet-filter]
    (let [listener-proxy (packet-listener-proxy listener)
          filter-proxy (packet-filter-proxy packet-filter)]
      (add-packet-listener connection listener-proxy filter-proxy)
      listener-proxy)))
