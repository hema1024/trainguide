package com.ethic.trainguide.calculate;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.graph.TrainRoute;

public interface ShortestPath {

    /**
     *
     * @param trainRoute trainRoute information will stations,
     *                   adjacent stations and distance between the stations
     * @param origin origin station from which the shortest path to
     *               other stations must be computed
     * @return
     */
    public void setShortestPathFromOrigin(TrainRoute trainRoute, Station origin);

    /**
     *
     * @param trainRoute trainRoute information will stations,
     *                   adjacent stations and distance between the stations
     * @param originName origin station name from which the shortest path to
     *               other stations must be computed
     * @return
     */
    public void setShortestPathFromOrigin(TrainRoute trainRoute, String originName);

}
