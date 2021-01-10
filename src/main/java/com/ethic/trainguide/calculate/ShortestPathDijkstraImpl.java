package com.ethic.trainguide.calculate;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;

import java.util.*;

import static com.ethic.trainguide.graph.TrainRouteValidate.validateTrainRoute;

public class ShortestPathDijkstraImpl implements ShortestPath {


    @Override
    public void setShortestPathFromOrigin(TrainRoute trainRoute, Station origin) {

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
    public void setShortestPathFromOrigin(TrainRoute trainRoute, String originName) {
        validateTrainRoute(trainRoute);

        Station origin = trainRoute.getStationByName(originName);
        setShortestPathFromOrigin(trainRoute, origin);
    }

    private void setShortestDisatance(Station currentStation, Station adjacentStation, int edgeDistance) {
        int distanceFromOriginOfCurrentStation = currentStation.getDistanceFromOrigin();
        int newDistance = distanceFromOriginOfCurrentStation + edgeDistance;
        if(newDistance < adjacentStation.getDistanceFromOrigin()) {
            adjacentStation.setDistanceFromOrigin(newDistance);
            List<Station> shortestPath = new LinkedList<>(currentStation.getShortestPathFromOrigin());
            shortestPath.add(currentStation);
            adjacentStation.setShortestPathFromOrigin(shortestPath);
        }
    }

    private Station getStationWithShortestDistance(Set<Station> unsettledStations) {
        return unsettledStations.stream()
                .min((s1, s2) -> s1.getDistanceFromOrigin() - s2.getDistanceFromOrigin())
                .get();
    }
}
