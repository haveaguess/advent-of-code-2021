;; went off in the wrong direction and wasted time on this :
;;
;;=> {
;; lengths 6 and 5 could be three numbers each
;; 6 [[0 6] [6 6] [9 6]], so 0, 6, 9
;; 5 [[3 5] [2 5] [5 5]], so 3, 2, 5
;;
;; these numbers are done already
;; 3 [[7 3]], 2 [[1 2]], 4 [[4 4]],
;; 7 [[8 7]]
;; so 1 4 7 8
;;
;; mapped these to counts not signal :(
;;

;; ** went off on one trying to figure out a way to deduce things :( **

;; difference between 3 and 2 tells us e and f respectively
;; difference between 4 and 7 tells us d and a respectively
;; remaining unknown b, c, g
;; 7 -a,f (remove signals) = c
;; remaining unknown b, g
;; 2 -a,c,d,e = g
;; 4 -f,c,d = b
;; all known!
;;
;; start again:
;; easy numbers: 1 4 7 8
;;
;; difference between 1 and 7 tells us a
;; find matches for 4 and a which ever matches all with one left over is 9 which gives us g
;; known letters: a g
;; known numbers: 1 4 7 8 9
;; difference 9 8 tells us e
;; known letters: a g e
;; known numbers: 1 4 7 8 9
;; difference between 4+a+e and 8 is g
;; turn off age in lengths 5+6 and the common sequence of length 3 left tells you 5+6 respectively
;;

;; the extra one in
;; 4 -1 = d
;; known letters: a b d
;; known numbers: 1 4 7 8

;; lets look at the 6's: 0 6 9 each has one missing
;; 5's:  2 3 5
;; cagedb
;; cefabd
;; cdfgeb
;;
;; abcdeg
;; abcdef
;; bcdefg
;; which ever one
;;
;; dry run
;; acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf
;; 3                         7                 4           1
;;
;; }

