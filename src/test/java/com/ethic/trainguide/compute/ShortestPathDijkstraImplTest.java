package com.ethic.trainguide.compute;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.calculate.ShortestPath;
import com.ethic.trainguide.calculate.ShortestPathDijkstraImpl;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchStationException;
import org.junit.Test;

public class ShortestPathDijkstraImplTest extends TrainGuideTestBase {

    @Test
    public void testGetShortestPath() throws NoSuchStationException {
        TrainRoute trainRoute = getAValidTrainRoute();

        ShortestPath shortestPath = new ShortestPathDijkstraImpl();
        shortestPath.setShortestPathFromOrigin(trainRoute, "D");

        Station stationC = trainRoute.getStationByName("A");
        System.out.println(stationC.getShortestPathFromOrigin());
    }
}
