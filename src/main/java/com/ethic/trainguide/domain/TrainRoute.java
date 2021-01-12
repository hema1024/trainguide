package com.ethic.trainguide.domain;

import com.ethic.trainguide.exception.NoSuchRouteException;
import java.util.List;
import java.util.Set;

public interface TrainRoute {

    /**
     * Get all the stations in this train route graph
     * @return
     */
    public Set<Station> getStations();

    /**
     * Find the station object, given its name
     * @param name
     * @return
     */
    public Station getStationByName(String name);

    /**
     * Find the distance of a route,
     * provided the list of stations in the route.
     * @param stationNames
     * @return
     * @throws NoSuchRouteException
     */
    public int getDistanceOfRoute(List<String> stationNames) throws NoSuchRouteException;

    /**
     * Find the number of different routes between two stations,
     * having a maximum of N stops.
     * @return
     */
    public List<List<String>> getRoutesByNumberOfStops(String originName, String destinationName, int maxNumerOfStops);


    /**
     * Find routes between two stations,
     * lesser than given distance.
     * @param originName
     * @param destinationName
     * @param maxDistance
     * @return
     */
    public List<List<String>> getRoutesByDistance(String originName, String destinationName, int maxDistance);

}
