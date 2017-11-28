#!/usr/bin/env bash

#download dependencies
mkdir -p libs
cd libs
wget http://central.maven.org/maven2/com/airhacks/afterburner.fx/1.7.0/afterburner.fx-1.7.0.jar
wget http://central.maven.org/maven2/com/aquafx-project/aquafx/0.1/aquafx-0.1.jar
wget http://central.maven.org/maven2/org/projectlombok/lombok/1.16.18/lombok-1.16.18.jar
cd ..
#compile using ant
ant