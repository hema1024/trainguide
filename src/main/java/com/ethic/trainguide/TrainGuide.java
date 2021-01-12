package com.ethic.trainguide;

import org.apache.commons.cli.*;

/**
 * Main entry point of the application
 *
 */
public class TrainGuide {

    /**
     * class to print usage and parse command line arguments
     */
    private static class CommandLineParser {
        private static CommandLine commandLine;
        private static final String OPTION_ROUTE_GRAPH_FILE = "routeGraphFile";
        private static final String OPTION_DELIMITER = "delimiter";
        private static final String DEFAULT_DELIMITER = ",";
        private static Options options;

        static {
            options = getOptions();
        }

        /**
         * Get all command line options supported by this application
         * @return
         */
        private static Options getOptions() {
            if (options != null) {
                return options;
            }

            Options options = new Options();

            // add the command line options
            options.addOption(OPTION_ROUTE_GRAPH_FILE, true, "Path to the file containing train routes in graph format.  There should be one line per route, with 3 delimited columns.  Expected format per line : origin<DELIM>destination<DELIM>distance. (mandatory)");
            options.addOption(OPTION_DELIMITER, true, "Delimiter that seprates the 3 columns in the input file (optional, default comma)");

            return options;
        }

        /**
         * Parse arguments passed in the command line
         * and validate
         * @param args
         * @throws ParseException
         */
        private static void parseArguments(String[] args) throws ParseException {
            commandLine = new DefaultParser().parse(options, args);

            if (!commandLine.hasOption(OPTION_ROUTE_GRAPH_FILE)) {
                throw new IllegalArgumentException(String.format("%s is required", OPTION_ROUTE_GRAPH_FILE));
            }
        }

        private static String getOptionRouteGraphFile() {
            return commandLine.getOptionValue(OPTION_ROUTE_GRAPH_FILE);
        }

        private static String getDelimiter() {
            if(commandLine.hasOption(OPTION_DELIMITER)) {
                return commandLine.getOptionValue(OPTION_DELIMITER);
            } else {
                return DEFAULT_DELIMITER;
            }
        }

    }

    /**
     * Entry point
     * @param args
     */
    public static void main(String[] args) {

        try {
            CommandLineParser.parseArguments(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(TrainGuide.class.toString(), CommandLineParser.getOptions());
            System.exit(-1);
        }

        TrainGuideCliInterface cliInterface = new TrainGuideCliInterface(
                CommandLineParser.getOptionRouteGraphFile(),
                CommandLineParser.getDelimiter());

        cliInterface.run();
    }

}
