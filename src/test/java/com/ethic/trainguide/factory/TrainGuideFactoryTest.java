package com.ethic.trainguide.factory;

import com.ethic.trainguide.TrainGuideCli;
import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.cache.LRUCache;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TrainGuideFactoryTest extends TrainGuideTestBase {

    @Test
    public void testNewTrainRoute() throws CannotBuildTrainRouteException {

        TrainRoute expectedTrainRoute = getAValidTrainRoute();
        TrainRoute trainRoute = TrainGuideFactory.newTrainRoute(asInputStream(expectedTrainRoute), ",");

        assertTrue(expectedTrainRoute.getStations().size() == trainRoute.getStations().size());

        assertTrue(expectedTrainRoute.getStations().containsAll(trainRoute.getStations()));

    }

    @Test
    public void tesGetShortestPathCalculator() {
        assertTrue(TrainGuideFactory.getShortestPathCalculator() != null);
    }

    @Test
    public void tesGetLRUCache() {
        LRUCache cache = TrainGuideFactory.getLRUCache();
        assertTrue(cache != null);
        assertTrue(cache.getCapacity() == TrainGuideFactory.LRU_CACHE_CAPACITY);
    }

    @Test
    public void tesGetTrainGuideCli() {
        TrainGuideCli cli = TrainGuideFactory.getTrainGuideCli(
                getClass().getClassLoader().getResource(GRAPH_RESOURCE_FILE_WITH_VALID_DATA).getFile(), ",");

        assertTrue(cli != null);
    }
}
