package com.ethic.trainguide;

import com.ethic.trainguide.domain.Station;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import com.ethic.trainguide.graph.TrainRouteGraphImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TrainGuideTestBase {

    protected static final String GRAPH_RESOURCE_FILE_WITH_VALID_DATA = "test_data_valid.csv";
    protected static final String GRAPH_RESOURCE_FILE_WITH_NEGATIVE_DISTANCE = "test_data_invalid_negative_distance.csv";
    protected static final String GRAPH_RESOURCE_FILE_WITH_LESS_COLUMNS = "test_data_invalid_less_columns.csv";

    protected final List<Station> EXPECTED_STATIONS = Arrays.asList(
            new Station("A"),
            new Station("B"),
            new Station("C"),
            new Station("D"),
            new Station("E"));

    protected InputStream getResourceAsInputStream(String resourceFileName) {
        return getClass().getClassLoader().getResourceAsStream(resourceFileName);
    }

    protected TrainRoute getAValidTrainRoute() throws CannotBuildTrainRouteException {
        return new TrainRouteGraphImpl.Builder()
                .withColumnDelimiter(",")
                .withInputStream(getResourceAsInputStream(GRAPH_RESOURCE_FILE_WITH_VALID_DATA))
                .build();
    }

    protected TrainRoute getAnInValidTrainRoute() throws CannotBuildTrainRouteException {
        return new TrainRouteGraphImpl.Builder()
                .withColumnDelimiter(",")
                .withInputStream(getResourceAsInputStream(GRAPH_RESOURCE_FILE_WITH_LESS_COLUMNS))
                .build();
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
