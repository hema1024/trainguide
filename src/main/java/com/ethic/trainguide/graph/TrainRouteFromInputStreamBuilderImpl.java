package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TrainRouteFromInputStreamBuilderImpl implements TrainRouteFromInputStreamBuilder {

    private InputStream graphDataInputStream;
    private Map<String, Station> nameToStationMap;

    private class OneStop {
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

    public TrainRouteFromInputStreamBuilderImpl(InputStream inputStream) {
        if(inputStream == null) {
            throw new IllegalArgumentException("inputStream must not be null");
        }

        this.graphDataInputStream = inputStream;
    }


    /**
     *
     * @return
     * @throws CannotBuildTrainRouteException
     */
    @Override
    public TrainRoute build() throws CannotBuildTrainRouteException {

        nameToStationMap = buildStations();

        TrainRoute trainRoute = new TrainRouteGraphImpl();

        // add all stations to the train route graph
        for(Station station : nameToStationMap.values()) {
            trainRoute.addStation(station);
        }

        return trainRoute;
    }

    private Map<String, Station> buildStations() throws CannotBuildTrainRouteException {
        BufferedReader reader = null;
        Map<String, Station> nameToStationMap = new HashMap();

        try {
            reader = new BufferedReader(new InputStreamReader(graphDataInputStream));

            String line = null;
            while ((line = reader.readLine()) != null) {
                addDestinationToSourceStation(nameToStationMap, parseOneStop(line));
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
    private OneStop parseOneStop(String line) {
        String[] chunks = line.split("-");
        if(chunks.length < 3) {
            throw new IllegalArgumentException("Expected line format <origin>-<destination>-<distance>");
        }

        return new OneStop(
                chunks[0],
                chunks[1],
                Integer.parseInt(chunks[2])
        );

    }

}
