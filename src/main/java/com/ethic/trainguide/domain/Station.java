package com.ethic.trainguide.domain;

import com.ethic.trainguide.graph.TrainRoute;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * class to store information
 * about a train station.
 * This will be vertices in the graph.
 */
public class Station {

    private String name;

    private Distance distance;

    private Map<Station, Distance> adjacentStations = new HashMap<>();

    private List<Station> shortestPathFromOrigin = new LinkedList();

    public Station(String name) {
        if(StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name must not be null");
        }

        this.name = name;
        this.distance = new Distance(Integer.MAX_VALUE);
    }

    public String getName() {
        return name;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public void setDistance(int distance) {
        this.distance = new Distance(distance);
    }

    public Map<Station, Distance> getAdjacentStations() {
        return adjacentStations;
    }

    public List<Station> getShortestPathFromOrigin() {
        return shortestPathFromOrigin;
    }

    public void setShortestPathFromOrigin(List<Station> shortestPathFromOrigin) {
        this.shortestPathFromOrigin = shortestPathFromOrigin;
    }

    public void addAdjacentStation(Station adjacentStation, Distance distance) {
        if(adjacentStation == null) {
            throw new IllegalArgumentException("adjacentStation must not be null");
        }

        adjacentStations.put(adjacentStation, distance);
    }

    public Map.Entry<Station, Distance>  getAdjacentStationByName(String name) {

        for(Map.Entry<Station, Distance> entry : getAdjacentStations().entrySet()) {
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
