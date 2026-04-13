(ns test
  (:require [clojure.core.async :as async]))

(def ch (async/chan))


;; POSITIVE CASES (should trigger finding)


;; 1. Simple blocking inside go
(async/go
  (async/<!! ch))

;; 2. Multiple blocking calls
(async/go
  (async/<!! ch)
  (async/>!! ch 42))

;; 3. Nested expression
(async/go
  (println (async/<!! ch)))

;; 4. Deeper nesting
(async/go
  (let [x 1]
    (when true
      (async/<!! ch))))

;;  NEGATIVE CASES (should NOT trigger)

;; 5. Blocking OUTSIDE go
(async/<!! ch)

;; 6. Non-blocking inside go (correct usage)
(async/go
  (async/<! ch))

;; 7. Different construct (thread is OK)
(async/thread
  (async/<!! ch))

;; 8. Future (also OK)
(future
  (async/<!! ch))

;; 9. go without blocking
(async/go
  (println "safe"))

;; 10. Shadowed symbol (should NOT match core.async)
(let [<!! (fn [_] :fake)]
  (async/go
    (<!! ch)))

