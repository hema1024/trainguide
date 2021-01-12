package com.ethic.trainguide.graph;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import com.ethic.trainguide.exception.NoSuchRouteException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class TrainRouteGraphImplTest extends TrainGuideTestBase {


    @Test
    public void testAddStation() throws CannotBuildTrainRouteException {

        TrainRoute trainRoute = getAValidTrainRoute();

        Set<Station> stations = trainRoute.getStations();

        // check if the stations we added are in the object
        EXPECTED_STATIONS.forEach(s ->
                assertTrue(String.format("Station %s not found in trainRoute [%s]", s.getName(), trainRoute.getStations()),
                        stations.contains(s)));

    }


    @Test
    public void testGetStationByName() throws CannotBuildTrainRouteException {

        TrainRoute trainRoute = getAValidTrainRoute();

        // get station by name and verify it is present
        EXPECTED_STATIONS.forEach(s -> {
            Station station = trainRoute.getStationByName(s.getName());

            assertTrue(String.format("Station %s not found trainRoute [%s]", s.getName(), trainRoute.getStations()),
                    station.getName().equalsIgnoreCase(s.getName()));
        });
    }

    @Test
    public void testGetDistanceOfRoute_ForValidRoute() throws NoSuchRouteException, CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();

        List<String> route = Arrays.asList("D", "C", "E", "B");

        int expectedDistance = 13;
        int distance = trainRoute.getDistanceOfRoute(route);

        assertTrue(String.format("Expected distance %d, got %d", expectedDistance, distance),
                distance == expectedDistance);

    }

    @Test(expected = NoSuchRouteException.class)
    public void testGetDistanceOfRoute_ForInValidRoute() throws NoSuchRouteException, CannotBuildTrainRouteException {

        TrainRoute trainRoute = getAValidTrainRoute();

        List<String> route = Arrays.asList("D", "C", "B");
        trainRoute.getDistanceOfRoute(route);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidOrigin() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();
        trainRoute.getRoutesByNumberOfStops(null, "B", 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidDestination() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();
        trainRoute.getRoutesByNumberOfStops("A", null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidStops() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();
        trainRoute.getRoutesByNumberOfStops("A", "B", -9);
    }

}
