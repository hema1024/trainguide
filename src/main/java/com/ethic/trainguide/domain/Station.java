package com.ethic.trainguide.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * class to store information
 * about a train station.
 * This will be vertices in the graph.
 */
public class Station {

    /**
     * Name of the station.
     * This is also the unique ID, that identifies a station.
     */
    private String name;

    /**
     * Distance from origin will be set only
     * after a call to some shortest path algorithm.
     * Until then this will be Integer.MAX_VALUE
     */
    private Integer distanceFromOrigin;

    /**
     * Map to store adjacent stations to this station.
     * The map value is the distance between this station
     * and the adjacent station.
     */
    private Map<Station, Integer> adjacentStations = new HashMap<>();

    /**
     * Linked list that represents the route of the
     * shortest path from the origin station to this station.
     */
    private List<Station> shortestPathFromOrigin = new LinkedList();

    public Station(String name) {
        if(StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name must not be null");
        }

        this.name = name;
        this.distanceFromOrigin = Integer.MAX_VALUE;
    }

    public String getName() {
        return name;
    }

    public Integer getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public void setDistanceFromOrigin(int distanceFromOrigin) {
        this.distanceFromOrigin = distanceFromOrigin;
    }

    public Map<Station, Integer> getAdjacentStations() {
        return adjacentStations;
    }

    public void addAdjacentStation(Station adjacentStation, Integer distance) {
        if(adjacentStation == null) {
            throw new IllegalArgumentException("adjacentStation must not be null");
        }

        adjacentStations.put(adjacentStation, distance);
    }

    public List<Station> getShortestPathFromOrigin() {
        return shortestPathFromOrigin;
    }

    public void setShortestPathFromOrigin(List<Station> shortestPathFromOrigin) {
        this.shortestPathFromOrigin = shortestPathFromOrigin;
    }

    public Map.Entry<Station, Integer>  getAdjacentStationByName(String name) {

        for(Map.Entry<Station, Integer> entry : getAdjacentStations().entrySet()) {
            if(entry.getKey().getName().equalsIgnoreCase(name)) {
                return entry;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Station station = (Station) o;

        return name.equals(station.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return getName();
    }
}
