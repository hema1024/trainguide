package com.ethic.trainguide;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.graph.TrainRouteGraphImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class TrainGuideTestBase {

    protected TrainRoute getAValidTrainRoute() {
        TrainRoute trainRoute = new TrainRouteGraphImpl();

        Station A = new Station("A");
        Station B = new Station("B");
        Station C = new Station("C");
        Station D = new Station("D");
        Station E = new Station("E");

        A.addAdjacentStation(B, 5);
        A.addAdjacentStation(D, 5);
        A.addAdjacentStation(E, 7);

        B.addAdjacentStation(C, 4);

        C.addAdjacentStation(D, 8);
        C.addAdjacentStation(E, 2);

        D.addAdjacentStation(C, 8);
        D.addAdjacentStation(E, 6);

        E.addAdjacentStation(B, 3);

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

        A.addAdjacentStation(B, -5);
        A.addAdjacentStation(D, 5);
        A.addAdjacentStation(E, -7);

        B.addAdjacentStation(C, 4);

        C.addAdjacentStation(D, 8);
        C.addAdjacentStation(E, 2);

        D.addAdjacentStation(C, -8);
        D.addAdjacentStation(E, 6);

        E.addAdjacentStation(B, -3);

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

    protected List<String> reformatListStringStringToListString(List<List<String>> listOfListOfStrings) {
        return listOfListOfStrings.stream()
                .map(s -> s.toString().replaceAll("[\\[\\], ]", ""))
                .collect(Collectors.toList());

    }

}
