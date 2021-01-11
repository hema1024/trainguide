package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TrainRouteFromInputStreamBuilderImpl implements TrainRouteFromInputStreamBuilder {

    private static final String DEFAULT_DELIMITER = ",";
    private InputStream graphDataInputStream;
    private String columnDelimiter;

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
        this(inputStream, DEFAULT_DELIMITER);
    }

    public TrainRouteFromInputStreamBuilderImpl(InputStream inputStream, String columnDelimiter) {
        if(inputStream == null) {
            throw new IllegalArgumentException("inputStream must not be null");
        }

        this.graphDataInputStream = inputStream;

        if(StringUtils.isEmpty(columnDelimiter)) {
            this.columnDelimiter = DEFAULT_DELIMITER;
        } else {
            this.columnDelimiter = columnDelimiter;
        }
    }


    /**
     *
     * @return
     * @throws CannotBuildTrainRouteException
     */
    @Override
    public TrainRoute build() throws CannotBuildTrainRouteException {

        Map<String, Station> nameToStationMap = buildStations();

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

        int lineNumber = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(graphDataInputStream));
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
