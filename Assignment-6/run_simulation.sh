#!/bin/bash

java -jar jars/simulator.jar src/configuration/config.xml Q2/1024_16/stats_ev.txt test_cases/evenorodd.out
java -jar jars/simulator.jar src/configuration/config.xml Q2/1024_16/stats_fi.txt test_cases/fibonacci.out
java -jar jars/simulator.jar src/configuration/config.xml Q2/1024_16/stats_de.txt test_cases/descending.out
java -jar jars/simulator.jar src/configuration/config.xml Q2/1024_16/stats_pa.txt test_cases/palindrome.out
java -jar jars/simulator.jar src/configuration/config.xml Q2/1024_16/stats_pr.txt test_cases/prime.out