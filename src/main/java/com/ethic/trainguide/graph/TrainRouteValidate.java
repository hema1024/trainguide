package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.TrainRoute;
import org.apache.commons.lang3.StringUtils;

/**
 * helper/utility class for some validations
 */
public class TrainRouteValidate {

    public static void validateTrainRoute(TrainRoute trainRoute) {
        if(trainRoute == null) {
            throw new IllegalArgumentException("trainRoute must not be null");
        }

        if(trainRoute.getStations() == null || trainRoute.getStations().isEmpty()) {
            throw new IllegalArgumentException("No stations information found in trainRoute");
        }
    }

    /**
     * Method to validate if the given station is present
     * in the trainRoute object.
     * This method assumes the trainRoute object is not null.
     * @param trainRoute
     * @param stationName
     */
    public static void validateStation(TrainRoute trainRoute, String stationName) {

        if(StringUtils.isEmpty(stationName)) {
            throw new IllegalArgumentException("stationName must not be null");
        }

        if(trainRoute.getStationByName(stationName) == null) {
            throw new IllegalArgumentException(String.format("Station %s does not exist in train route", stationName));
        }
    }
}
