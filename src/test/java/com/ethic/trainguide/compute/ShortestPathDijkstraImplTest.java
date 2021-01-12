package com.ethic.trainguide.compute;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.calculate.ShortestPathCalculator;
import com.ethic.trainguide.calculate.ShortestPathCalculatorDijkstraImpl;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchStationException;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class ShortestPathDijkstraImplTest extends TrainGuideTestBase {

    @Test
    public void testGetShortestPath() throws NoSuchStationException {
        TrainRoute trainRoute = getAValidTrainRoute();

        ShortestPathCalculator shortestPath = new ShortestPathCalculatorDijkstraImpl();
        Map<String, Integer> shortestDistanceMap = shortestPath.getShortestPathFromOrigin(trainRoute, "D");

        int expectedShortestDistance = 8;
        int shortestDistance = shortestDistanceMap.get("C");
        assertTrue(String.format("Exepected distance %d, got %d", expectedShortestDistance, shortestDistance),
                expectedShortestDistance == shortestDistance);

    }

    @Test(expected = NoSuchStationException.class)
    public void testGetShortestPath_ForInvalidStation1() throws NoSuchStationException {
        TrainRoute trainRoute = getAValidTrainRoute();

        ShortestPathCalculator shortestPath = new ShortestPathCalculatorDijkstraImpl();
        shortestPath.getShortestPathFromOrigin(trainRoute, "F");
    }

    @Test(expected = NoSuchStationException.class)
    public void testGetShortestPath_ForInvalidStation2() throws NoSuchStationException {
        TrainRoute trainRoute = getAValidTrainRoute();

        ShortestPathCalculator shortestPath = new ShortestPathCalculatorDijkstraImpl();
        shortestPath.getShortestPathFromOrigin(trainRoute, "F");
    }

}
