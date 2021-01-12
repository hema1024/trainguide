package com.ethic.trainguide.domain;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class StationTest {

    @Test
    public void testConstructor() {
        String name = "A";
        Station station = new Station(name);

        assertTrue(station.getName().equals(name));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ForNullName() {
        new Station(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_ForEmptyName() {
        new Station("");
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAddAdjacentStation_ForNullStation() {
        Station A = new Station("A");
        A.addAdjacentStation(null, 9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAdjacentStation_ForNegativeDistance() {
        Station A = new Station("A");
        Station B = new Station("B");
        A.addAdjacentStation(B, -9);
    }

    @Test
    public void testAddAdjacentStation_ForValidInput() {

        Station A = new Station("A");
        Station B = new Station("B");
        Station C = new Station("C");

        A.addAdjacentStation(B, 9);
        A.addAdjacentStation(C, 4);

        // assert size
        assertTrue(A.getAdjacentStations().size() == 2);

        // assert two adjacent stations are present
        assertTrue(A.getAdjacentStations().containsKey(B));
        assertTrue(A.getAdjacentStations().containsKey(C));

        // assert distance of adjacent stations
        assertTrue(A.getAdjacentStations().get(B) == 9);
        assertTrue(A.getAdjacentStations().get(C) == 4);

    }


    @Test
    public void testGetAdjacentStationByName() {
        Station A = new Station("A");
        Station B = new Station("B");
        Station C = new Station("C");

        A.addAdjacentStation(B, 9);
        A.addAdjacentStation(C, 4);

        assertTrue(A.getAdjacentStationByName("B").getKey().getName().equals("B"));
        assertTrue(A.getAdjacentStationByName("C").getKey().getName().equals("C"));
    }


}
