package com.ethic.trainguide.graph;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TrainRouteGraphImpl_GetRoutesByStopsTest extends TrainGuideTestBase {

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForNullOrigin() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();
        trainRoute.getRoutesByNumberOfStops(null, "B", 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidOrigin() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();
        trainRoute.getRoutesByNumberOfStops("K", "B", 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForNullDestination() throws CannotBuildTrainRouteException  {
        TrainRoute trainRoute = getAValidTrainRoute();
        trainRoute.getRoutesByNumberOfStops("A", null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidDestination() throws CannotBuildTrainRouteException  {
        TrainRoute trainRoute = getAValidTrainRoute();
        trainRoute.getRoutesByNumberOfStops("A", "L", 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidStops() throws CannotBuildTrainRouteException  {
        TrainRoute trainRoute = getAValidTrainRoute();
        trainRoute.getRoutesByNumberOfStops("A", "B", -9);
    }

    @Test
    public void testGetRoutesByNumberOfStops_ForSameOriginAndDestination() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();
        List<String> expectedRoutes = Arrays.asList("CDC", "CEBC");

        List<List<String>> routes = trainRoute.getRoutesByNumberOfStops("C", "C", 3);

        // convert to list of string to make comparisons easier
        List<String> reformattedRoutes = reformatListStringStringToListString(routes);

        assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, routes),
                expectedRoutes.size() == routes.size());

        expectedRoutes.forEach(r ->
                assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                        reformattedRoutes.contains(r)));
    }

    @Test
    public void testGetRoutesByNumberOfStops_ForDifferentOriginAndDestination() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();
        List<String> expectedRoutes = Arrays.asList("ABCDE", "ABCE", "ADCDE", "ADCE", "ADE", "AE", "AEBCE");

        List<List<String>> routes = trainRoute.getRoutesByNumberOfStops("A", "E", 4);

        // convert to list of string to make comparisons easier
        List<String> reformattedRoutes = reformatListStringStringToListString(routes);

        assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                expectedRoutes.size() == reformattedRoutes.size());

        expectedRoutes.forEach(r ->
                assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                        reformattedRoutes.contains(r)));
    }

    @Test
    public void testGetRoutesByNumberOfStops_ForNoRoute() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();

        // there is no route between E and A
        List<List<String>> routes = trainRoute.getRoutesByNumberOfStops("E", "A", 4);

        assertTrue(String.format("Expected no routes, but found %s", routes),
                routes.size() == 0);
    }

    @Test
    public void testGetRoutesByNumberOfStops_ForNoRouteWithinGivenNumberOfStops() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();

        // there is a route between E and D, but only with 3 stops, not 2
        List<List<String>> routes = trainRoute.getRoutesByNumberOfStops("E", "D", 2);

        assertTrue(String.format("Expected no routes, but found %s", routes),
                routes.size() == 0);

    }

}
