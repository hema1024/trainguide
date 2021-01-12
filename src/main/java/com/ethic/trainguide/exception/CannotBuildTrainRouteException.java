package com.ethic.trainguide.exception;

/**
 * Class for exceptions when building the train route
 * graph, from the provided input
 */
public class CannotBuildTrainRouteException extends Exception {

    public CannotBuildTrainRouteException(Exception e) {
        super(e);
    }

}
