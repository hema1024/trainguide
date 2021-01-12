package com.ethic.trainguide.domain;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.exception.NoSuchRouteException;
import com.ethic.trainguide.exception.NoSuchStationException;

import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface TrainRoute {

    public void addStation(Station station);

    public Set<Station> getStations();

    /**
     * Method to find the station object, given its name
     * @param name
     * @return
     */
    public Station getStationByName(String name);

    /**
     * Method to find the distance of a route,
     * provided the list of stations in the route.
     * @param stationNames
     * @return
     * @throws NoSuchRouteException
     */
    public int getDistanceOfRoute(List<String> stationNames) throws NoSuchRouteException;

    /**
     * Method to find the number of different routes between two stations,
     * having a maximum of N stops.
     * @return
     */
    public List<List<String>> getRoutesByNumberOfStops(String originName, String destinationName, int maxNumerOfStops);


    /**
     * Method to find routes between two stations,
     * lesser than given distance.
     * @param originName
     * @param destinationName
     * @param maxDistance
     * @return
     */
    public List<List<String>> getRoutesByDistance(String originName, String destinationName, int maxDistance);

}
