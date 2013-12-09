(ns appengine-t.app_servlet
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use appengine-t.core)
  (:use [appengine-magic.servlet :only [make-servlet-service-method]]))

(defn -service [this request response]
  ((make-servlet-service-method appengine-t-app) this request response))
