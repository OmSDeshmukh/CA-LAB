#!/bin/bash
ant 
ant make-jar
java -jar jars/simulator.jar src/configuration/config.xml q4_16.txt PersonalTc/q4.out
# java -jar jars/simulator.jar src/configuration/config.xml stats2.txt PersonalTc/q4.out
# java -jar jars/simulator.jar src/configuration/config.xml stats3.txt PersonalTc/q4.out
# java -jar jars/simulator.jar src/configuration/config.xml stats4.txt PersonalTc/q4.out
# java -jar jars/simulator.jar src/configuration/config.xml stats5.txt PersonalTc/q4.out