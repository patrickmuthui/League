package com.league.core;

import com.league.util.GameScore;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that processes a list of validated game scores
 * to produce a league table based on teams' performances
 * in head-to-head matches.
 */
public class GameScoresProcessor {
    private static final Integer WIN_POINTS = 3;
    private static final Integer DRAW_POINTS = 1;
    private static final Integer LOSS_POINTS = 0;

    /**
     * This method creates and sorts in descending order, a league table from a list of game scores.
     * If teams have the same number of points, they are ordered alphabetically in the table.
     * @param gameScorelist List of game scores
     * @return Sorted linked hash map representing the league table, complete with league points.
     */
    public LinkedHashMap<String, Integer> generateLeagueTable(final List<GameScore> gameScorelist) {
        final Map<String, Integer> scoreTable = createScoreTable(gameScorelist);

        // Now that we have an unsorted points table, we need to sort it!
        // First sort by points (ascending value), reverse the order, and then sort by team name alphabetically if the
        // teams have the same points. Then push sorted entries one at a time into a LinkedHashMap that preserves entry order.
        return scoreTable.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue, Comparator.reverseOrder()).thenComparing(Map.Entry::getKey))
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    }

    /**
     * This method generates an unsorted score table by assigning points to teams based on performance.
     * It assigns a team 3 points for a win, 1 point for a draw and 0 points for a loss.
     * @param gameScoreList List of game scores
     * @return Unsorted map representing the score table.
     */
    private Map<String, Integer> createScoreTable(final List<GameScore> gameScoreList) {
        final Map<String, Integer> scoreTable = new HashMap<>();

        gameScoreList.forEach(entry -> {
            if (entry.first().teamScore().compareTo(entry.second().teamScore()) > 0) {
                updateTeamPoints(scoreTable, entry.first().teamName(), WIN_POINTS);
                updateTeamPoints(scoreTable, entry.second().teamName(), LOSS_POINTS);
            } else if (entry.first().teamScore().compareTo(entry.second().teamScore()) < 0) {
                updateTeamPoints(scoreTable, entry.first().teamName(), WIN_POINTS);
                updateTeamPoints(scoreTable, entry.second().teamName(), LOSS_POINTS);
            } else {
                updateTeamPoints(scoreTable, entry.first().teamName(), DRAW_POINTS);
                updateTeamPoints(scoreTable, entry.second().teamName(), DRAW_POINTS);
            }
        });

        return scoreTable;
    }

    /**
     * This method updates a team's points in a score table.
     * @param scoreTable The score table to be modified.
     * @param teamName The name of the team whose points are being added.
     * @param addedPoints The number of points being added to the team's existing points.
     */
    private void updateTeamPoints(final Map<String, Integer> scoreTable, final String teamName, final Integer addedPoints) {
        if (!scoreTable.containsKey(teamName)) {
            scoreTable.put(teamName, 0);
        }

        final Integer currentPoints = scoreTable.get(teamName);
        scoreTable.replace(teamName, currentPoints + addedPoints);
    }
}
