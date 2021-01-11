package com.ethic.trainguide.compute;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.calculate.ShortestPath;
import com.ethic.trainguide.calculate.ShortestPathDijkstraImpl;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchStationException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ShortestPathDijkstraImplTest extends TrainGuideTestBase {

    @Test
    public void testGetShortestPath() throws NoSuchStationException {
        TrainRoute trainRoute = getAValidTrainRoute();

        ShortestPath shortestPath = new ShortestPathDijkstraImpl();
        shortestPath.setShortestPathFromOrigin(trainRoute, "D");

        int expectedShortestDistance = 8;
        int shortestDistance = trainRoute.getShortestDistance("D", "C");
        assertTrue(String.format("Exepected distance %d, got %d", expectedShortestDistance, shortestDistance),
                expectedShortestDistance == shortestDistance);

    }

    @Test(expected = NoSuchStationException.class)
    public void testGetShortestPath_ForInvalidStation1() throws NoSuchStationException {
        TrainRoute trainRoute = getAValidTrainRoute();

        ShortestPath shortestPath = new ShortestPathDijkstraImpl();
        shortestPath.setShortestPathFromOrigin(trainRoute, "F");
    }

    @Test(expected = NoSuchStationException.class)
    public void testGetShortestPath_ForInvalidStation2() throws NoSuchStationException {
        TrainRoute trainRoute = getAValidTrainRoute();

        ShortestPath shortestPath = new ShortestPathDijkstraImpl();
        shortestPath.setShortestPathFromOrigin(trainRoute, new Station("F"));
    }

}
