package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.Distance;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.exception.NoSuchRouteException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TrainRouteGraphImpl implements TrainRoute {

    private Set<Station> stations = new HashSet();

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
        return getStations()
                .stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .get();
    }

    @Override
    public int getDistanceOfRoute(List<String> stationNames) throws NoSuchRouteException {

        if(stationNames == null || stationNames.isEmpty()) {
            throw new IllegalArgumentException("stationNames must not be empty");
        }

        Station currentStation = getStationByName(stationNames.get(0));
        int distanceOfRoute = 0;

        if(stationNames.size() > 1) {
            for (int i = 1; i < stationNames.size(); i++) {
                Map.Entry<Station, Distance> adjacentStationEntry = currentStation.getAdjacentStationByName(stationNames.get(i));
                if(adjacentStationEntry == null) {
                    throw new NoSuchRouteException("No such route " + stationNames.toString());
                }
                distanceOfRoute += adjacentStationEntry.getValue().getDistance();
                currentStation = adjacentStationEntry.getKey();
            }
        }

        return distanceOfRoute;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<String> routes = new ArrayList();

        for(Station station : stations) {
            for (Map.Entry<Station, Distance> entry : station.getAdjacentStations().entrySet()) {
                routes.add(String.format("%s-%s-%d", station.getName(), entry.getKey().getName(), entry.getValue().getDistance()));
            }
        }

        return StringUtils.join(routes, "\n");
    }

}
