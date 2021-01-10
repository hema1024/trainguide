package com.ethic.trainguide;

import com.ethic.trainguide.domain.Distance;
import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.graph.TrainRouteGraphImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TrainGuideTestBase {

    protected TrainRoute getAValidTrainRoute() {
        TrainRoute trainRoute = new TrainRouteGraphImpl();

        Station A = new Station("A");
        Station B = new Station("B");
        Station C = new Station("C");
        Station D = new Station("D");
        Station E = new Station("E");

        A.addAdjacentStation(B, new Distance(5));
        A.addAdjacentStation(D, new Distance(5));
        A.addAdjacentStation(E, new Distance(7));

        B.addAdjacentStation(C, new Distance(4));

        C.addAdjacentStation(D, new Distance(8));
        C.addAdjacentStation(E, new Distance(2));

        D.addAdjacentStation(C, new Distance(8));
        D.addAdjacentStation(E, new Distance(6));

        E.addAdjacentStation(B, new Distance(3));

        trainRoute.addStation(A);
        trainRoute.addStation(B);
        trainRoute.addStation(C);
        trainRoute.addStation(D);
        trainRoute.addStation(E);

        return trainRoute;

    }

    protected TrainRoute getAnIValidTrainRoute() {
        TrainRoute trainRoute = new TrainRouteGraphImpl();

        Station A = new Station("A");
        Station B = new Station("B");
        Station C = new Station("C");
        Station D = new Station("D");
        Station E = new Station("E");

        A.addAdjacentStation(B, new Distance(-5));
        A.addAdjacentStation(D, new Distance(5));
        A.addAdjacentStation(E, new Distance(-7));

        B.addAdjacentStation(C, new Distance(4));

        C.addAdjacentStation(D, new Distance(8));
        C.addAdjacentStation(E, new Distance(2));

        D.addAdjacentStation(C, new Distance(-8));
        D.addAdjacentStation(E, new Distance(6));

        E.addAdjacentStation(B, new Distance(-3));

        trainRoute.addStation(A);
        trainRoute.addStation(B);
        trainRoute.addStation(C);
        trainRoute.addStation(D);
        trainRoute.addStation(E);

        return trainRoute;

    }

    protected InputStream asInputStream(TrainRoute trainRoute) {
        return new ByteArrayInputStream(trainRoute.toString().getBytes());

    }

}
