package com.ethic.trainguide;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CommandLineParserTest extends TrainGuideTestBase {

    @Test
    public void testGetOptions() {
        assertTrue(TrainGuide.CommandLineParser.getOptions().hasOption(TrainGuide.CommandLineParser.OPTION_ROUTE_GRAPH_FILE));
        assertTrue(TrainGuide.CommandLineParser.getOptions().hasOption(TrainGuide.CommandLineParser.OPTION_DELIMITER));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseArguments_WhenGraphFileIsNotProvided() throws ParseException {
        String[] args = new String[] {
                String.format("-")

        };

        TrainGuide.CommandLineParser.parseArguments(args);
    }

    @Test
    public void testParseArguments_WhenGraphFileIsProvided() throws ParseException {
        String expectedFile = "somefile.txt";
        String[] args = new String[] {
                String.format("-%s", TrainGuide.CommandLineParser.OPTION_ROUTE_GRAPH_FILE),
                expectedFile
        };

        TrainGuide.CommandLineParser.parseArguments(args);
        TrainGuide.CommandLineParser.getOptionRouteGraphFile().equals(expectedFile);
    }

    @Test
    public void testParseArguments_WhenDelimiterIsProvided() throws ParseException {
        String expectedFile = "somefile.txt";
        String expectedDelimiter = "\t";
        String[] args = new String[] {
                String.format("-%s", TrainGuide.CommandLineParser.OPTION_ROUTE_GRAPH_FILE),
                expectedFile,
                String.format("-%s", TrainGuide.CommandLineParser.OPTION_DELIMITER),
                expectedDelimiter
        };

        TrainGuide.CommandLineParser.parseArguments(args);
        TrainGuide.CommandLineParser.getDelimiter().equals(expectedDelimiter);
    }

    @Test
    public void testParseArguments_ForDefaultDelimiter() throws ParseException {
        String expectedFile = "somefile.txt";
        String expectedDelimiter = TrainGuide.CommandLineParser.DEFAULT_DELIMITER;
        String[] args = new String[] {
                String.format("-%s", TrainGuide.CommandLineParser.OPTION_ROUTE_GRAPH_FILE),
                expectedFile
        };

        TrainGuide.CommandLineParser.parseArguments(args);
        TrainGuide.CommandLineParser.getDelimiter().equals(expectedDelimiter);
    }
}
