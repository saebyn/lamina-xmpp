(ns lamina-xmpp.core
  (:require [lamina-xmpp.xmpp :refer :all]))


; xmpp-client: returns a result channel that yields the connection
; xmpp-presence: takes a connection and returns a connection for sending and receiving presence data
; xmpp-conversation takes a connection, and a target JID or conversation ID,
;                   returns a connection for sending and receiving IMs in a conversation
