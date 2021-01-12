package com.ethic.trainguide.factory;

import com.ethic.trainguide.cache.LRUCache;
import com.ethic.trainguide.cache.LRUCacheLinkedHashMapImpl;
import com.ethic.trainguide.graph.ShortestPathCalculator;
import com.ethic.trainguide.graph.ShortestPathCalculatorDijkstraImpl;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import com.ethic.trainguide.graph.TrainRouteGraphImpl;

import java.io.InputStream;
import java.util.Map;

/**
 * Factory class to provide instances of TrainRoute,
 * ShortestPathCalculator, LRUCache etc.
 * Encapsulates all implementation classes.
 */
public class TrainGuideFactory {

    private static ShortestPathCalculator shortestPathCalculator = null;
    private static LRUCache<String, Map<String, Integer>> lruCache = null;

    /**
     * Get a new TrainRoute object, provided a graph data input stream
     * @param inputStream inputStream contain graph data of train routes
     * @param columnDelimiter column delimiter separating origin,
     *                        destination and distance in the file
     * @return TrainRoute object containing station and adjacent
     * stations/distance associations
     * @throws CannotBuildTrainRouteException
     */
    public static TrainRoute newTrainRoute(InputStream inputStream, String columnDelimiter) throws CannotBuildTrainRouteException {
        return new TrainRouteGraphImpl.Builder()
                .withColumnDelimiter(columnDelimiter)
                .withInputStream(inputStream)
                .build();
    }

    /**
     * Get a singleton instance of ShortestPath calculator
     * for the entire application
     * @return instance of ShortestPathCalculator
     */
    synchronized public static ShortestPathCalculator getShortestPathCalculator() {
        if(shortestPathCalculator == null) {
            shortestPathCalculator = new ShortestPathCalculatorDijkstraImpl();
        }

        return shortestPathCalculator;
    }

    /**
     * returns singleton instance of LRUCache
     * for the entire application.
     * The LRU cache stores
     * key : station name that is the origin
     * value : map of the shortest distance to
     * all stations in the route, with respect
     * to the origin (station in the key)
     * @param capacity
     * @return
     */
    synchronized public static LRUCache<String, Map<String, Integer>> getLRUCache(int capacity) {
        if(lruCache == null) {
            lruCache = new LRUCacheLinkedHashMapImpl(capacity);
        }

        return lruCache;
    }

}
