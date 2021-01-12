package com.ethic.trainguide;

import com.ethic.trainguide.factory.TrainGuideFactory;
import org.apache.commons.cli.*;

/**
 * Main entry point of the application
 *
 */
public class TrainGuide {

    /**
     * class to print usage and parse command line arguments
     */
    public static class CommandLineParser {
        private static CommandLine commandLine;
        public static final String OPTION_ROUTE_GRAPH_FILE = "routeGraphFile";
        public static final String OPTION_DELIMITER = "delimiter";
        public static final String OPTION_HELP = "help";
        public static final String DEFAULT_DELIMITER = ",";
        private static final Options options;

        static {
            options = getOptions();
        }

        /**
         * Get all command line options supported by this application
         * @return
         */
        public static Options getOptions() {
            if (options != null) {
                return options;
            }

            Options options = new Options();

            // add the command line options
            options.addOption(OPTION_ROUTE_GRAPH_FILE, true, "Path to the file containing train routes in graph format.  There should be one line per route, with 3 delimited columns.  Expected format per line : origin<DELIM>destination<DELIM>distance. (mandatory)");
            options.addOption(OPTION_DELIMITER, true, "Delimiter that seprates the 3 columns in the input file (optional, default comma)");
            options.addOption(OPTION_HELP, false, "Display this help");

            return options;
        }

        /**
         * Parse arguments passed in the command line
         * and validate
         * @param args
         * @throws ParseException
         */
        public static void parseArguments(String[] args) throws ParseException {
            commandLine = new DefaultParser().parse(options, args);
        }

        public static void validateArguments() {
            if (!commandLine.hasOption(OPTION_ROUTE_GRAPH_FILE)) {
                throw new IllegalArgumentException(String.format("%s is required", OPTION_ROUTE_GRAPH_FILE));
            }
        }

        public static String getOptionRouteGraphFile() {
            return commandLine.getOptionValue(OPTION_ROUTE_GRAPH_FILE);
        }

        public static boolean hasOptionHelp() {
            return commandLine.hasOption(OPTION_HELP);
        }

        public static String getDelimiter() {
            if(commandLine.hasOption(OPTION_DELIMITER)) {
                return commandLine.getOptionValue(OPTION_DELIMITER);
            } else {
                return DEFAULT_DELIMITER;
            }
        }

    }

    public static void printUsage(int exitStatus) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(TrainGuide.class.toString(), CommandLineParser.getOptions());
        System.exit(exitStatus);
    }

    /**
     * Entry point
     * @param args
     */
    public static void main(String[] args) {

        try {
            CommandLineParser.parseArguments(args);
            if (CommandLineParser.hasOptionHelp()) {
                printUsage(0);
            }

            CommandLineParser.validateArguments();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            printUsage(-1);
        }

        TrainGuideCli cliInterface = TrainGuideFactory.getTrainGuideCli(
                CommandLineParser.getOptionRouteGraphFile(),
                CommandLineParser.getDelimiter());

        cliInterface.run();
    }

}
