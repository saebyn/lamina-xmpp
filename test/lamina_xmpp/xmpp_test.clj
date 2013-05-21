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
