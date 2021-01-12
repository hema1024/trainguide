package com.ethic.trainguide.calculate;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.NoSuchStationException;

import java.util.Map;

public interface ShortestPathCalculator {

    /**
     *
     * @param trainRoute trainRoute information will stations,
     *                   adjacent stations and distance between the stations
     * @param origin origin station from which the shortest path to
     *               other stations must be computed
     * @return
     */
    public Map<String, Integer> getShortestPathFromOrigin(TrainRoute trainRoute, String originName) throws NoSuchStationException;

}
