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
    public static final Integer WIN_POINTS = 3;
    public static final Integer DRAW_POINTS = 1;
    public static final Integer LOSS_POINTS = 0;

    /**
     * This method creates and sorts in descending order, a league table from a list of game scores.
     * If teams have the same number of points, they are ordered alphabetically in the table.
     * @param gameScorelist List of game scores
     * @return Sorted linked hash map representing the league table, complete with league points.
     */
    public LinkedHashMap<String, Integer> generateLeagueTable(final List<GameScore> gameScorelist) {
        final Map<String, Integer> scoreTable = createPointsTable(gameScorelist);

        // Now that we have an unsorted points table, we need to sort it!
        // First sort by points (ascending value), reverse the order, and then sort by team name alphabetically if the
        // teams have the same points. Then push sorted entries one at a time into a LinkedHashMap that preserves entry order.
        return scoreTable.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue, Comparator.reverseOrder()).thenComparing(Map.Entry::getKey))
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    }

    /**
     * This method generates an unsorted points table by assigning points to teams based on performance.
     * It assigns a team 3 points for a win, 1 point for a draw and 0 points for a loss.
     * @param gameScoreList List of game scores
     * @return Unsorted map representing the points table.
     */
    public Map<String, Integer> createPointsTable(final List<GameScore> gameScoreList) {
        final Map<String, Integer> pointsTable = new HashMap<>();

        gameScoreList.forEach(entry -> {
            if (entry.first().teamScore().compareTo(entry.second().teamScore()) > 0) {
                updateTeamPoints(pointsTable, entry.first().teamName(), WIN_POINTS);
                updateTeamPoints(pointsTable, entry.second().teamName(), LOSS_POINTS);
            } else if (entry.first().teamScore().compareTo(entry.second().teamScore()) < 0) {
                updateTeamPoints(pointsTable, entry.first().teamName(), WIN_POINTS);
                updateTeamPoints(pointsTable, entry.second().teamName(), LOSS_POINTS);
            } else {
                updateTeamPoints(pointsTable, entry.first().teamName(), DRAW_POINTS);
                updateTeamPoints(pointsTable, entry.second().teamName(), DRAW_POINTS);
            }
        });

        return pointsTable;
    }

    /**
     * This method updates a team's points in a points table.
     * @param pointsTable The points table to be modified.
     * @param teamName The name of the team whose points are being added.
     * @param addedPoints The number of points being added to the team's existing points.
     */
    public void updateTeamPoints(final Map<String, Integer> pointsTable, final String teamName, final Integer addedPoints) {
        if (!pointsTable.containsKey(teamName)) {
            pointsTable.put(teamName, 0);
        }

        final Integer currentPoints = pointsTable.get(teamName);
        pointsTable.replace(teamName, currentPoints + addedPoints);
    }
}
