(ns appengine-t.core
  (:gen-class :extends javax.servlet.http.HttpServlet)
  (:use compojure.core
        [clojure.pprint :only [pprint]]
        hiccup.core
        hiccup.form
        [ring.middleware.params :only [wrap-params]])
  (:require [appengine-magic.core :as ae]
            [appengine-magic.services.datastore :as ds]
            [compojure.route :as route]))

(defn add []
  (form-to [:post "/new"]
           (text-field {:placeholder "Parent"} "parent-name")
           (text-field {:placeholder "Child"} "child-name")
           (submit-button "Confirm")
))


(ds/defentity Parent [^{:tag :key} name, children])
(ds/defentity Child [^{:tag :key} name])

(defroutes handler
  (GET "/add" []
       (html (add)))
  (GET "/hello/:name" [name]
       {:status 200
        :headers {"Content-Type" "text/plain"}
        :body (format "Hello, %s!" name)})
  
  (POST "/new" [parent-name child-name]  
  ;; (POST "/new/:parent-name/:child-name" [parent-name child-name]
        (let [parent (or (ds/retrieve Parent parent-name)
                                                  ;; Note the use of
                         ;; ds/save! here. Unless an entity has
                                                  ;; been saved to the
                         ;; datastore, children cannot join
                         ;; the entity group.
                         (ds/save! (Parent. parent-name [])))
                            ;; Note the use of ds/new* here: it is
              ;; required so that a :parent
                            ;; entity may be specified in the
              ;; instantiation of a child entity.
              child (ds/new* Child [child-name] :parent parent)]
                    ;; Updating the parent and the child together
          ;; occurs in a transaction.
          (ds/with-transaction
            (ds/save! (assoc parent
                        :members (conj (:children parent) child-name)))
            (ds/save! child))
          {:headers {"Content-Type" "text/plain"}
           :body "done"}))
  (GET  "/parents" []
        (let [parents (ds/query :kind Parent)]
          {:headers {"Content-Type" "text/plain"}
           :body (str (with-out-str (pprint parents))
                      "\n"
                      (with-out-str (pprint (map ds/get-key-object parents))))}))
  (GET  "/children" []
        (let [children (ds/query :kind Child)]
          {:headers {"Content-Type" "text/plain"}
           :body (str (with-out-str (pprint children))
                      "\n"
                      (with-out-str (pprint (map ds/get-key-object children))))}))
    (ANY  "*" [] {:status 404 :body "not found" :headers {"Content-Type" "text/plain"}}))

(def appengine-t-app
  (wrap-params handler))

(def app
  (wrap-params handler))

;; (ae/def-appengine-app
;;   appengine-t-app
;;   #'app)

;; (ae/def-appengine-app
;;   appengine-t-app
;;   (-> #'app
;;     wrap-params))

;; (ae/serve appengine-t-app) ;; start web server
;; (ae/stop) ;; stop web server
