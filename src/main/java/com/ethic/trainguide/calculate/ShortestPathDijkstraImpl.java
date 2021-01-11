package com.ethic.trainguide.calculate;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchStationException;

import java.util.*;

import static com.ethic.trainguide.graph.TrainRouteValidate.validateTrainRoute;

public class ShortestPathDijkstraImpl implements ShortestPath {


    @Override
    public void setShortestPathFromOrigin(TrainRoute trainRoute, Station origin) throws NoSuchStationException {
        validateTrainRoute(trainRoute);

        if(origin == null || trainRoute.getStationByName(origin.getName()) == null) {
            throw new NoSuchStationException(String.format("Origin station '%s', not found in route", origin));
        }

        // distance between the origin station and itself is 0
        origin.setDistanceFromOrigin(0);

        Set<Station> settledStations = new HashSet<>();
        Set<Station> unsettledStations = new HashSet<>();

        unsettledStations.add(origin);

        while(unsettledStations.size() != 0) {
            Station currentStation = getStationWithShortestDistance(unsettledStations);
            unsettledStations.remove(currentStation);
            for(Map.Entry<Station, Integer> adjacentEntry : currentStation.getAdjacentStations().entrySet()) {
                Station adjacentStation = adjacentEntry.getKey();

                if(!settledStations.contains(adjacentStation)) {
                    setShortestDisatance(currentStation, adjacentStation, adjacentEntry.getValue());
                    unsettledStations.add(adjacentStation);
                }
            }

            settledStations.add(currentStation);

        }

    }

    @Override
    public void setShortestPathFromOrigin(TrainRoute trainRoute, String originName) throws NoSuchStationException {
        Station origin = trainRoute.getStationByName(originName);
        setShortestPathFromOrigin(trainRoute, origin);
    }

    private void setShortestDisatance(Station currentStation, Station adjacentStation, int edgeDistance) {
        int distanceFromOriginOfCurrentStation = currentStation.getDistanceFromOrigin();
        int newDistance = distanceFromOriginOfCurrentStation + edgeDistance;
        if(newDistance < adjacentStation.getDistanceFromOrigin()) {
            adjacentStation.setDistanceFromOrigin(newDistance);
        }
    }

    private Station getStationWithShortestDistance(Set<Station> unsettledStations) {
        return unsettledStations.stream()
                .min(Comparator.comparing(Station::getDistanceFromOrigin))
                .get();
    }
}
