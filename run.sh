#!/bin/bash

read -p "
Pre-requisites : make sure the following are installed before proceeding  
(1) Default java version 8 
(2) Maven.  

Press enter to continue..." userInput

echo "*********Packaging jar********"
mvn package

echo "*********Running application with sample graph data provided in the exercise********"
java -cp target/trainguide-1.0-jar-with-dependencies.jar com.ethic.trainguide.TrainGuide -routeGraphFile src/main/resources/sample_data/ethic_sample.csv

