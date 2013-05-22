(ns lamina-xmpp.xmpp-test
  (:use midje.sweet)
  (:require [lamina-xmpp.xmpp :refer :all]))


(fact "Opens a connection"
  (open-connection ..username.. ..password.. :host ..host.. :port ..port.. :proxy-info ..proxy..) => ..connection..
  (provided
    (build-configuration ..proxy.. ..host.. ..port..) => ..configuration..
    (build-connection ..configuration..) => ..connection..
    (connect ..connection..) => nil
    (login ..connection.. ..username.. ..password.. nil) => nil))


(fact "Hooks up packet listener"
  (packet-listener ..connection.. ..callback..) => ..proxy..
  (provided
    (#'lamina-xmpp.xmpp/packet-listener-proxy ..callback..) => ..proxy..
    (#'lamina-xmpp.xmpp/add-packet-listener ..connection.. ..proxy..) => nil))


(fact "Hooks up packet listener with filter"
  (packet-listener ..connection.. ..callback.. ..filter..) => ..proxy..
  (provided
    (#'lamina-xmpp.xmpp/packet-listener-proxy ..callback..) => ..proxy..
    (#'lamina-xmpp.xmpp/packet-filter-proxy ..filter..) => ..filter-proxy..
    (#'lamina-xmpp.xmpp/add-packet-listener ..connection.. ..proxy.. ..filter-proxy..) => nil))
