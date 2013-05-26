(ns lamina-xmpp.core-test
  (:use midje.sweet)
  (:import [org.jivesoftware.smack XMPPException])
  (:require [lamina.core :refer [run-pipeline enqueue error-value]])
  (:require [lamina-xmpp.core :refer :all]
            [lamina-xmpp.xmpp :as xmpp]))


(facts "about xmpp-client")


(fact "channel enters error state when chat fails to send"
      (let [ch @(xmpp-conversation ..client.. ..destination.. ..thread..)]
        (enqueue ch ..message..)
        (error-value ch nil)) => :xmpp-exception

      (provided
        (get-xmpp-connection ..client..) => ..connection..
        (xmpp/create-chat ..connection.. ..destination.. ..thread.. anything) => ..chat..
        (xmpp/send-message ..chat.. ..message..) =throws=> (XMPPException.)))


(facts "about xmpp-presence")
