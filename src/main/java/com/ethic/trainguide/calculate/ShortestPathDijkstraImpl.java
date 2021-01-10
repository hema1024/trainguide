package com.ethic.trainguide.calculate;

import com.ethic.trainguide.domain.Distance;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.graph.TrainRoute;

import java.util.*;

import static com.ethic.trainguide.graph.TrainRouteValidate.validateTrainRoute;

public class ShortestPathDijkstraImpl implements ShortestPath {


    @Override
    public void setShortestPathFromOrigin(TrainRoute trainRoute, Station origin) {

        // distance between the origin station and itself is 0
        origin.setDistance(0);

        Set<Station> settledStations = new HashSet<>();
        Set<Station> unsettledStations = new HashSet<>();

        unsettledStations.add(origin);

        while(unsettledStations.size() != 0) {
            Station currentStation = getStationWithShortestDistance(unsettledStations);
            unsettledStations.remove(currentStation);
            for(Map.Entry<Station, Distance> adjacentEntry : currentStation.getAdjacentStations().entrySet()) {
                Station adjacentStation = adjacentEntry.getKey();

                if(!settledStations.contains(adjacentStation)) {
                    setShortestDisatance(currentStation, adjacentStation, adjacentEntry.getValue().getDistance());
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

    private void setShortestDisatance(Station currentStation, Station adjacentStation, int distance) {
        int originDistance = currentStation.getDistance().getDistance();
        int newDistance = originDistance + distance;
        if(newDistance < adjacentStation.getDistance().getDistance()) {
            adjacentStation.setDistance(newDistance);
            List<Station> shortestPath = new LinkedList<>(currentStation.getShortestPathFromOrigin());
            shortestPath.add(currentStation);
            adjacentStation.setShortestPathFromOrigin(shortestPath);
        }
    }

    private Station getStationWithShortestDistance(Set<Station> unsettledStations) {
        return unsettledStations.stream()
                .min((s1, s2) -> s1.getDistance().getDistance() - s2.getDistance().getDistance())
                .get();
    }
}
