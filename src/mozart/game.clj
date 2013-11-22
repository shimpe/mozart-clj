(ns mozart.game
  (:require
    [overtone.midi] 
    [overtone.at-at :as at-at]
    [overtone.studio.midi :as studio.midi]
    [overtone.music.pitch :as pitch]))

;;
;; "Mozart" Musical Dice Game
;;
;;  Select random bars from a pre-determined dataset.
;;
;; call mozart-game with a MIDI out device
;;
;; e.g. (mozart-game (midi-out "GS"))
;;
;; See http://en.wikipedia.org/wiki/Musikalisches_W%C3%BCrfelspiel
;; and http://explodingart.com/jmusic/jmtutorial/MozartDiceGame.html
;;

(def RHYTHMS
  { :QT 1/3
   :Q 1/2
   :CT 2/3
   :C 1
   :DC 3/2
   :M 2
   :SB 4})

(defn rhythm [k] (k RHYTHMS))

(def bar1
  [
   [[:F2 :F4] :C]
   [[:D2 :D4] :C]
   [[:G2 :G4] :C]
   ])

(def bar2
  [
   [[:B1 :G2 :A3] :C]
   [[:F#3] :Q]
   [[:G3] :Q]
   [[:B3] :Q]
   [[:G4] :Q]
   ])

(def bar3
  [
   [[:C2 :E2 :G4] :C]
   [[:C4] :C]
   [[:E4] :C]
   ])

(def bar4
  [
   [[:G1 :G4] :Q]
   [[:B1] :Q]
   [[:G2 :D4] :C]
   [[:B1] :C]
   ])

(def bar5
  [
   [[:G1 :G3 :B3 :D4 :G4] :C]
   [[:G2] :Q]
   [[:F2] :Q]
   [[:E2] :Q]
   [[:D2] :Q]
   ])

(def bar6
  [
   [[:C2 :E2 :G3] :C]
   [[:C4] :C]
   [[:E4] :C]
   ])

(def bar7
  [
   [[:C2 :G2 :E4] :Q]
   [[:C4] :Q]
   [[:E4] :Q]
   [[:G4] :Q]
   [[:C5] :Q]
   [[:G4] :Q]
   ])

(def bar8
  [
   [[:C2 :C4] :C]
   [[:G1] :C]
   [[:C1] :C]
   ])

(def bar9
  [
   [[:G2 :C4 :E4] :C]
   [[:B3 :D4] :C]
   [[:G1] :C]
   ])

(def bar10
  [
   [[:G2 :B3] :Q]
   [[:A3] :Q]
   [[:B3] :Q]
   [[:C4] :Q]
   [[:D4] :Q]
   [[:B3] :Q]
   ])

(def bar11
  [
   [[:C2 :E4] :Q]
   [[:C4] :Q]
   [[:D2 :B3] :Q]
   [[:A3] :Q]
   [[:D1 :G3] :Q]
   [[:F#3] :Q]
   ])

(def bar12
  [
   [[:C2 :E3 :C4] :C]
   [[:C2 :E3 :C4] :C]
   [[:C2 :E3 :C4] :C]
   ])

(def bar13
  [
   [[:E2 :G2 :C4] :C]
   [[:G3] :C]
   [[:E3] :C]
   ])

(def bar15
  [
   [[:C2 :G2 :E4] :C]
   [[:G4] :Q]
   [[:E4] :Q]
   [[:C2 :E2 :C4] :C]
   ])

(def bar16
  [
   [[:D2 :F#2 :A4] :C]
   [[:F#4] :C]
   [[:C2 :F#2 :D4] :C]
   ])

(def bar17
  [
   [[:E2 :G2 :C4] :Q]
   [[:G3] :Q]
   [[:C4] :Q]
   [[:E4] :Q]
   [[:G3] :Q]
   [[:C4] :Q]
   ])

(def bar18
  [
   [[:C2 :E2 :G3] :C]
   [[:C4] :C]
   [[:C2 :G2 :E4] :C]
   ])

(def bar19
  [
   [[:C2 :G2 :E4] :Q]
   [[:C4] :Q]
   [[:E4] :C]
   [[:C2 :E2 :G4] :C]
   ])

(def bar20
  [
   [[:B1 :G4] :C]
   [[:B4] :Q]
   [[:D5] :Q]
   [[:D4] :C]
   ])

(def bar21
  [
   [[:C2 :C4] :Q]
   [[:E4] :Q]
   [[:D2 :G4] :Q]
   [[:D4] :Q]
   [[:D1 :A3] :Q]
   [[:F#4] :Q]
   ])

(def bar22
  [
   [[:C2 :E4] :C]
   [[:C4] :C]
   [[:G3] :C]
   ])

(def bar23
  [
   [[:F2 :F4] :Q]
   [[:E2 :E4] :Q]
   [[:D2 :D4] :Q]
   [[:E2 :E4] :Q]
   [[:F2 :F4] :Q]
   [[:G2 :G4] :Q]
   ])

(def bar25
  [
   [[:D2 :D3] :Q]
   [[:F#3] :Q]
   [[:A3] :Q]
   [[:D4] :Q]
   [[:C2 :F#4] :Q]
   [[:A4] :Q]
   ])

(def bar26
  [
   [[:C2 :C4 :E4] :Q]
   [[:E2] :Q]
   [[:G2 :C4 :E4] :Q]
   [[:E2] :Q]
   [[:C3 :C4 :E4] :Q]
   [[:C2] :Q]
   ])

(def bar27
  [
   [[:G2 :B2 :F4] :Q]
   [[:E4] :Q]
   [[:F4] :Q]
   [[:D4] :Q]
   [[:C4] :Q]
   [[:B3] :Q]
   ])

(def bar28
  [
   [[:C2 :A2 :F#4] :Q]
   [[:D4] :Q]
   [[:A3] :Q]
   [[:A4] :Q]
   [[:F#4] :Q]
   [[:D4] :Q]
   ])

(def bar29
  [
   [[:G2 :B3] :Q]
   [[:D4] :Q]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:G1 :B3] :C]
   ])

(def bar31
  [
   [[:C2 :G2 :E4] :Q]
   [[:C4] :Q]
   [[:G3] :Q]
   [[:C2 :G2 :E4] :Q]
   ])

(def bar32
  [
   [[:C2 :E2 :G3] :C]
   [[:C4] :C]
   [[:E4] :C]
   ])

(def bar34
  [
   [[:G2 :E4] :Q]
   [[:C4] :Q]
   [[:D4] :Q]
   [[:B3] :Q]
   [[:G3] :C]
   ])

(def bar35
  [
   [[:D2 :F#2 :A3] :C]
   [[:D4] :C]
   [[:C2 :A2 :F#4] :C]
   ])

(def bar36
  [
   [[:C2 :A3] :Q]
   [[:E4] :Q]
   [[:D2 :D4] :Q]
   [[:G4] :Q]
   [[:D1 :F#4] :Q]
   [[:A4] :Q]
   ])

(def bar37
  [
   [[:B1 :D2 :G4] :Q]
   [[:B4] :Q]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:B3] :C]
   ])

(def bar38
  [
   [[:C2 :E2 :C4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :G3] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :E4] :Q]
   [[:G2] :Q]
   ])

(def bar39
  [
   [[:B1 :G4] :Q]
   [[:D2] :Q]
   [[:G2 :G3] :Q]
   [[:D2] :Q]
   [[:B1 :G3] :Q]
   [[:G1] :Q]
   ])

(def bar40
  [
   [[:C2 :E2 :C4] :Q]
   [[:B3] :Q]
   [[:C4] :Q]
   [[:E4] :Q]
   [[:G3] :Q]
   [[:C4] :Q]
   ])

(def bar41
  [
   [[:C2 :E2 :C4] :Q]
   [[:B3] :Q]
   [[:C4] :Q]
   [[:E4] :Q]
   [[:G3] :C]
   ])

(def bar42
  [
   [[:G1 :B3] :Q]
   [[:C4] :Q]
   [[:D4] :Q]
   [[:B3] :Q]
   [[:A3] :Q]
   [[:G3] :Q]
   ])

(def bar43
  [
   [[:C2 :E2 :G4] :C]
   [[:F4] :Q]
   [[:E4] :Q]
   [[:D4] :Q]
   [[:C4] :Q]
   ])

(def bar44
  [
   [[:F2 :A3] :C]
   [[:F4] :Q]
   [[:D4] :Q]
   [[:G2 :A3] :Q]
   [[:B3] :Q]
   ])

(def bar45
  [
   [[:E2 :G2 :C4] :Q]
   [[:B3] :Q]
   [[:C4] :Q]
   [[:G3] :Q]
   [[:E3] :Q]
   [[:C3] :Q]
   ])

(def bar46
  [
   [[:B1 :D2 :G4] :C]
   [[:B4] :Q]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:B3] :Q]
   ])

(def bar47
  [
   [[:B1 :D2 :G4] :C]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:B4] :C]
   ])

(def bar48
  [
   [[:C2 :G2 :E4] :C]
   [[:C4] :Q]
   [[:E4] :Q]
   [[:C2 :E2 :G4] :Q]
   [[:C5] :Q]
   ])

(def bar49
  [
   [[:C2 :E2 :E4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :C4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :G3] :Q]
   [[:G2] :Q]
   ])

(def bar50
  [
   [[:E2 :G2 :C4] :C]
   [[:E4] :Q]
   [[:C4] :Q]
   [[:G3] :C]
   ])

(def bar51
  [
   [[:C2 :E2 :C4] :Q]
   [[:G3] :Q]
   [[:E4] :Q]
   [[:C4] :Q]
   [[:G4] :Q]
   [[:E4] :Q]
   ])

(def bar52
  [
   [[:F2 :D4] :Q]
   [[:C#4] :Q]
   [[:D4] :Q]
   [[:F4] :Q]
   [[:G2 :G3] :Q]
   [[:B3] :Q]
   ])

(def bar53
  [
   [[:C2 :C4 :E4] :C]
   [[:C4 :E4] :Q]
   [[:D4 :F4] :Q]
   [[:E4 :G4] :C]
   ])

(def bar54
  [
   [[:C2 :E3 :C4] :C]
   [[:C2 :E3 :C4] :C]
   [[:C2 :E3 :C4] :C]
   ])

(def bar55
  [
   [[:B1 :D2 :G4] :C]
   [[:B4] :C]
   [[:D4] :C]
   ])

(def bar56
  [
   [[:G1 :G2 :D4] :Q]
   [[:B3] :Q]
   [[:G3] :C]
   [[:G2] :C]
   ])

(def bar58
  [
   [[:C2 :E2 :G4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :E4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :C4] :Q]
   [[:G2] :Q]
   ])

(def bar59
  [
   [[:C2 :E2 :G4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :C4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :E4] :Q]
   [[:G2] :Q]
   ])

(def bar60
  [
   [[:C2 :E2 :G4] :C]
   [[:F4] :Q]
   [[:E4] :Q]
   [[:D4] :Q]
   [[:C4] :Q]
   ])

(def bar61
  [
   [[:E2 :G2 :C4] :C]
   [[:E4] :Q]
   [[:C4] :Q]
   [[:G4] :C]
   ])

(def bar62
  [
   [[:C2 :E4] :Q]
   [[:C4] :Q]
   [[:D2 :B3] :Q]
   [[:G3] :Q]
   [[:D1 :A3] :Q]
   [[:F#3] :Q]
   ])

(def bar63
  [
   [[:C2 :E4] :Q]
   [[:C4] :Q]
   [[:B3] :Q]
   [[:C4] :Q]
   [[:G3] :C]
   ])

(def bar64
  [
   [[:C2 :G2 :E4] :Q]
   [[:G4] :Q]
   [[:C4] :Q]
   [[:G4] :Q]
   [[:C2 :G2 :E4] :Q]
   [[:C4] :Q]
   ])

(def bar65
  [
   [[:D2 :F#2 :D4] :Q]
   [[:A3] :Q]
   [[:D4] :C]
   [[:F#4] :C]
   ])

(def bar66
  [
   [[:D2 :A2 :F#4] :C]
   [[:D2 :F#2 :A4] :C]
   [[:C2 :D2 :F#4] :C]
   ])

(def bar67
  [
   [[:C2 :E2 :C4] :Q]
   [[:B3] :Q]
   [[:C4] :Q]
   [[:E4] :Q]
   [[:E2 :G2 :G3] :Q]
   [[:C4] :Q]
   ])

(def bar68
  [
   [[:B1 :G4] :C]
   [[:B4] :Q]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:G4] :Q]
   ])

(def bar69
  [
   [[:C2 :E2 :G4] :C]
   [[:E4] :C]
   [[:C4] :C]
   ])

(def bar70
  [
   [[:D2 :F#4] :C]
   [[:A4] :Q]
   [[:F#4] :Q]
   [[:C2 :D4] :Q]
   [[:F#4] :Q]
   ])

(def bar71
  [
   [[:B1 :D2 :G4] :Q]
   [[:B4] :Q]
   [[:D5] :Q]
   [[:B4] :Q]
   [[:B1 :D2 :G4] :C]
   ])

(def bar72
  [
   [[:F2 :F4] :Q]
   [[:E4] :Q]
   [[:D4] :Q]
   [[:C4] :Q]
   [[:G2 :B3] :Q]
   [[:D4] :Q]
   ])

(def bar73
  [
   [[:C2 :E2 :G4] :Q]
   [[:G4] :Q]
   [[:C2 :E2 :E4] :Q]
   [[:G4] :Q]
   [[:C2 :E2 :C4] :Q]
   [[:G4] :Q]
   ])

(def bar74
  [
   [[:C2 :E2 :C5] :Q]
   [[:B4] :Q]
   [[:C5] :Q]
   [[:G4] :Q]
   [[:E4] :Q]
   [[:C4] :Q]
   ])

(def bar75
  [
   [[:C2 :D4 :F#4] :C]
   [[:C2 :D4 :F#4] :C]
   [[:C2 :D4 :F#4] :C]
   ])

(def bar76
  [
   [[:C2 :E2 :C4] :Q]
   [[:B3] :Q]
   [[:C4] :Q]
   [[:G4] :Q]
   [[:C2 :G2 :E4] :Q]
   [[:C4] :Q]
   ])

(def bar77
  [
   [[:B1 :D2 :G4] :Q]
   [[:B4] :Q]
   [[:G4] :C]
   [[:B1 :G2 :D4] :C]
   ])

(def bar78
  [
   [[:C2 :C4] :C]
   [[:C3] :C]
   [[:C1] :C]
   ])

(def bar80
  [
   [[:C2 :D4] :C]
   [[:A3] :C]
   [[:F#4] :C]
   ])

(def bar82
  [
   [[:B1 :G2 :D4] :Q]
   [[:B3] :Q]
   [[:G3] :C]
   [[:B1 :D2 :G4] :C]
   ])

(def bar84
  [
   [[:C2 :E2 :C4] :Q]
   [[:G3] :Q]
   [[:E4] :Q]
   [[:C4] :Q]
   [[:G4] :Q]
   [[:E4] :Q]
   ])

(def bar85
  [
   [[:E2 :G2 :C4] :Q]
   [[:E4] :Q]
   [[:G3] :Q]
   ])

(def bar86
  [
   [[:B1 :G2 :D4] :C]
   [[:D4] :Q]
   [[:G4] :Q]
   [[:B4] :C]
   ])

(def bar87
  [
   [[:C2 :E2 :G4] :C]
   [[:C4] :C]
   [[:C2 :G2 :E4] :C]
   ])

(def bar88
  [
   [[:B1 :D2 :G4] :Q]
   [[:D4] :Q]
   [[:G4] :Q]
   [[:B4] :Q]
   [[:B1 :D2 :G4] :Q]
   [[:D4] :Q]
   ])

(def bar89
  [
   [[:F2 :F4] :Q]
   [[:E2 :E4] :Q]
   [[:D2 :D4] :C]
   [[:G2 :G4] :C]
   ])

(def bar90
  [
   [[:C2 :A2 :F#4] :Q]
   [[:A4] :Q]
   [[:D5] :Q]
   [[:A4] :Q]
   [[:C2 :A2 :F#4] :Q]
   [[:A4] :Q]
   ])

(def bar92
  [
   [[:G1 :G2 :B3 :D4] :C]
   [[:G4] :Q]
   [[:B4] :Q]
   [[:G2 :D4] :C]
   ])

(def bar95
  [
   [[:C2 :E2 :G4] :C]
   [[:E4] :C]
   [[:C4] :C]
   ])

(def bar96
  [
   [[:C2 :E4] :C]
   [[:C4] :C]
   [[:G3] :C]
   ])

(def bar97
  [
   [[:B1 :D2 :G4] :Q]
   [[:F#4] :Q]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:B1 :G2 :B3] :Q]
   [[:G3] :Q]
   ])

(def bar98
  [
   [[:C2 :E2 :C4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :G3] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :E4] :Q]
   [[:G2] :Q]
   ])

(def bar99
  [
   [[:C2 :A2 :F#4] :C]
   [[:A4] :C]
   [[:C2 :A2 :D4] :C]
   ])

(def bar101
  [
   [[:C2 :G2 :E4] :Q]
   [[:D4] :Q]
   [[:E4] :Q]
   [[:G4] :Q]
   [[:C2 :E2 :C5] :Q]
   [[:G4] :Q]
   ])

(def bar102
  [
   [[:C2 :A2 :F#4] :Q]
   [[:D4] :Q]
   [[:A3] :C]
   [[:C2 :A2 :F#4] :C]
   ])

(def bar103
  [
   [[:E2 :G2 :C4] :Q]
   [[:E4] :Q]
   [[:C4] :Q]
   [[:G3] :Q]
   [[:E3] :C]
   ])

(def bar104
  [
   [[:C2 :E4] :Q]
   [[:D4] :Q]
   [[:E4] :Q]
   [[:G4] :Q]
   [[:C5] :Q]
   [[:G4] :Q]
   ])

(def bar105
  [
   [[:C2 :F#4] :C]
   [[:A4] :Q]
   [[:F#4] :Q]
   [[:D4] :Q]
   [[:F#4] :Q]
   ])

(def bar106
  [
   [[:C2 :A3] :C]
   [[:D2 :D4] :Q]
   [[:C4] :Q]
   [[:D1 :B3] :Q]
   [[:A3] :Q]
   ])

(def bar108
  [
   [[:C2 :G2 :E4] :Q]
   [[:G4] :Q]
   [[:C2 :E2 :C5] :Q]
   ])

(def bar109
  [
   [[:F2 :A2 :D4] :Q]
   [[:F4] :Q]
   [[:D4] :Q]
   [[:F4] :Q]
   [[:G2 :D3 :B3] :Q]
   [[:D4] :Q]
   ])

(def bar110
  [
   [[:C2 :B3 :D4] :Q]
   [[:A3 :C4] :Q]
   [[:D2 :A3 :C4] :Q]
   [[:G3 :B3] :Q]
   [[:D1 :G3 :B3] :Q]
   [[:F#3 :A3] :Q]
   ])

(def bar113
  [
   [[:G2 :B2 :F4] :C]
   [[:D4] :C]
   [[:B3] :C]
   ])

(def bar114
  [
   [[:G2 :B3 :D4] :C]
   [[:G2 :B3 :D4] :C]
   [[:G2 :B3 :D4] :C]
   ])

(def bar115
  [
   [[:C2 :E2 :C4] :Q]
   [[:G3] :Q]
   [[:E4] :Q]
   [[:C4] :Q]
   [[:G4] :Q]
   [[:E4] :Q]
   ])

(def bar116
  [
   [[:F2 :D4] :Q]
   [[:F4] :Q]
   [[:A4] :Q]
   [[:F4] :Q]
   [[:G2 :D4] :Q]
   [[:B3] :Q]
   ])

(def bar117
  [
   [[:D2 :F#2 :D4] :Q]
   [[:A3] :Q]
   [[:D4] :Q]
   [[:F#4] :Q]
   [[:A4] :Q]
   [[:F#4] :Q]
   ])

(def bar118
  [
   [[:A4] :Q]
   [[:D2 :G4] :Q]
   [[:B4] :Q]
   [[:D1 :F#4] :Q]
   [[:A4] :Q]
   ])

(def bar119
  [
   [[:C2 :E2 :E4] :Q]
   [[:C4] :Q]
   [[:G4] :Q]
   [[:E4] :Q]
   [[:C5] :Q]
   [[:g5] :Q]
   ])

(def bar120
  [
   [[:D2 :F#2 :D5] :C]
   [[:A4] :Q]
   [[:F#4] :Q]
   [[:C2 :F#2 :D4] :Q]
   [[:A3] :Q]
   ])

(def bar121
  [
   [[:B1 :G2 :G4] :C]
   [[:B4] :Q]
   [[:G4] :Q]
   [[:D4] :C]
   ])

(def bar122
  [
   [[:B1 :D2 :G4] :Q]
   [[:F#4] :Q]
   [[:B1 :D2 :G4] :Q]
   [[:B4] :Q]
   [[:B1 :D2 :D4] :C]
   ])

(def bar125
  [
   [[:G2 :G4] :Q]
   [[:E4] :Q]
   [[:G1 :D4] :Q]
   [[:B3] :Q]
   [[:G3] :C]
   ])

(def bar126
  [
   [[:E2 :C4] :Q]
   [[:G3] :Q]
   [[:C4] :Q]
   [[:E4] :Q]
   [[:E2 :G4] :Q]
   [[:C2 :C4 :E4] :Q]
   ])

(def bar128
  [
   [[:G1 :B3] :C]
   [[:D4] :C]
   [[:G4] :C]
   ])

(def bar129
  [
   [[:B1 :D2 :A4] :Q]
   [[:G4] :Q]
   [[:B1 :D2 :F#4] :Q]
   [[:G4] :Q]
   [[:B1 :G2 :D4] :C]
   ])

(def bar130
  [
   [[:C2 :E3 :C4] :C]
   [[:C2 :E3 :C4] :C]
   [[:C2 :E3 :C4] :C]
   ])

(def bar132
  [
   [[:G2 :C4 :E4] :C]
   [[:G1 :B3 :D4] :Q]
   [[:G3 :B3] :Q]
   [[:G3] :C]
   ])

(def bar133
  [
   [[:B1 :G2 :D4] :C]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:B3] :Q]
   [[:D4] :Q]
   ])

(def bar134
  [
   [[:C2 :A3] :Q]
   [[:E4] :Q]
   [[:D2 :B3 :D4] :Q]
   [[:A3 :C4] :Q]
   [[:D1 :G3 :B3] :Q]
   [[:F#3 :A3] :Q]
   ])

(def bar135
  [
   [[:C2 :D2 :F#4] :C]
   [[:C2 :D2 :F#4] :Q]
   [[:D4] :Q]
   [[:C2 :D2 :A4] :C]
   ])

(def bar136
  [
   [[:C2 :E2 :C5] :Q]
   [[:B4] :Q]
   [[:C5] :Q]
   [[:G4] :Q]
   [[:E4] :Q]
   [[:C4] :Q]
   ])

(def bar137
  [
   [[:C2 :E2 :C4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :G3] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :E4] :Q]
   [[:G2] :Q]
   ])

(def bar138
  [
   [[:D1 :A3 :D4 :F#4] :Q]
   [[:D2] :Q]
   [[:C#2 :F#4] :Q]
   [[:D2] :Q]
   [[:C2] :Q]
   [[:D2] :Q]
   ])

(def bar139
  [
   [[:B1 :G4] :Q]
   [[:B4] :Q]
   [[:G4] :Q]
   [[:B4] :Q]
   [[:D4] :C]
   ])

(def bar140
  [
   [[:C2 :F#2 :A3] :C]
   [[:C2 :F#2 :A3] :Q]
   [[:D4] :Q]
   [[:C2 :A2 :F#4] :C]
   ])

(def bar141
  [
   [[:B1 :G2 :D4] :Q]
   [[:E4] :Q]
   [[:F4] :Q]
   [[:D4] :Q]
   [[:G1 :C4] :Q]
   [[:B3] :Q]
   ])

(def bar142
  [
   [[:C2 :E2 :C4] :C]
   [[:G3] :C]
   [[:E4] :C]
   ])

(def bar143
  [
   [[:D4] :Q]
   [[:B3] :Q]
   [[:B1 :D2 :G3] :C]
   ])

(def bar144
  [
   [[:C2 :E2 :G4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :C4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :E4] :Q]
   [[:G2] :Q]
   ])

(def bar145
  [
   [[:F2 :D4] :Q]
   [[:F4] :Q]
   [[:A3] :Q]
   [[:D4] :Q]
   [[:G2 :B3] :Q]
   [[:D4] :Q]
   ])

(def bar146
  [
   [[:C2 :F#3 :D4] :C]
   [[:C2 :D4 :F#4] :C]
   [[:C2 :F#4 :A4] :C]
   ])

(def bar147
  [
   [[:C2 :E4] :Q]
   [[:C5] :Q]
   [[:D2 :B4] :Q]
   [[:G4] :Q]
   [[:D1 :A4] :Q]
   [[:F#4] :Q]
   ])

(def bar148
  [
   [[:C2 :E2 :C5] :Q]
   [[:B4] :Q]
   [[:C5] :Q]
   [[:G4] :Q]
   [[:E4] :Q]
   [[:C4] :Q]
   ])

(def bar149
  [
   [[:F2 :F4] :Q]
   [[:D4] :Q]
   [[:A3] :Q]
   [[:G2 :B3] :Q]
   ])

(def bar150
  [
   [[:C2 :G3 :C4 :E4] :Q]
   [[:B1] :Q]
   [[:C2 :E4] :Q]
   [[:D2] :Q]
   [[:E2] :Q]
   [[:F#2] :Q]
   ])

(def bar152
  [
   [[:C2 :E2 :G4] :C]
   [[:F4] :Q]
   [[:E4] :Q]
   [[:D4] :Q]
   [[:C4] :Q]
   ])

(def bar154
  [
   [[:C2 :D4] :Q]
   [[:C#4] :Q]
   [[:D4] :Q]
   [[:F#4] :Q]
   [[:A4] :Q]
   [[:F#4] :Q]
   ])

(def bar155
  [
   [[:B1 :D2 :G4] :Q]
   [[:B4] :Q]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:B3] :Q]
   [[:G3] :Q]
   ])

(def bar156
  [
   [[:E2 :G2 :C4] :Q]
   [[:G3] :Q]
   [[:E4] :Q]
   [[:C4] :Q]
   [[:G4] :C]
   ])

(def bar157
  [
   [[:C2 :E4] :Q]
   [[:D4] :Q]
   [[:E4] :Q]
   [[:G4] :Q]
   [[:C5] :Q]
   [[:G4] :Q]
   ])

(def bar158
  [
   [[:G1 :B3] :C]
   [[:D4] :Q]
   [[:B3] :Q]
   [[:A3] :Q]
   [[:G3] :Q]
   ])

(def bar159
  [
   [[:C2 :E4] :Q]
   [[:G4] :Q]
   [[:D2 :D4] :Q]
   [[:C4] :Q]
   [[:D1 :B3] :Q]
   [[:A3] :Q]
   ])

(def bar160
  [
   [[:C2 :E2 :C4] :Q]
   [[:B3] :Q]
   [[:C4] :Q]
   [[:E4] :Q]
   [[:C2 :E2 :G3] :Q]
   [[:C4] :Q]
   ])

(def bar161
  [
   [[:C2 :F#3 :D4] :C]
   [[:C2 :F#3 :D4] :C]
   [[:C2 :F#3 :D4] :C]
   ])

(def bar162
  [
   [[:C2 :G2 :E4] :Q]
   [[:D4] :Q]
   [[:E4] :Q]
   [[:G4] :Q]
   [[:C2 :E2 :C5] :Q]
   [[:G4] :Q]
   ])

(def bar163
  [
   [[:B1 :D2 :G4] :Q]
   [[:F#4] :Q]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:B3] :Q]
   [[:G3] :Q]
   ])

(def bar164
  [
   [[:G2 :D4] :Q]
   [[:F#2] :Q]
   [[:G2 :G3] :Q]
   [[:D2] :Q]
   [[:B1] :Q]
   [[:G1] :Q]
   ])

(def bar165
  [
   [[:B1 :D4] :C]
   [[:B3] :C]
   [[:G3] :C]
   ])

(def bar166
  [
   [[:G2 :B2 :D4] :Q]
   [[:B4] :Q]
   [[:G4] :Q]
   [[:D4] :Q]
   [[:B3] :C]
   ])

(def bar167
  [
   [[:C2 :E2 :C4] :C]
   [[:C4] :Q]
   [[:D4] :Q]
   [[:E4] :C]
   ])

(def bar168
  [
   [[:C2 :E2 :G4] :C]
   [[:F4] :Q]
   [[:E4] :Q]
   [[:E2 :G2 :D4] :Q]
   [[:C4] :Q]
   ])

(def bar169
  [
   [[:C2 :E4] :Q]
   [[:G4] :Q]
   [[:D2 :D4] :Q]
   [[:G4] :Q]
   [[:D1 :A3] :Q]
   [[:F#4] :Q]
   ])

(def bar171
  [
   [[:G1 :G2 :B3] :Q]
   [[:C4] :Q]
   [[:D4] :Q]
   [[:E4] :Q]
   [[:B1 :G2 :F4] :Q]
   [[:E4] :Q]
   ])

(def bar173
  [
   [[:F2 :F4] :Q]
   [[:A4] :Q]
   [[:A3] :C]
   [[:G2 :B3] :Q]
   [[:D4] :Q]
   ])

(def bar174
  [
   [[:C2 :E2 :G3] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :C4] :Q]
   [[:G2] :Q]
   [[:C2 :E2 :E4] :Q]
   [[:G2] :Q]
   ])

(def bar175
  [
   [[:G2 :E4] :Q]
   [[:C4] :Q]
   [[:G1 :B3] :Q]
   [[:D4] :Q]
   [[:G4] :C]
   ])

(def bar176
  [
   [[:B1 :D2 :A4] :Q]
   [[:G4] :Q]
   [[:B4] :Q]
   [[:G4] :Q]
   [[:B1 :D2 :D4] :Q]
   [[:G4] :Q]
   ])

(def bar24 bar5)
(def bar30 bar5)
(def bar33 bar5)
(def bar81 bar5)
(def bar91 bar5)
(def bar94 bar5)
(def bar100 bar5)
(def bar107 bar5)
(def bar123 bar5)
(def bar127 bar5)

(def bar57 bar49)
(def bar112 bar49)

(def bar124 bar12)

(def bar14 bar8)
(def bar79 bar8)
(def bar83 bar8)
(def bar93 bar8)
(def bar111 bar8)
(def bar131 bar8)
(def bar151 bar8)
(def bar170 bar8)
(def bar172 bar8)

(def waltz1
  {
   [0 0] bar96
   [0 1] bar32
   [0 2] bar69
   [0 3] bar40
   [0 4] bar148
   [0 5] bar104
   [0 6] bar152
   [0 7] bar119
   [0 8] bar98
   [0 9] bar3
   [0 10] bar54
   [1 0] bar22
   [1 1] bar6
   [1 2] bar95
   [1 3] bar17
   [1 4] bar74
   [1 5] bar157
   [1 6] bar60
   [1 7] bar84
   [1 8] bar142
   [1 9] bar87
   [1 10] bar130
   [2 0] bar141
   [2 1] bar128
   [2 2] bar158
   [2 3] bar113
   [2 4] bar163
   [2 5] bar27
   [2 6] bar171
   [2 7] bar114
   [2 8] bar42
   [2 9] bar165
   [2 10] bar10
   [3 0] bar41
   [3 1] bar63
   [3 2] bar13
   [3 3] bar85
   [3 4] bar45
   [3 5] bar167
   [3 6] bar53
   [3 7] bar50
   [3 8] bar156
   [3 9] bar61
   [3 10] bar103
   [4 0] bar105
   [4 1] bar146
   [4 2] bar158
   [4 3] bar161
   [4 4] bar80
   [4 5] bar154
   [4 6] bar99
   [4 7] bar140
   [4 8] bar75
   [4 9] bar135
   [4 10] bar28
   [5 0] bar122
   [5 1] bar46
   [5 2] bar55
   [5 3] bar2
   [5 4] bar97
   [5 5] bar68
   [5 6] bar133
   [5 7] bar86
   [5 8] bar129
   [5 9] bar47
   [5 10] bar37
   [6 0] bar11
   [6 1] bar134
   [6 2] bar110
   [6 3] bar159
   [6 4] bar36
   [6 5] bar118
   [6 6] bar21
   [6 7] bar169
   [6 8] bar62
   [6 9] bar147
   [6 10] bar106
   [7 0] bar30
   [7 1] bar81
   [7 2] bar24
   [7 3] bar100
   [7 4] bar107
   [7 5] bar91
   [7 6] bar127
   [7 7] bar94
   [7 8] bar123
   [7 9] bar33
   [7 10] bar5})

(def waltz2
  {
   [0 0] bar70
   [0 1] bar117
   [0 2] bar66
   [0 3] bar90
   [0 4] bar25
   [0 5] bar138
   [0 6] bar16
   [0 7] bar120
   [0 8] bar65
   [0 9] bar102
   [0 10] bar35
   [1 0] bar121
   [1 1] bar39
   [1 2] bar139
   [1 3] bar17
   [1 4] bar74
   [1 5] bar157
   [1 6] bar60
   [1 7] bar84
   [1 8] bar142
   [1 9] bar87
   [1 10] bar130
   [2 0] bar26
   [2 1] bar126
   [2 2] bar15
   [2 3] bar7
   [2 4] bar64
   [2 5] bar150
   [2 6] bar57
   [2 7] bar48
   [2 8] bar19
   [2 9] bar31
   [2 10] bar108
   [3 0] bar9
   [3 1] bar56
   [3 2] bar132
   [3 3] bar34
   [3 4] bar125
   [3 5] bar29
   [3 6] bar175
   [3 7] bar166
   [3 8] bar82
   [3 9] bar164
   [3 10] bar92
   [4 0] bar112
   [4 1] bar174
   [4 2] bar73
   [4 3] bar67
   [4 4] bar76
   [4 5] bar101
   [4 6] bar43
   [4 7] bar51
   [4 8] bar137
   [4 9] bar144
   [4 10] bar12
   [5 0] bar49
   [5 1] bar18
   [5 2] bar58
   [5 3] bar160
   [5 4] bar136
   [5 5] bar162
   [5 6] bar168
   [5 7] bar115
   [5 8] bar38
   [5 9] bar59
   [5 10] bar124
   [6 0] bar109
   [6 1] bar116
   [6 2] bar145
   [6 3] bar52
   [6 4] bar1
   [6 5] bar23
   [6 6] bar89
   [6 7] bar72
   [6 8] bar149
   [6 9] bar173
   [6 10] bar44
   [7 0] bar14
   [7 1] bar83
   [7 2] bar79
   [7 3] bar170
   [7 4] bar93
   [7 5] bar151
   [7 6] bar172
   [7 7] bar111
   [7 8] bar8
   [7 9] bar78
   [7 10] bar131})

(def VEL 100)

(def PPQN 480)

(def ALLOW-GAP 0.9)

(def BAR-KINDS 11)

(def at-pool (at-at/mk-pool))

(defn notes-on-off [sink notes vel dur]
  (doseq [n notes]
    (studio.midi/midi-note sink (pitch/note n) vel dur)))

(defn play-chords [out bar]
  (loop [chords bar
         offset 0]
    (when chords
      (let [[notes r] (first chords)
            duration (* PPQN (rhythm r))]
        (at-at/at (+ (at-at/now) offset)
                  #(notes-on-off out notes VEL duration) at-pool)
        (recur (next chords) (+ offset duration))))))

(defn random-bars [count bars]
  (mapcat #(get bars [% (rand-int BAR-KINDS)]) (range count)))

(defn mozart-score []
  (concat (random-bars 8 waltz1) (random-bars 8 waltz2)))

(defn mozart-game [out]
  (play-chords out (mozart-score)))

(def ms-synth (overtone.midi/midi-out "GS"))
