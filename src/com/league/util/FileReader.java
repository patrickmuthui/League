package com.league.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import static java.lang.String.format;

public class FileReader {

    private final String filePath;

    public FileReader(final String filePath) {
        this.filePath = filePath;
    }

    public List<GameScore> parseGameScores() {
        final List<GameScore> gameScores = new ArrayList<>();

        try {
            // Create file object, open it and parse it
            File scoresFile = new File(filePath);

            Scanner scanner = new Scanner(scoresFile);

            while (scanner.hasNextLine()){
                String gameScoreLine = scanner.nextLine();
                gameScores.add(parseGameScore(gameScoreLine));
            }
            scanner.close();

            return gameScores;
        }
        catch (Exception e) {
            throw new RuntimeException(String.format("Error opening and reading input file: %s", filePath), e);
        }
    }

    private GameScore parseGameScore(final String gameScoreLine) {
        String[] gameScoreEntries = gameScoreLine.split(", ");
        if (gameScoreEntries.length != 2){
            throw new RuntimeException(format("Invalid game score line entry: %s", gameScoreLine));
        }

        List<TeamScore> teamScores = new ArrayList<>();
        Arrays.stream(gameScoreEntries).forEach(entry -> teamScores.add(parseTeamScore(entry)));

        return new GameScore(teamScores.getFirst(), teamScores.getLast());
    }

    private TeamScore parseTeamScore(final String teamScoreLine) {
        final String[] teamScoreEntries = teamScoreLine.split(" ", 0);

        // Validate that the last element is a number and concatenate other elements to a team name
        try {
            final String teamScore = Arrays.stream(teamScoreEntries).toList().getLast();
            final String teamName = teamScoreLine.substring(0, teamScoreLine.lastIndexOf(" " + teamScore));
            return new TeamScore(teamName, Integer.parseInt(teamScore));
        } catch (Exception e) {
            throw new RuntimeException(format("Invalid team score line: %s", teamScoreLine), e);
        }
    }
}
