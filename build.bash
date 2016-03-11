#!/bin/bash
set -e -v
cd src
javac Game.java Model.java View.java Controller.java Sprite.java Bird.java Tree.java ControlAction.java Plant.java AmmoBar.java Egg.java

mv *.class ../.
