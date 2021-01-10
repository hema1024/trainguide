package com.ethic.trainguide.graph;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.domain.TrainRoute;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class TrainRouteGraphImpl_GetRoutesByDistanceTest extends TrainGuideTestBase {

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByDistance_ForInvalidOrigin() {
        TrainRouteGraphImpl rainRoute = new TrainRouteGraphImpl();
        rainRoute.getRoutesByDistance(null, "B", 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByDistance_ForInvalidDestination() {
        TrainRouteGraphImpl rainRoute = new TrainRouteGraphImpl();
        rainRoute.getRoutesByDistance("A", null, 3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoutesByDistance_ForInvalidStops() {
        TrainRouteGraphImpl rainRoute = new TrainRouteGraphImpl();
        rainRoute.getRoutesByDistance("A", "B", -9);
    }

    @Test
    public void testGetRoutesByDistance_ForSameOriginAndDestination() {
        TrainRoute rainRoute = getAValidTrainRoute();
        List<String> expectedRoutes = Arrays.asList("CDC", "CEBC", "CEBCDC", "CDCEBC", "CDEBC", "CEBCEBC", "CEBCEBCEBC");

        List<List<String>> routes = rainRoute.getRoutesByDistance("C", "C", 30);

        // convert to list of string to make comparisons easier
        List<String> reformattedRoutes = reformatListStringStringToListString(routes);

        assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                expectedRoutes.size() == reformattedRoutes.size());

        expectedRoutes.forEach(r ->
                assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                        reformattedRoutes.contains(r)));
    }

    @Test
    public void testGetRoutesByDistance_ForDifferentOriginAndDestination() {
        TrainRoute rainRoute = getAValidTrainRoute();
        List<String> expectedRoutes = Arrays.asList("ABCE", "ADCE", "ADE", "AE", "AEBCE");

        List<List<String>> routes = rainRoute.getRoutesByDistance("A", "E", 20);

        // convert to list of string to make comparisons easier
        List<String> reformattedRoutes = reformatListStringStringToListString(routes);

        assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                expectedRoutes.size() == reformattedRoutes.size());

        expectedRoutes.forEach(r ->
                assertTrue(String.format("expected Routes %s, but found %s", expectedRoutes, reformattedRoutes),
                        reformattedRoutes.contains(r)));
    }

    @Test
    public void testGetRoutesByDistance_ForNoRoute() {
        TrainRoute rainRoute = getAValidTrainRoute();

        // there is no route between E and A
        List<List<String>> routes = rainRoute.getRoutesByDistance("E", "A", 10);

        assertTrue(String.format("Expected no routes, but found %s", routes),
                routes.size() == 0);
    }

    @Test
    public void testGetRoutesByDistance_ForNoRouteWithinGivenDistance() {
        TrainRoute rainRoute = getAValidTrainRoute();

        // there is a between E and D with distance of 15, but not 10
        List<List<String>> routes = rainRoute.getRoutesByDistance("E", "D", 10);

        assertTrue(String.format("Expected no routes, but found %s", routes),
                routes.size() == 0);

    }

}
