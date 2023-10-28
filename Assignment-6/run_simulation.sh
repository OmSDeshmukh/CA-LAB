#!/bin/bash

ant
ant make-jar
java -jar jars/simulator.jar src/configuration/config.xml stats1.txt test_cases/evenorodd.out