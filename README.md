# TRAIN GUIDE 

## Command Line Usage

```
usage: class com.ethic.trainguide.TrainGuide
 -delimiter <arg>        Delimiter that seprates the 3 columns in the
                         input file (optional, default comma)
 -help                   Display this help
 -routeGraphFile <arg>   Path to the file containing train routes in graph
                         format.  There should be one line per route, with
                         3 delimited columns.  Expected format per line :
                         origin<DELIM>destination<DELIM>distance.
                         (mandatory)
```
                         
## Graph Data File
The example provided in the exercise is available as a file under trainguide/src/main/resources/sample_data

* ethic_sample.csv : example provided by Ethic 

To input your own custom graph data file, make sure the file conforms to the following syntax.

_Graph data file format:_

Each line should contain one route information about two adjacent stations.  
For example if the distance between station A and B (adjacent stations) is 5, 
then there should be one line in the file

```
A,B,5
``` 

If there is a route back from B to A, that would be represented in a new line like this:
```
B,A,5
``` 

Format:

```
<source>,<destination>,<edge_distance>
<source>,<destination>,<edge_distance>
<source>,<destination>,<edge_distance>
<source>,<destination>,<edge_distance>
```

Default delimiter is comma (configurable)


## How to compile and run the program

_**1.  Prerequisites to compile the program**_

*  Java 8
*  Maven

**_2.  Compiling/packaging the jar file_**

Execute the following command from the "trainguide" folder to create the jar file.

```
mvn package

```
**_3.  Running the program_**

_Quick start_

To quick start the program with the graph data file provided in the exercise, just execute the following command.
It will run tests, create the jar file and execute the program.
```
./run.sh
``` 

_To start with custom graph file and column delimiter_

To pass in your custom graph data file, execute the following command with your preferred file.

```
java -cp target/trainguide-1.0-jar-with-dependencies.jar com.ethic.trainguide.TrainGuide -routeGraphFile src/main/resources/sample_data/ethic_sample.csv
```  

To pass in a preferred column delimiter:
```
java -cp target/trainguide-1.0-jar-with-dependencies.jar com.ethic.trainguide.TrainGuide -routeGraphFile src/main/resources/sample_data/ethic_sample.csv -delimiter ","
```

## How to run unit tests

To run the unit tests, execute the following command from the "trainguide" folder

```
mvn test
```
(The test would also run as part of "mvn package")

## Assumptions

* The data in the provided graph input file is valid

    * The edge distance of bi-directional routes between two adjacent stations will be the same.  
For example, if there is a bi-directional route between C and D with edge distance of 8, then both lines representing this route in the file must have a distance of 8.
(D,C,8 and C,D,8)
    * All lines in the graph file have 3 delimited columns (source, destination, edge-distance).  If there are lesser columns in a line, the program will stop with a load error.      

* Town/Station names provided in the graph are considered their unique identifiers and are case sensitive.

* The train route graph data is to be stored in memory, because using an external Graph DB solution is out of scope for this exercise.  
So, the jvm is expected to be provided appropriate memory proportional to the graph data file size, plus additional object/variables and LRU cache overhead.
When passing in initial/max memory (-Xms and -Xmx) the following should be taken into consideration
    *  File size - all routes from the graph file will be loaded into a TrainRoute object with adjacent station associations. 
    *  TrainRoute object stores ALL station names, adjacent stations and distance to adjacent stations in memory.  
    Additional buffer should be accounted for class/instance variables overhead.
    *  There is a simple LRUCache supported in the application for caching shortest distances from a given origin, 
    and the default cache size is 50 elements.  Additional memory should be given if the cache capacity is increased.  
Note: If you are only testing with a few hundred lines, then the default jvm -Xmx value (which is 1/4th of physical memory) should be sufficient. 

* The command line client interface is single threaded, so only one person can interact with an instance of the process/application at a time.
