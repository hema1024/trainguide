package com.ethic.trainguide.graph;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchRouteException;
import com.sun.tools.javac.util.List;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TrainRouteGraphImplTest extends TrainGuideTestBase {

    private final List<Station> EXPECTED_STATIONS = List.of(
            new Station("A"),
            new Station("B"),
            new Station("C"));

    @Test
    public void testAddStation() {

        TrainRouteGraphImpl trainRoute = new TrainRouteGraphImpl();

        // add stations list to train route object
        EXPECTED_STATIONS.forEach(s -> trainRoute.addStation(s));

        Set<Station> stations = trainRoute.getStations();

        // check if the stations we added are in the object
        EXPECTED_STATIONS.forEach(s ->
                assertTrue(String.format("Station %s not found in trainRoute [%s]", s.getName(), trainRoute.getStations()),
                        stations.contains(s)));

    }


    @Test
    public void testGetStationByName() {

        TrainRouteGraphImpl trainRoute = new TrainRouteGraphImpl();

        // add stations list to train route object
        EXPECTED_STATIONS.forEach(s -> trainRoute.addStation(s));

        Set<Station> stations = trainRoute.getStations();

        // get station by name and verify it is present,
        // and it is the same object we added
        EXPECTED_STATIONS.forEach(s -> {
            Station station = trainRoute.getStationByName(s.getName());

            assertTrue(String.format("Station %s not found trainRoute [%s]", s.getName(), trainRoute.getStations()),
                    station == s);
        });
    }

    @Test
    public void testGetDistanceOfRoute_ForValidRoute() throws NoSuchRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();

        List<String> route = List.of("D", "C", "E", "B");

        int expectedDistance = 13;
        int distance = trainRoute.getDistanceOfRoute(route);

        assertTrue(String.format("Expected distance %d, got %d", expectedDistance, distance),
                distance == expectedDistance);

    }

    @Test(expected = NoSuchRouteException.class)
    public void testGetDistanceOfRoute_ForInValidRoute() throws NoSuchRouteException {

        TrainRoute trainRoute = getAValidTrainRoute();

        List<String> route = List.of("D", "C", "B");
        trainRoute.getDistanceOfRoute(route);
    }
}
