package com.ethic.trainguide.graph;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TrainRouteFromFileBuilderImplTest extends TrainGuideTestBase {

    @Test
    public void testBuildTrainRouteGraph() throws CannotBuildTrainRouteException {

        TrainRoute expectedTrainRoute = getAValidTrainRoute();
        TrainRoute trainRoute = new TrainRouteFromInputStreamBuilderImpl(asInputStream(expectedTrainRoute)).build();

        Set<Station> stations = trainRoute.getStations();

        // assert that the train route graph
        // has the expected no. of stations provided in the input
        assertTrue(String.format("Expected %d stations, found %d",
                expectedTrainRoute.getStations().size(),
                trainRoute.getStations().size()),
                trainRoute.getStations().size() == expectedTrainRoute.getStations().size());

        // assert that the train route graph has
        // each of the stations (by name) provided in the input
        expectedTrainRoute.getStations()
                .stream()
                .forEach(s -> assertTrue(String.format("Station '%s' not found in train route graph", s),
                        stations.contains(new Station(s.getName()))));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput_ForInputStream() throws CannotBuildTrainRouteException {
        new TrainRouteFromInputStreamBuilderImpl(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInput_ForDistance() throws CannotBuildTrainRouteException {
        new TrainRouteFromInputStreamBuilderImpl(asInputStream(getAnIValidTrainRoute())).build();
    }

}
