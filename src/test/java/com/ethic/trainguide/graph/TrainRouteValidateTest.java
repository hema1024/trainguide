package com.ethic.trainguide.graph;

import com.ethic.trainguide.TrainGuideTestBase;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import org.junit.Test;

public class TrainRouteValidateTest extends TrainGuideTestBase {

    @Test(expected = IllegalArgumentException.class)
    public void testValidateTrainRoute_ForNullTrainRoute() {
        TrainRouteValidate.validateTrainRoute(null);
    }

    @Test
    public void testValidateTrainRoute_ForValidTrainRoute() throws CannotBuildTrainRouteException {
        TrainRouteValidate.validateTrainRoute(getAValidTrainRoute());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateStation_ForNullStation() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();

        TrainRouteValidate.validateStation(trainRoute, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateStation_ForInvalidStation() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();

        TrainRouteValidate.validateStation(trainRoute, "L");
    }

    @Test
    public void testValidateStation_ForValidStation() throws CannotBuildTrainRouteException {
        TrainRoute trainRoute = getAValidTrainRoute();

        TrainRouteValidate.validateStation(trainRoute, "A");
    }
}
