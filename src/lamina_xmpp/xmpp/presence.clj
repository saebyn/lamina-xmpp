(ns lamina-xmpp.xmpp.presence
  (:import [org.jivesoftware.smack.filter PacketFilter PacketTypeFilter]
           [org.jivesoftware.smack.packet Presence Presence$Mode Presence$Type]))


(defn- presence-type->keyword [#^Presence$Type presence-type]
  ({Presence$Type/available :available
    Presence$Type/unavailable :unavailable
    Presence$Type/subscribe :subscribe
    Presence$Type/subscribed :subscribed
    Presence$Type/unsubscribe :unsubscribe
    Presence$Type/unsubscribed :unsubscribed
    Presence$Type/error :error} presence-type))


(defn- keyword->presence-type [presence-type]
  (when presence-type
    ({:available Presence$Type/available   
      :unavailable Presence$Type/unavailable 
      :subscribe Presence$Type/subscribe   
      :subscribed Presence$Type/subscribed  
      :unsubscribe Presence$Type/unsubscribe 
      :unsubscribed Presence$Type/unsubscribed
      :error Presence$Type/error} presence-type)))


(defn- presence-mode->keyword [#^Presence$Mode presence-mode]
  ({Presence$Mode/available :available
    Presence$Mode/away :away
    Presence$Mode/chat :chat
    Presence$Mode/dnd :dnd
    Presence$Mode/xa :xa} presence-mode))


(defn- keyword->presence-mode [presence-mode]
  (when presence-mode
    ({:available Presence$Mode/available
      :away Presence$Mode/away
      :chat Presence$Mode/chat
      :dnd Presence$Mode/dnd
      :xa Presence$Mode/xa} presence-mode)))


(defn presence->map [#^Presence presence]
  {:type (presence-type->keyword (.getType presence))
   :status (.getStatus presence)
   :mode (presence-mode->keyword (.getMode presence))
   :priority (.getPriority presence)})


(defn map->presence [presence]
  (Presence.
    (or (keyword->presence-type (:type presence)) Presence$Type/available)
    (or (:status presence) "")
    (or (:priority presence) -128)
    (or (keyword->presence-mode (:mode presence)) Presence$Mode/available)))


(defn presence-filter []
  (PacketTypeFilter. Presence))
