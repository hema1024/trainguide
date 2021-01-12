package com.ethic.trainguide;

import com.ethic.trainguide.cache.LRUCache;
import com.ethic.trainguide.graph.ShortestPathCalculator;
import com.ethic.trainguide.domain.TrainRoute;
import com.ethic.trainguide.exception.CannotBuildTrainRouteException;
import com.ethic.trainguide.exception.NoSuchRouteException;
import com.ethic.trainguide.factory.TrainGuideFactory;
import com.ethic.trainguide.graph.TrainRouteGraphImpl;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    private static String OUTPUT_PREFIX = "ANSWER : ";
    private static int LRU_CACHE_CAPACITY = 50;

    private String graphDataFileName;
    private String columnDelimiter;
    private TrainRoute trainRoute;
    private LRUCache<String, Map<String, Integer>> lruCache;
    private Scanner scanner;
    private ShortestPathCalculator shortestPathCalculator;


    public TrainGuideCliInterface(String graphDataFileName, String columnDelimiter) {
        if(StringUtils.isEmpty(graphDataFileName) || StringUtils.isEmpty(columnDelimiter)) {
            throw new IllegalArgumentException("graphDataFileName/columnDelimiter must not be null");
        }

        this.graphDataFileName = graphDataFileName;
        this.columnDelimiter = columnDelimiter;
        this.scanner = new Scanner(System.in);
        this.lruCache = TrainGuideFactory.getLRUCache(LRU_CACHE_CAPACITY);
        this.shortestPathCalculator = TrainGuideFactory.getShortestPathCalculator();
    }

    private String getMenuText(String resourceFileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceFileName);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

    }

    private TrainRoute newTrainRouteFromGraphDataFile() {

        try {
            return TrainGuideFactory.newTrainRoute(
                    new FileInputStream(graphDataFileName),
                    columnDelimiter);

        } catch (IOException e) {
            System.out.println("Error opening input file : " + e.getMessage());
            System.exit(-1);
        } catch (CannotBuildTrainRouteException e) {
            System.out.println("Error processing input file : " + e.getMessage());
            System.exit(-1);
        } catch (Exception e) {
            System.out.println("ERROR : " + e.getMessage());
            System.exit(-1);
        }

        return null;
    }

    public void run() {

        // read the input file and prepare train route graph object
        // to answer the questions
        trainRoute = newTrainRouteFromGraphDataFile();

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

        String chunks[] = new String[]{};
        String userInput = "y";
        while(userInput.equalsIgnoreCase("y")) {
            System.out.println(menuText);
            chunks = scanner.nextLine().split(",");
            if(chunks.length <= 1) {
                System.out.println(String.format("Expected at least 2 towns/stations, got %d.  Try agian (y/n)?", chunks.length));
                userInput = scanner.nextLine();
            } else {
                userInput = "n";
            }
        }

        List<String> route =
                Arrays.stream(chunks)
                .map(s -> s.trim())
                .collect(Collectors.toList());

        String output = OUTPUT_PREFIX;
        try {
            output += new Integer(trainRoute.getDistanceOfRoute(route)).toString();
        } catch (NoSuchRouteException e) {
            output += "NO SUCH ROUTE";
        }

        System.out.println(output);

        pressEnterToContinue();

    }

    private void processRoutesByStops() {

        String menuText = getMenuText(MENU_NUMBER_OF_ROUTES_BY_STOPS_RESOURCE_FILE);

        String chunks[] = new String[]{};
        String userInput = "y";
        int maxNumStops = 0;
        while(userInput.equalsIgnoreCase("y")) {
            System.out.println(menuText);
            chunks = scanner.nextLine().split(",");
            if(chunks.length < 3) {
                System.out.println(String.format("Expected 3 parameters, got %d.  Try agian (y/n)?", chunks.length));
                userInput = scanner.nextLine();
            } else {
                try {
                    maxNumStops = Integer.parseInt(chunks[2].trim());
                    userInput = "n";
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid value for (3) max. no of stops.  Try agian (y/n)?");
                    userInput = scanner.nextLine();
                }
            }
        }

        String output = OUTPUT_PREFIX;

        try {
            output += Integer.toString(trainRoute.getRoutesByNumberOfStops(
                    chunks[0].trim(), chunks[1].trim(), maxNumStops).size());
        } catch (Exception e) {
            output += e.getMessage();
        }

        System.out.println(output);

        pressEnterToContinue();

    }

    private void processRoutesByDistance() {
        String menuText = getMenuText(MENU_NUMBER_OF_ROUTES_BY_DISTANCE_RESOURCE_FILE);

        String chunks[] = new String[]{};
        String userInput = "y";
        int maxDistance = 0;
        while(userInput.equalsIgnoreCase("y")) {
            System.out.println(menuText);
            chunks = scanner.nextLine().split(",");
            if(chunks.length < 3) {
                System.out.println(String.format("Expected 3 parameters, got %d.  Try agian (y/n)?", chunks.length));
                userInput = scanner.nextLine();
            } else {
                try {
                    maxDistance = Integer.parseInt(chunks[2].trim());
                    userInput = "n";
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid value for (3) max. distance.  Try agian (y/n)?");
                    userInput = scanner.nextLine();
                }
            }
        }

        String output = OUTPUT_PREFIX;

        try {
            output += Integer.toString(trainRoute.getRoutesByDistance(
                    chunks[0].trim(), chunks[1].trim(), maxDistance).size());
        } catch (Exception e) {
            output += e.getMessage();
        }

        System.out.println(output);

        pressEnterToContinue();
    }

    private void processShortestDistance() {
        String menuText = getMenuText(MENU_SHORTEST_ROUTE_RESOURCE_FILE);

        String chunks[] = new String[]{};
        String userInput = "y";
        int maxDistance = 0;
        while(userInput.equalsIgnoreCase("y")) {
            System.out.println(menuText);
            chunks = scanner.nextLine().split(",");
            if(chunks.length != 2) {
                System.out.println(String.format("Expected 2 parameters, got %d.  Try agian (y/n)?", chunks.length));
                userInput = scanner.nextLine();
            } else {
                userInput = "n";
            }
        }

        String output = OUTPUT_PREFIX;
        String origin = chunks[0].trim(), destination = chunks[1];

        // check in cache if we have shortest route calculated
        // for this origin station before
        Map<String, Integer> shortestDistanceMap = lruCache.getItem(origin);

        try {
            if(shortestDistanceMap == null) {
                shortestDistanceMap = shortestPathCalculator.getShortestPathFromOrigin(trainRoute, origin);
                lruCache.putItem(origin, shortestDistanceMap);
            }
            Integer dist = shortestDistanceMap.get(destination);
            output += (dist == null) ? "NO SUCH ROUTE" : dist.toString();
        } catch (Exception e) {
            output += e.getMessage();
        }

        System.out.println(output);

        pressEnterToContinue();
    }

    private void pressEnterToContinue() {
        System.out.println("Press enter to continue...");
        scanner.nextLine();
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
                processRoutesByDistance();
                break;
            case 4:
                processShortestDistance();
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
