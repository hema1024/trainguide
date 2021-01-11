package com.ethic.trainguide.factory;

import com.ethic.trainguide.calculate.ShortestPath;
import com.ethic.trainguide.calculate.ShortestPathDijkstraImpl;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.graph.TrainRouteBuilder;
import com.ethic.trainguide.graph.TrainRouteFromInputStreamBuilderImpl;
import com.ethic.trainguide.graph.TrainRouteGraphImpl;

import java.io.InputStream;

/**
 * Factory class to provide instances of TrainRoute and
 * TrainRouteBuilder implmentations
 */
public class TrainGuideFactory {

    private static ShortestPath shortestPath = null;

    public static TrainRouteBuilder newTrainRouteFromInputStreamBuilder(InputStream inputStream, String columnDelimiter) {
        return new TrainRouteFromInputStreamBuilderImpl(inputStream, columnDelimiter);
    }

    public static TrainRoute newTrainRoute() {
        return new TrainRouteGraphImpl();
    }

    synchronized public static ShortestPath newShortestPath() {
        if(shortestPath == null) {
            shortestPath = new ShortestPathDijkstraImpl();
        }

        return shortestPath;
    }

}
