(ns lamina-xmpp.core-test
  (:use midje.sweet)
  (:require [lamina-xmpp.core :refer :all]
            [lamina-xmpp.xmpp :as xmpp])
  (:import [org.jivesoftware.smack XMPPConnection XMPPException]))


(fact "Opens a connection"
  (open-connection ..username.. ..password.. :host ..host.. :port ..port.. :proxy-info ..proxy..) => ..connection..
  (provided
    (xmpp/build-configuration ..proxy.. ..host.. ..port..) => ..configuration..
    (xmpp/build-connection ..configuration..) => ..connection..
    (xmpp/connect ..connection..) => nil
    (xmpp/login ..connection.. ..username.. ..password.. nil) => nil))
