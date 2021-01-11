package com.ethic.trainguide;

import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import com.ethic.trainguide.exception.NoSuchRouteException;
import com.ethic.trainguide.factory.TrainGuideFactory;
import com.ethic.trainguide.graph.TrainRouteBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * class for printing options/questions on command line interface,
 * taking user input and printing results
 */
public class TrainGuideCliInterface {

    private static String MAIN_MENU_RESOURCE_FILE = "main_menu.txt";
    private static String MENU_DISTANCE_ALONG_ROUTE_RESOURCE_FILE = "menu_distance_along_route.txt";
    private static String MENU_NUMBER_OF_ROUTES_BY_STOPS_RESOURCE_FILE = "menu_number_of_routes_by_stops.txt";
    private static String MENU_NUMBER_OF_ROUTES_BY_DISTANCE_RESOURCE_FILE = "menu_number_of_routes_by_distance.txt";
    private static String MENU_SHORTEST_ROUTE_RESOURCE_FILE = "shortest_route.txt";

    private String graphDataFileName;
    private String columnDelimiter;
    private TrainRoute trainRoute;
    private Scanner scanner;


    public TrainGuideCliInterface(String graphDataFileName, String columnDelimiter) {
        if(StringUtils.isEmpty(graphDataFileName) || StringUtils.isEmpty(columnDelimiter)) {
            throw new IllegalArgumentException("graphDataFileName/columnDelimiter must not be null");
        }

        this.graphDataFileName = graphDataFileName;
        this.columnDelimiter = columnDelimiter;
        this.scanner = new Scanner(System.in);
    }

    private String getMenuText(String resourceFileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceFileName);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

    }

    private void loadGraphDataFile() {

        try {
            TrainRouteBuilder trainRouteBuilder = TrainGuideFactory.newTrainRouteFromInputStreamBuilder(
                    new FileInputStream(graphDataFileName), columnDelimiter);

            trainRoute = trainRouteBuilder.build();

        } catch (IOException e) {
            System.out.println("Error opening input file : " + e.getMessage());
            System.exit(-1);
        } catch (CannotBuildTrainRouteException e) {
            System.out.println("Error processing input file : " + e.getMessage());
            System.exit(-1);
        }
    }

    public void run() {

        // read the input file and prepare train route graph object
        // to answer the questions
        loadGraphDataFile();

        // print the main menu and process the selections
        String menuText = getMenuText(MAIN_MENU_RESOURCE_FILE);

        while(true) {
            System.out.println(menuText);
            String optionStr = scanner.nextLine();
            int option = 0;
            try {
                option = Integer.parseInt(optionStr);
            } catch (NumberFormatException ex) {
                System.out.println("Invalid option, please try again.");
                continue;
            }

            processSelectedOption(option);
        }
    }

    private void processDistanceAlongRoute() {

        String menuText = getMenuText(MENU_DISTANCE_ALONG_ROUTE_RESOURCE_FILE);
        System.out.println(menuText);
        List<String> route =
                Arrays.stream(scanner.nextLine().split(","))
                .map(s -> s.trim())
                .collect(Collectors.toList());

        String output = "";
        try {
            output = new Integer(trainRoute.getDistanceOfRoute(route)).toString();
        } catch (NoSuchRouteException e) {
            output = "NO SUCH ROUTE";
        }

        System.out.println(output);

    }

    private void processRoutesByStops() {

        String menuText = getMenuText(MENU_NUMBER_OF_ROUTES_BY_STOPS_RESOURCE_FILE);
        System.out.println(menuText);

        String chunks[] = scanner.nextLine().split(",");
        if(chunks.length < 3) {
            System.out.println(String.format("Expected 3 parameters, got %d.  Try agian (y/n)?", chunks.length));
            if(scanner.nextLine() == "y");
            return;
        }

        String output = "";
        try {
            output = new Integer(trainRoute.getDistanceOfRoute(route)).toString();
        } catch (NoSuchRouteException e) {
            output = "NO SUCH ROUTE";
        }

        System.out.println(output);

    }

    private void processSelectedOption(int option) {

        switch (option) {
            case 1:
                processDistanceAlongRoute();
                break;
            case 2:
                processRoutesByStops();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }
    }


}
