(ns advent.day-9
  (:require (clojure [set :as set] [string :as str]) :verbose
            [clojure.test :refer [is deftest testing]]
            [clojure.tools [trace :as trace]]))

(defn load-heightmap [file]
  (map seq (str/split (slurp file) #"\n")))

(defn example [] (load-heightmap "./src/advent/day_9_example.txt"))

(defn heightmap-map [heightmap-vec]
  (for [[y row] (map-indexed list heightmap-vec)
        [x cell] (map-indexed list row)]
    {[x y] (Character/digit cell 10)}))

(def example-map
  (into {} (heightmap-map
            (example))))


(defn out-of-bounds [x max-x]
  (or
   (> x max-x)
   (< x 0)))

; if out of bounds, return Infinity to ignore it when searching for min
(defn get-neighbour [x y max-x max-y heightmap]
  (if  
   (or (out-of-bounds x max-x) (out-of-bounds y max-y))
       ##Inf
       (heightmap [x y])))



;tests
(= (get-neighbour 10 -13 9 4 example-map) ##Inf)
(= (get-neighbour 9 0 9 4 example-map) 0)

(= (get-neighbour 6 100 9 4 example-map) ##Inf)
(= (get-neighbour 6 4 9 4 example-map) 5)

; note this has a bug that gets fixed later..
(defn is-min
  ([x y max-x max-y heightmap]
   (= (heightmap [x y])
      (let [neighbour #(get-neighbour %1 %2 max-x max-y heightmap)]
        (min (neighbour (dec x) y)
             (neighbour (inc x) y)
             (neighbour x (dec y))
             (neighbour x (inc y))
             (heightmap [x y]))))))


;; (defn is-min [x y max-x max-y current-minima])

;tests
(= true (is-min 1 0 9 4 example-map))
(= true (is-min 9 0 9 4 example-map))
(= true (is-min 2 2 9 4 example-map))
(= true (is-min 6 4 9 4 example-map))

(= false (is-min 4 4 9 4 example-map))
(= false (is-min 4 4 9 4 example-map))
(= false (is-min 3 2 9 4 example-map))
(= false (is-min 2 1 9 4 example-map))

(defn get-mins [heightmap max-x max-y]
  (filter
   #(is-min (first (first %)) (second (first %)) max-x max-y heightmap) heightmap))

; test
(= [[[2 2] 5] [[1 0] 1] [[9 0] 0] [[6 4] 5]] (get-mins example-map 9 4))


(defn sumrisk [heightmap max-x max-y]
  (reduce + (map (comp inc second) (get-mins heightmap max-x max-y))))

; test
(= 15 (sumrisk example-map (dec (count (first (example)))) (dec (count (example)))))

(defn example [] (load-heightmap "/Users/ben.ritchie/Documents/dev/advent-of-code-2021/src/advent/day_9_example.txt"))

;;; pt 1

(defn testdata [] (load-heightmap "./src/advent/day_9.txt"))

(def test-map
  (into {} (heightmap-map
            (testdata))))

(sumrisk test-map (dec (count (first (testdata)))) (dec (count (testdata))))
; 1557
;; number too high, fail! :/

;; realised Im checking that is the min vs neighbours but might be the same as a neighbour, should be _strictly_ min ie. no equal neighbour

; add condition that must be less than neighbours

(defn is-min
  ([x y max-x max-y heightmap]
   (every? (partial < (heightmap [x y]))
           (let [neighbour #(get-neighbour %1 %2 max-x max-y heightmap)]
             [(neighbour (dec x) y)
              (neighbour (inc x) y)
              (neighbour x (dec y))
              (neighbour x (inc y))]))))

(sumrisk test-map (dec (count (first (testdata)))) (dec (count (testdata))))

; => 417 woo hoo!

;pt 2


;; (defn get-basin
;;   [[x y] max-x max-y heightmap visited]
;;   (cond
;;     (contains? visited [x y]) #{}
;;     (= 9 (heightmap [x y])) #{}
;;     (> x max-x) #{}
;;     (> y max-y) #{}
;;     (< x 0) #{}
;;     (< y 0) #{}
;;     :else (set/union
;;             (get-basin [(inc x) y] max-x max-y heightmap (set/union visited #{[x y] [x (inc y)] [(dec x) y] [x (dec y)]}))
;;             (get-basin [x (inc y)] max-x max-y heightmap (set/union visited #{[x y] [(inc x) y] [(dec x) y] [x (dec y)]}))
;;             (get-basin [(dec x) y] max-x max-y heightmap (set/union visited #{[x y] [(inc x) y] [x (inc y)] [x (dec y)]}))
;;             (get-basin [x (dec y)] max-x max-y heightmap (set/union visited #{[x y] [(inc x) y] [x (inc y)] [(dec x) y]}))
;;             #{[x y]})))




(defn valid-pt [[x y] max-x max-y visited heightmap ]
  (cond
  (or (contains? visited [x y])
      (= 9 (heightmap [x y]))
      (> x max-x)
      (> y max-y)
      (< x 0)
      (< y 0)) #{}
  :else #{[x y]}))

(is (= (valid-pt [0 0] 9 4  #{[0 0]} example-map) #{}))
(is (= (valid-pt [0 0] 9 4  #{[0 1]} example-map) #{[0 0]}))
(is (= (valid-pt [100 0] 9 4  #{[0 0]} example-map) #{}))



(defn get-valid-neighbours [search max-x max-y visited heightmap]
  ;; (println search max-x max-y visited heightmap)
  (if-let [[x y] (first search)]
    (let [neighbours-new (set/union
                          (valid-pt [(inc x) y] max-x max-y visited heightmap)
                          (valid-pt [x (inc y)] max-x max-y visited heightmap)
                          (valid-pt [(dec x) y] max-x max-y visited heightmap)
                          (valid-pt [x (dec y)] max-x max-y visited heightmap))]
      
       (get-valid-neighbours 
        (set/union neighbours-new (set/difference search #{[x y]})) 
        max-x 
        max-y 
        (set/union visited #{[x y]}) 
        heightmap))
    visited))

(get-valid-neighbours #{[8 0]} 9 4  #{} example-map)


(defn basin-score [max-x max-y heightmap]
  (->> (get-mins heightmap max-x max-y)
       (map #(get-valid-neighbours #{(first %)} max-x max-y #{} heightmap))
       (map count)
       (sort >)
       (take 3)
       (reduce *)))

(is (= 1134 (basin-score 9 4 example-map)))
