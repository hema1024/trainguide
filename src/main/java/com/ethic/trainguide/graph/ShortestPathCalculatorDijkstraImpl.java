package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchStationException;
import com.ethic.trainguide.graph.ShortestPathCalculator;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.ethic.trainguide.graph.TrainRouteValidate.validateTrainRoute;

/**
 * Implementation of shortest path from origin station to the rest of the stations
 * in the TrainRoute object.  This implementation uses the Dijkstra's algorithm for
 * finding the shortest path.
 * Time complexity : 
 */
public class ShortestPathCalculatorDijkstraImpl implements ShortestPathCalculator {

    @Override
    public Map<String, Integer> getShortestPathFromOrigin(TrainRoute trainRoute, String originName) throws NoSuchStationException {
        validateTrainRoute(trainRoute);

        if(StringUtils.isEmpty(originName)) {
            throw new IllegalArgumentException("originName must not be null");
        }

        Station origin = trainRoute.getStationByName(originName);

        if(origin == null) {
            throw new NoSuchStationException(String.format("Origin station '%s', not found in route", origin));
        }

        Map<Station, Integer> stationShortestDistanceMap = new HashMap<>();

        // distance between the origin station and itself is 0
        stationShortestDistanceMap.put(origin, 0);

        Set<Station> settledStations = new HashSet<>();
        Set<Station> unsettledStations = new HashSet<>();

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

        return reformatShortestDistanceMap(stationShortestDistanceMap);

    }

    private Map<String,Integer> reformatShortestDistanceMap(Map<Station, Integer> stationShortestDistanceMap) {

        Map<String,Integer> shortestDistanceMap = new HashMap<>();
        stationShortestDistanceMap.entrySet().stream()
                .forEach(e -> shortestDistanceMap.put(e.getKey().getName(), e.getValue()));

        return shortestDistanceMap;

    }

    private void setShortestDisatance(Station currentStation, Station adjacentStation, int edgeDistance, Map<Station, Integer> shortestDistanceFromOriginMap) {
        int distanceOfCurrentStationFromOrigin = shortestDistanceFromOriginMap.getOrDefault(currentStation, Integer.MAX_VALUE);
        int oldDistance = shortestDistanceFromOriginMap.getOrDefault(adjacentStation, Integer.MAX_VALUE);
        int newDistance = distanceOfCurrentStationFromOrigin + edgeDistance;
        if(newDistance < oldDistance) {
            shortestDistanceFromOriginMap.put(adjacentStation, newDistance);
        }
    }

    private Station getStationWithShortestDistance(Set<Station> unsettledStations, Map<Station, Integer>  shortestDistanceMap) {
        return unsettledStations.stream()
                .min((s1, s2) -> shortestDistanceMap.get(s1) - shortestDistanceMap.get(s2))
                .get();
    }
}
