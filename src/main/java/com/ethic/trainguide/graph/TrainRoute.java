package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.exception.NoSuchRouteException;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface TrainRoute {

    public void addStation(Station station);

    public Set<Station> getStations();

    public Station getStationByName(String name);

    public int getDistanceOfRoute(List<String> stationNames) throws NoSuchRouteException;


}
