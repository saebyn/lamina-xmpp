(ns lamina-xmpp.xmpp-test
  (:use midje.sweet)
  (:import [org.jivesoftware.smack XMPPException])
  (:require [lamina-xmpp.xmpp :refer :all]
            [lamina-xmpp.xmpp.listeners :refer :all]))


(fact "Throws an exception if login fails"
  (open-connection ..username.. ..password.. :host ..host.. :port ..port.. :proxy-info ..proxy..) => (throws XMPPException)
  (provided
    (build-configuration ..proxy.. ..host.. ..port..) => ..configuration..
    (build-connection ..configuration..) => ..connection..
    (connect ..connection..) => nil
    (login ..connection.. ..username.. ..password.. nil) =throws=> (XMPPException.)))


(fact "Throws an exception if connect fails"
  (open-connection ..username.. ..password.. :host ..host.. :port ..port.. :proxy-info ..proxy..) => (throws XMPPException)
  (provided
    (build-configuration ..proxy.. ..host.. ..port..) => ..configuration..
    (build-connection ..configuration..) => ..connection..
    (connect ..connection..) =throws=> (XMPPException.)))


(fact "Opens a connection"
  (open-connection ..username.. ..password.. :host ..host.. :port ..port.. :proxy-info ..proxy..) => ..connection..
  (provided
    (build-configuration ..proxy.. ..host.. ..port..) => ..configuration..
    (build-connection ..configuration..) => ..connection..
    (connect ..connection..) => nil :times 1
    (login ..connection.. ..username.. ..password.. nil) => nil :times 1))


(fact "Hooks up packet listener"
  (packet-listener ..connection.. ..callback..) => ..proxy..
  (provided
    (#'lamina-xmpp.xmpp.listeners/packet-listener-proxy ..callback..) => ..proxy..
    (#'lamina-xmpp.xmpp.listeners/add-packet-listener ..connection.. ..proxy..) => nil))


(fact "Hooks up packet listener with filter"
  (packet-listener ..connection.. ..callback.. ..filter..) => ..proxy..
  (provided
    (#'lamina-xmpp.xmpp.listeners/packet-listener-proxy ..callback..) => ..proxy..
    (#'lamina-xmpp.xmpp.listeners/packet-filter-proxy ..filter..) => ..filter-proxy..
    (#'lamina-xmpp.xmpp.listeners/add-packet-listener ..connection.. ..proxy.. ..filter-proxy..) => nil))
