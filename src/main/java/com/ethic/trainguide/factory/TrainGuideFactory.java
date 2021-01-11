package com.ethic.trainguide.factory;

import com.ethic.trainguide.cache.LRUCache;
import com.ethic.trainguide.cache.LRUCacheLinkedHashMapImpl;
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
    private static LRUCache<String, TrainRoute> lruCache = null;

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
    synchronized public static ShortestPath getShortestPath() {
        if(shortestPath == null) {
            shortestPath = new ShortestPathDijkstraImpl();
        }

        return shortestPath;
    }

    /**
     * returns singleton instance of LRUCache
     * for the entire application
     * @param capacity
     * @return
     */
    synchronized public static LRUCache<String, TrainRoute> getLRUCache(int capacity) {
        if(lruCache == null) {
            lruCache = new LRUCacheLinkedHashMapImpl<String, TrainRoute>(capacity);
        }

        return lruCache;
    }

}
