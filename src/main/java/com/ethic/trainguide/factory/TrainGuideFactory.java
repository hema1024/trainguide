package com.ethic.trainguide.factory;

import com.ethic.trainguide.cache.LRUCache;
import com.ethic.trainguide.cache.LRUCacheLinkedHashMapImpl;
import com.ethic.trainguide.calculate.ShortestPathCalculator;
import com.ethic.trainguide.calculate.ShortestPathCalculatorDijkstraImpl;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.graph.TrainRouteBuilder;
import com.ethic.trainguide.graph.TrainRouteFromInputStreamBuilderImpl;
import com.ethic.trainguide.graph.TrainRouteGraphImpl;

import java.io.InputStream;
import java.util.Map;

/**
 * Factory class to provide instances of TrainRoute and
 * TrainRouteBuilder implmentations
 */
public class TrainGuideFactory {

    private static ShortestPathCalculator shortestPathCalculator = null;
    private static LRUCache<String, Map<String, Integer>> lruCache = null;

    public static TrainRouteBuilder newTrainRouteFromInputStreamBuilder(InputStream inputStream, String columnDelimiter) {
        return new TrainRouteFromInputStreamBuilderImpl(inputStream, columnDelimiter);
    }

    public static TrainRoute newTrainRoute() {
        return new TrainRouteGraphImpl();
    }

    /**
     * returns singleton instance of ShortestPath calculator
     * for the entire application
     * @return
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
