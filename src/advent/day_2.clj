(ns advent.day-2
  (:require (clojure [set :as set] [string :as str]) :verbose
            [clojure.test :refer [is deftest testing]]
            [clojure.tools [trace :as trace]]))

;; https://adventofcode.com/2021/day/1

(def input (slurp "./src/advent/day_2.in.txt"))

(def example 
"forward 5
down 5
forward 8
up 3
down 8
forward 2")


(defn instructions [input]
  (map
   (comp
    #(do [(first %) (read-string (second %))])
    #(str/split % #" "))
   (str/split input #"\n")))

(is (= [["forward" 5] ["down" 5] ["forward" 8] ["up" 3] ["down" 8] ["forward" 2]] (instructions example)))


(defn update-position [x y instruction amount]
  (cond
    (= "forward" instruction) [(+ x amount) y]
    (= "down" instruction) [x (+ y amount)]
    (= "up" instruction) [x (- y amount)]
  ))

(is (= [0 3] (update-position 0 0 "down" 3)))


(defn go [[x y] [[z1 z2] & zs]]
  (if (nil? z1)
    [x y]
    (go (update-position x y z1 z2) zs)))

(is (= [15 10] (go [0 0] (instructions example))))


(defn part1 [input]
  (let [[x y] (go [0 0] (instructions input))]
    (* x y)))


(is (= 150 (part1 example)))
(is (= 1451208 (part1 input)))



;; part2 

(defn update-position-with-aim [x y current-aim instruction amount]
  (println ">" x y current-aim)
  (cond
    (= "forward" instruction) [(+ x amount) (+ y (* current-aim amount)) current-aim]
    (= "down" instruction) [x y (+ current-aim amount)]
    (= "up" instruction) [x y (- current-aim amount)]
  ))

(is (= [13 40 5] (update-position-with-aim 5 0 5 "forward" 8)))


(defn go-with-aim [[x y aim] [[instruction amount] & instructions]]
  (println [x y aim])
  (if (nil? instruction)
    [x y aim]
    (go-with-aim 
     (update-position-with-aim x y aim instruction amount) instructions)))

(is (= [15 60 10] (go-with-aim [0 0 0] (instructions example))))



(defn part2 [input]
  (let [[x y] (go-with-aim [0 0 0] (instructions input))]
    (* x y)))

(is (= 900 (part2 example)))
(is (= 1620141160 (part2 input)))
