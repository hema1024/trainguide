package com.ethic.trainguide.graph;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchRouteException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class TrainRouteGraphImpl_GetRoutesByNumberOfStopsTest extends TrainGuideTestBase {

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidOrigin() {
        TrainRouteGraphImpl rainRoute = new TrainRouteGraphImpl();
        rainRoute.getRoutesByNumberOfStops(null, "B", 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidDestination() {
        TrainRouteGraphImpl rainRoute = new TrainRouteGraphImpl();
        rainRoute.getRoutesByNumberOfStops("A", null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByNumberOfStops_ForInvalidStops() {
        TrainRouteGraphImpl rainRoute = new TrainRouteGraphImpl();
        rainRoute.getRoutesByNumberOfStops("A", "B", -9);
    }

    @Test
    public void testGetRoutesByNumberOfStops_ForSameOriginAndDestination() {
        TrainRoute rainRoute = getAValidTrainRoute();
        List<String> expectedRoutes = Arrays.asList("CDC", "CEBC");

        List<List<String>> routes = rainRoute.getRoutesByNumberOfStops("C", "C", 3);

        // convert to list of string to make comparisons easier
        List<String> reformattedRoutes = reformatListStringStringToListString(routes);

        assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, routes),
                expectedRoutes.size() == routes.size());

        expectedRoutes.forEach(r ->
                assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                        reformattedRoutes.contains(r)));
    }

    @Test
    public void testGetRoutesByNumberOfStops_ForDifferentOriginAndDestination() {
        TrainRoute rainRoute = getAValidTrainRoute();
        List<String> expectedRoutes = Arrays.asList("ABCDE", "ABCE", "ADCDE", "ADCE", "ADE", "AE", "AEBCE");

        List<List<String>> routes = rainRoute.getRoutesByNumberOfStops("A", "E", 4);

        // convert to list of string to make comparisons easier
        List<String> reformattedRoutes = reformatListStringStringToListString(routes);

        assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                expectedRoutes.size() == reformattedRoutes.size());

        expectedRoutes.forEach(r ->
                assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                        reformattedRoutes.contains(r)));
    }

    @Test
    public void testGetRoutesByNumberOfStops_ForNoRoute() {
        TrainRoute rainRoute = getAValidTrainRoute();

        // there is no route between E and A
        List<List<String>> routes = rainRoute.getRoutesByNumberOfStops("E", "A", 4);

        assertTrue(String.format("Expected no routes, but found %s", routes),
                routes.size() == 0);
    }

    @Test
    public void testGetRoutesByNumberOfStops_ForNoRouteWithinGivenNumberOfStops() {
        TrainRoute rainRoute = getAValidTrainRoute();

        // there is a route between E and D, but only with 3 stops, not 2
        List<List<String>> routes = rainRoute.getRoutesByNumberOfStops("E", "D", 2);

        assertTrue(String.format("Expected no routes, but found %s", routes),
                routes.size() == 0);

    }

}
