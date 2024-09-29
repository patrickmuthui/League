package com.league.core;

import com.league.util.GameScore;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class GameScoresProcessor {
    private static final Integer WIN_POINTS = 3;
    private static final Integer DRAW_POINTS = 1;
    private static final Integer LOSS_POINTS = 0;

    public LinkedHashMap<String, Integer> generateLeagueTable(final List<GameScore> gameScorelist) {
        final Map<String, Integer> scoreTable = createScoreTable(gameScorelist);

        // Now that we have an unsorted points table, we need to sort it!
        // First sort by points (ascending value), reverse the order, and then sort by team name alphabetically if the
        // teams have the same points. Then push sorted entries one at a time into a LinkedHashMap that preserves entry order.
        return scoreTable.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue, Comparator.reverseOrder()).thenComparing(Map.Entry::getKey))
                .collect(LinkedHashMap::new, (map, entry) -> map.put(entry.getKey(), entry.getValue()), Map::putAll);
    }

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

    private void updateTeamPoints(final Map<String, Integer> scoreTable, final String teamName, final Integer addedPoints) {
        if (!scoreTable.containsKey(teamName)) {
            scoreTable.put(teamName, 0);
        }

        final Integer currentPoints = scoreTable.get(teamName);
        scoreTable.replace(teamName, currentPoints + addedPoints);
    }
}
