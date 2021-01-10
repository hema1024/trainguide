package com.ethic.trainguide.graph;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;

public interface TrainRouteBuilder {

    /**
     * method to load graph data
     * and return a TrainRoute (Graph) object
     */
    public TrainRoute build() throws CannotBuildTrainRouteException;

}
