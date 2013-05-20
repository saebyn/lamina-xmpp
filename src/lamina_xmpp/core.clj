(ns lamina-xmpp.core
  (:import [org.jivesoftware.smack XMPPException])
  (:require [lamina-xmpp.xmpp :refer :all]))


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
