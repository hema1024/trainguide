package com.ethic.trainguide.graph;

public class TrainRouteValidate {

    public static void validateTrainRoute(TrainRoute trainRoute) {
        if(trainRoute == null) {
            throw new IllegalArgumentException("trainRoute must not be null");
        }

        if(trainRoute.getStations() == null || trainRoute.getStations().isEmpty()) {
            throw new IllegalArgumentException("No stations information found in trainRoute");
        }
    }
}
