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
     * Map to store adjacent stations to this station.
     * The map value is the distance between this station
     * and the adjacent station.
     */
    private Map<Station, Integer> adjacentStations = new HashMap<>();

    public Station(String name) {
        if(StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name must not be null");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<Station, Integer> getAdjacentStations() {
        return adjacentStations;
    }

    public void addAdjacentStation(Station adjacentStation, Integer distance) {
        if(adjacentStation == null) {
            throw new IllegalArgumentException("adjacentStation must not be null");
        }

        if(distance < 0) {
            throw new IllegalArgumentException("distance must be >= 0");
        }

        adjacentStations.put(adjacentStation, distance);
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
