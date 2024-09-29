package com.league.core;

import com.league.util.FileReader;
import com.league.util.GameScore;

import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Validate input arguments
        if (args.length != 1){
            throw new RuntimeException("Usage: You must pass in one single argument");
        }

        final FileReader fileReader = new FileReader(args[0]);
        final List<GameScore> gameScoreList = fileReader.parseGameScores();

        final GameScoresProcessor processor = new GameScoresProcessor();
        final Map<String, Integer> leagueTable = processor.generateLeagueTable(gameScoreList);

        Integer position = 1;
        for (Map.Entry<String, Integer> entry : leagueTable.entrySet()) {
            System.out.printf("%o. %s, %o pts%n", position, entry.getKey(), entry.getValue());
            position++;
        }
    }
}
