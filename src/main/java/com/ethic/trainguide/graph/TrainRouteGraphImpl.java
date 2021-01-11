package com.ethic.trainguide.graph;

import com.ethic.trainguide.calculate.ShortestPath;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchRouteException;
import com.ethic.trainguide.exception.NoSuchStationException;
import com.ethic.trainguide.factory.TrainGuideFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class TrainRouteGraphImpl implements TrainRoute {

    private Set<Station> stations = new HashSet();
    private static final String DELIMITER = "\t";

    /**
     * the origin station from which
     * shortest path information is calculate
     * and set in all other station objects.
     * When the object is created, this is null, meaning
     * there shortest path info is not available
     * for any stations.
     * In the first call to getShortestDistance()
     * the shortest distance is computed based on the origin
     * passed in.  After that, an instance of this object
     * can only be used for getting shortest distance for the same
     * origin station.
     */
    private String shortestPathOriginStation = null;

    public void TrainRouteGraph() {
    }

    @Override
    public void addStation(Station station) {
        if(station == null) {
            throw new IllegalArgumentException("vertex must not be null");
        }

        stations.add(station);
    }

    @Override
    public Set<Station> getStations() {
        return stations;
    }

    @Override
    public Station getStationByName(String name) {
        Optional<Station> station = getStations()
                .stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst();

        return station.orElse(null);
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
        if(getStationByName(originName) == null) {
            throw new IllegalArgumentException(String.format("Origin station %s does not exist in train route", originName));
        }

        Station destinationStation = getStationByName(destinationName);
        if(getStationByName(originName) == null) {
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
        if(getStationByName(originName) == null) {
            throw new IllegalArgumentException(String.format("Origin station %s does not exist in train route", originName));
        }

        Station destinationStation = getStationByName(destinationName);
        if(getStationByName(originName) == null) {
            throw new IllegalArgumentException(String.format("Destination station %s does not exist in train route", destinationName));
        }

        List<String> routes = new ArrayList();

        getRoutesByDistance(destinationStation,
                maxDistance, 0, originStation,
                0, "", routes);

        return reformatRoutes(routes);
    }

    @Override
    public int getShortestDistance(String origin, String destination) throws NoSuchStationException {

        if(StringUtils.isEmpty(origin) || StringUtils.isEmpty(destination)) {
            throw new IllegalArgumentException("origin/destination stations names must not be null");
        }

        Station originStation = getStationByName(origin);
        Station destinationStation = getStationByName(destination);

        if(originStation == null || destinationStation == null) {
            throw new NoSuchStationException(String.format("No such origin/destination station exists [%s, %s]", origin, destination));
        }

        if(shortestPathOriginStation != null) {
            if(!shortestPathOriginStation.equalsIgnoreCase(originStation.getName())) {
                throw new IllegalArgumentException("This instance can return shortest distance only when origin is " + origin);
            }
        } else {
            // compute shortest paths to all stations from this origin
            ShortestPath shortestPath = TrainGuideFactory.getShortestPath();
            shortestPath.setShortestPathFromOrigin(this, origin);
        }

        return destinationStation.getDistanceFromOrigin();

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

}
