package com.league.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.Scanner;

import static java.lang.String.format;

/**
 * Class opens, reads, and validates the contents of a file containing game score entries in a specific format.
 * It then returns a validated list of game scores with embedded teams scores, for further processing.
 */
public class FileReader {

    private static final Logger log = Logger.getLogger(String.valueOf(FileReader.class));
    private final File scoresFile;
    private final Scanner fileScanner;

    /**
     * Parameterised constructor that validates the file's location and access to it.
     * @param filePath Path o the input file
     */
    public FileReader(final String filePath) throws FileNotFoundException {
        try{
            this.scoresFile = new File(filePath);
            this.fileScanner = new Scanner(scoresFile);
        }
        catch (Exception e) {
            log.severe(String.format("Error opening input file: %s", filePath));
            throw e;
        }
    }

    /**
     * This method parses the file to retrieve game score, validating each game scoreline as it is parsed.
     * @return A list of validated game scores.
     */
    public List<GameScore> parseGameScores() {
        final List<GameScore> gameScores = new ArrayList<>();

        try {
            while (fileScanner.hasNextLine()){
                String gameScoreline = fileScanner.nextLine();
                gameScores.add(validateGameScore(gameScoreline));
            }
            fileScanner.close();

            return gameScores;
        }
        catch (Exception e) {
            log.severe(String.format("Error parsing input file: %s", scoresFile.getPath()));
            throw e;
        }
    }

    /**
     * This method validates a single game scoreline.
     * @param gameScoreline The game scoreline to be validated.
     * @return A validated game scoreline object.
     */
    public GameScore validateGameScore(final String gameScoreline) {
        String[] gameScoreEntries = gameScoreline.split(",");
        if (gameScoreEntries.length != 2){
            throw new RuntimeException(format("Invalid game scoreline entry: %s", gameScoreline));
        }

        List<TeamScore> teamScores = new ArrayList<>();
        Arrays.stream(gameScoreEntries).forEach(entry -> teamScores.add(validateTeamScore(entry.trim())));

        // A team cannot play itself. Throw if we detect this!
        if (teamScores.getFirst().teamName().equals(teamScores.getLast().teamName())) {
            throw new RuntimeException(format("Invalid game scoreline entry: %s", gameScoreline));
        }

        return new GameScore(teamScores.getFirst(), teamScores.getLast());
    }

    /**
     * This method validates a single team scoreline.
     * @param teamScoreline The team scoreline to be validated.
     * @return A validated team scoreline object.
     */
    public TeamScore validateTeamScore(final String teamScoreline) {
        final String[] teamScoreEntries = teamScoreline.split(" ", 0);

        // Validate that the last element is a number and concatenate other elements to a team name
        try {
            final String teamScore = Arrays.stream(teamScoreEntries).toList().getLast();
            final String teamName = teamScoreline.substring(0, teamScoreline.lastIndexOf(" " + teamScore));
            return new TeamScore(teamName, Integer.parseInt(teamScore));
        } catch (Exception e) {
            throw new RuntimeException(format("Invalid team scoreline: %s", teamScoreline), e);
        }
    }
}
