(ns lamina-xmpp.xmpp.message
  (:import [org.jivesoftware.smack.filter PacketFilter PacketTypeFilter]
           [org.jivesoftware.smack.packet Message Message$Type Message$Body]))


(defn message-type->keyword [#^Message$Type message-type]
  ({Message$Type/chat :chat
    Message$Type/error :error
    Message$Type/groupchat :groupchat
    Message$Type/headline :headline
    Message$Type/normal :normal} message-type))


(defn keyword->message-type [message-type]
  ({:chat Message$Type/chat
    :error Message$Type/error
    :groupchat Message$Type/groupchat
    :headline Message$Type/headline
    :normal Message$Type/normal} message-type))


(defn message-bodies->map [#^Message message]
  (apply hash-map
         (mapcat
           (fn [#^Message$Body body]
             [(.getLanguage body) (.getMessage body)])
           (seq (.getBodies message)))))


(defn message->map [#^Message message]
  {:thread (.getThread message)
   :subject (.getSubject message)
   :type (message-type->keyword (.getType message))
   :body (message-bodies->map message)
   :default-body (.getBody message)})


(defn map->message [message]
  ; Pass through any string messages
  (if (string? message)
    message
    (let [xmpp-message (Message.)]
      (.setType xmpp-message
                (keyword->message-type
                  (or (:type message) :normal)))
      (when (:thread message)
        (.setThread xmpp-message (:thread message)))
      (when (:subject message)
        (.setSubject xmpp-message (:subject message)))

      (when (:default-body message)
        (.setBody xmpp-message (:default-body message)))

      (when (:body message)
        (dorun
          (map (fn [language body]
                 (.addBody xmpp-message language body))
               (:body message))))

      xmpp-message)))


(defn message-filter []
  (PacketTypeFilter. Message))
