package com.league.core;

import com.league.util.FileReader;
import com.league.util.GameScore;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class of the League App.
 */
public class Main {

    /**
     * Application entry-point.
     * @param args List of arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Validate input arguments
        if (args.length != 1){
            throw new RuntimeException("Usage: You must pass in only a single argument, as the path to the game score file");
        }

        // Access the game score file, parse and validate its contents
        final FileReader fileReader = new FileReader(args[0]);
        final List<GameScore> gameScorelist = fileReader.parseGameScores();

        // Proceed to process the validated game score list to produce a league table
        final GameScoresProcessor processor = new GameScoresProcessor();
        final LinkedHashMap<String, Integer> leagueTable = processor.generateLeagueTable(gameScorelist);

        // Print out the league table
        Integer position = 1;
        for (Map.Entry<String, Integer> entry : leagueTable.entrySet()) {
            System.out.printf("%o. %s, %o pts%n", position, entry.getKey(), entry.getValue());
            position++;
        }
    }
}
