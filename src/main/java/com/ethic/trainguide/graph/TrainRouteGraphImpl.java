package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import com.ethic.trainguide.exception.NoSuchRouteException;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class TrainRouteGraphImpl implements TrainRoute {

    /**
     * Set to store all stations in the route graph
     */
    private Set<Station> stations;

    /**
     * A convenience map to store name to station object
     */
    private Map<String, Station> nameToStationMap;

    private static final String DELIMITER = "\t";

    private TrainRouteGraphImpl(Builder builder) {
        this.stations = builder.stations;
        this.nameToStationMap = builder.nameToStationMap;
    }

    @Override
    public Set<Station> getStations() {
        return stations;
    }

    @Override
    public Station getStationByName(String name) {
        return nameToStationMap.get(name);
    }

    @Override
    public int getDistanceOfRoute(List<String> stationNames) throws NoSuchRouteException {

        if(stationNames == null || stationNames.isEmpty()) {
            throw new IllegalArgumentException("stationNames must not be empty");
        }

        Station currentStation = getStationByName(stationNames.get(0));
        if(currentStation == null) {
            throw new NoSuchRouteException("No such route : " + stationNames.toString());
        }

        int distanceOfRoute = 0;

        if(stationNames.size() > 1) {
            for (int i = 1; i < stationNames.size(); i++) {
                Map.Entry<Station, Integer> adjacentStationEntry = currentStation.getAdjacentStationByName(stationNames.get(i));
                if(adjacentStationEntry == null) {
                    throw new NoSuchRouteException("No such route : " + stationNames.toString());
                }
                distanceOfRoute += adjacentStationEntry.getValue();
                currentStation = adjacentStationEntry.getKey();
            }
        }

        return distanceOfRoute;
    }

    @Override
    public List<List<String>> getRoutesByNumberOfStops(String originName, String destinationName, int maxNumerOfStops) {

        if(maxNumerOfStops <= 0) {
            throw new IllegalArgumentException("maxNumerOfStops must at least be 1");
        }

        if(StringUtils.isEmpty(originName) || StringUtils.isEmpty(destinationName)) {
            throw new IllegalArgumentException("origin/destination station name(s) must not be null");
        }

        Station originStation = getStationByName(originName);
        if(originStation == null) {
            throw new IllegalArgumentException(String.format("Origin station %s does not exist in train route", originName));
        }

        Station destinationStation = getStationByName(destinationName);
        if(destinationStation == null) {
            throw new IllegalArgumentException(String.format("Destination station %s does not exist in train route", destinationName));
        }

        List<String> routes = new ArrayList();

        getRoutesByNumberOfStops(destinationStation,
                maxNumerOfStops, originStation, -1,
                "", routes);

        return reformatRoutes(routes);
    }

    private void getRoutesByNumberOfStops(Station destination,
                                          int maxNumerOfStops, Station currentStation,
                                          int currentNoOfStops, String currentRoute,
                                          List<String> routes) {

        currentNoOfStops++;
        if(currentRoute == "") {
            currentRoute += currentStation.getName();
        } else {
            currentRoute += DELIMITER + currentStation.getName();
        }

        if (currentStation.equals(destination) && currentNoOfStops > 0) {
            routes.add(currentRoute);
        }

        // no point going on after this
        if(currentNoOfStops == maxNumerOfStops) {
            return;
        }

        for(Station nextStation : currentStation.getAdjacentStations().keySet()) {
            getRoutesByNumberOfStops(destination, maxNumerOfStops,
                    nextStation, currentNoOfStops, currentRoute, routes);
        }
    }

    @Override
    public List<List<String>> getRoutesByDistance(String originName, String destinationName, int maxDistance) {
        if(maxDistance < 0) {
            throw new IllegalArgumentException("maxDistance must be > 0");
        }

        if(StringUtils.isEmpty(originName) || StringUtils.isEmpty(destinationName)) {
            throw new IllegalArgumentException("origin/destination station name(s) must not be null");
        }

        Station originStation = getStationByName(originName);
        if(originStation == null) {
            throw new IllegalArgumentException(String.format("Origin station %s does not exist in train route", originName));
        }

        Station destinationStation = getStationByName(destinationName);
        if(destinationStation == null) {
            throw new IllegalArgumentException(String.format("Destination station %s does not exist in train route", destinationName));
        }

        List<String> routes = new ArrayList();

        getRoutesByDistance(destinationStation,
                maxDistance, 0, originStation,
                0, "", routes);

        return reformatRoutes(routes);
    }

    private void getRoutesByDistance(Station destination,
                                     int maxDistance, int currentDistance,
                                     Station currentStation, int distanceToNextStation,
                                     String currentRoute, List<String> routes) {

        currentDistance += distanceToNextStation;

        if(currentDistance >= maxDistance) {
            return;
        }

        if(currentRoute == "") {
            currentRoute += currentStation.getName();
        } else {
            currentRoute += DELIMITER + currentStation.getName();
        }

        if (currentStation.equals(destination) && currentDistance > 0) {
            routes.add(currentRoute);
        }

        for(Map.Entry<Station, Integer> nextStationEntry : currentStation.getAdjacentStations().entrySet()) {
            getRoutesByDistance(destination,
                    maxDistance, currentDistance,
                    nextStationEntry.getKey(),
                    nextStationEntry.getValue(),
                    currentRoute, routes);
        }
    }


    /**
     * Method to reformat routes list, to separtae out
     * individual stations in a route into a list
     * @param routes
     * @return
     */
    private List<List<String>> reformatRoutes(List<String> routes) {
        return routes.stream()
                .map(s -> Arrays.asList(s.split(DELIMITER)))
                .collect(Collectors.toList());
    }




    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<String> routes = new ArrayList();

        for(Station station : stations) {
            for (Map.Entry<Station, Integer> entry : station.getAdjacentStations().entrySet()) {
                routes.add(String.format("%s,%s,%d", station.getName(), entry.getKey().getName(), entry.getValue()));
            }
        }

        return StringUtils.join(routes, "\n");
    }

    private static class OneStop {
        private String origin;
        private String destination;
        private int distance;

        public OneStop(String origin, String destination, int distance) {
            this.origin = origin;
            this.destination = destination;
            this.distance = distance;
        }

        public String getOrigin() {
            return origin;
        }

        public String getDestination() {
            return destination;
        }

        public int getDistance() {
            return distance;
        }
    }

    public static class Builder {

        private InputStream inputStream;
        private String columnDelimiter = DEFAULT_DELIMITER;
        private static final String DEFAULT_DELIMITER = ",";
        private Map<String, Station> nameToStationMap;
        private Set<Station> stations;


        public Builder() {
        }

        public Builder withInputStream(InputStream inputStream) {
            if(inputStream == null) {
                throw new IllegalArgumentException("inputStream must not be null");
            }

            this.inputStream = inputStream;
            return this;
        }

        public Builder withColumnDelimiter(String columnDelimiter) {
            if(StringUtils.isEmpty(columnDelimiter)) {
                throw new IllegalArgumentException("columnDelimiter must not be null");
            }

            this.columnDelimiter = columnDelimiter;
            return this;
        }

        public TrainRoute build() throws CannotBuildTrainRouteException {

            nameToStationMap = buildStations();
            stations = new HashSet();

            // add all stations to the train route graph
            for(Station station : nameToStationMap.values()) {
                stations.add(station);
            }

            return new TrainRouteGraphImpl(this);
        }

        private Map<String, Station> buildStations() throws CannotBuildTrainRouteException {
            BufferedReader reader = null;
            Map<String, Station> nameToStationMap = new HashMap();

            int lineNumber = 0;
            try {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    addDestinationToSourceStation(nameToStationMap, parseOneStop(line, lineNumber));
                }
            } catch (IOException e) {
                throw new CannotBuildTrainRouteException(e);
            }

            return nameToStationMap;
        }


        private void addDestinationToSourceStation(Map<String, Station> nameToStationMap, OneStop oneStop) {

            // check if we already processed this station before
            // if yes, use the same object to add new destination information
            Station origin = nameToStationMap.get(oneStop.getOrigin());
            Station destination = nameToStationMap.get(oneStop.getDestination());

            if(origin == null) {
                origin = new Station(oneStop.getOrigin());
                nameToStationMap.put(oneStop.getOrigin(), origin);
            }

            if(destination == null) {
                destination = new Station(oneStop.getDestination());
                nameToStationMap.put(oneStop.getDestination(), destination);
            }

            origin.addAdjacentStation(destination, oneStop.getDistance());
        }

        /**
         * method to parse origin and destination stations
         * and the distance between them from the data file.
         * @param line represents a one stop route, expected format "<origin>-<destination>-<distance>"
         * @return
         */
        private OneStop parseOneStop(String line, int lineNumber) {
            String[] chunks = line.split(columnDelimiter);
            if(chunks.length < 3) {
                throw new IllegalArgumentException(
                        String.format("Error on line %d ('%s').  Expected 3 columns, found %d",
                                lineNumber, line, chunks.length));
            }

            return new OneStop(
                    chunks[0],
                    chunks[1],
                    Integer.parseInt(chunks[2])
            );

        }

    }
}
