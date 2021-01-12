package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchStationException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.ethic.trainguide.graph.TrainRouteValidate.validateTrainRoute;

/**
 * Implementation of shortest path from origin station to the rest of the stations
 * in the TrainRoute object.  This implementation uses the Dijkstra's algorithm for
 * finding the shortest path.
 * Time complexity : O(V2)
 */
public class ShortestPathCalculatorDijkstraImpl implements ShortestPathCalculator {

    @Override
    public Map<String, Integer> getShortestPathFromOrigin(TrainRoute trainRoute, String originName) throws NoSuchStationException {
        // perform input validations
        validateTrainRoute(trainRoute);

        if(StringUtils.isEmpty(originName)) {
            throw new IllegalArgumentException("originName must not be null");
        }

        Station origin = trainRoute.getStationByName(originName);

        if(origin == null) {
            throw new NoSuchStationException(String.format("Origin station '%s', not found in route", origin));
        }

        // map to represent shortest distance to a station from the origin
        Map<Station, Integer> stationShortestDistanceMap = new HashMap<>();

        // distance between the origin station and itself is 0
        stationShortestDistanceMap.put(origin, 0);

        // stations whose adjacent vertices are all processed
        // will go into "settled", and still processing will to
        // into "unsettled"
        Set<Station> settledStations = new HashSet<>();
        Set<Station> unsettledStations = new HashSet<>();

        // start with the origin station
        // from which we want to find the shortest
        // distance to all other stations
        unsettledStations.add(origin);

        while(unsettledStations.size() != 0) {
            Station currentStation = getStationWithShortestDistance(unsettledStations, stationShortestDistanceMap);
            unsettledStations.remove(currentStation);
            for(Map.Entry<Station, Integer> adjacentEntry : currentStation.getAdjacentStations().entrySet()) {
                Station adjacentStation = adjacentEntry.getKey();

                if(!settledStations.contains(adjacentStation)) {
                    setShortestDisatance(currentStation, adjacentStation, adjacentEntry.getValue(), stationShortestDistanceMap);
                    unsettledStations.add(adjacentStation);
                }
            }

            settledStations.add(currentStation);

        }

        // reformat the map to have station name as key
        return reformatShortestDistanceMap(stationShortestDistanceMap);

    }

    private Map<String,Integer> reformatShortestDistanceMap(Map<Station, Integer> stationShortestDistanceMap) {

        Map<String,Integer> shortestDistanceMap = new HashMap<>();
        stationShortestDistanceMap.entrySet().stream()
                .forEach(e -> shortestDistanceMap.put(e.getKey().getName(), e.getValue()));

        return shortestDistanceMap;

    }

    /**
     * Compare current distance with new distance
     * and set it if the value is lower
     * @param currentStation
     * @param adjacentStation
     * @param edgeDistance
     * @param shortestDistanceFromOriginMap
     */
    private void setShortestDisatance(Station currentStation, Station adjacentStation, int edgeDistance, Map<Station, Integer> shortestDistanceFromOriginMap) {
        int distanceOfCurrentStationFromOrigin = shortestDistanceFromOriginMap.getOrDefault(currentStation, Integer.MAX_VALUE);
        int oldDistance = shortestDistanceFromOriginMap.getOrDefault(adjacentStation, Integer.MAX_VALUE);
        int newDistance = distanceOfCurrentStationFromOrigin + edgeDistance;
        if(newDistance < oldDistance) {
            shortestDistanceFromOriginMap.put(adjacentStation, newDistance);
        }
    }

    /**
     * Get the lowest distance station from the
     * "unsettled" stations
     * @param unsettledStations
     * @param shortestDistanceMap
     * @return
     */
    private Station getStationWithShortestDistance(Set<Station> unsettledStations, Map<Station, Integer>  shortestDistanceMap) {
        return unsettledStations.stream()
                .min((s1, s2) -> shortestDistanceMap.get(s1) - shortestDistanceMap.get(s2))
                .get();
    }
}
