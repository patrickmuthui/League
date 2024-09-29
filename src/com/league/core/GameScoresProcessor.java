package com.league.core;

import com.league.util.GameScore;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GameScoresProcessor {
    private static final Integer WIN_POINTS = 3;
    private static final Integer DRAW_POINTS = 1;
    private static final Integer LOSS_POINTS = 0;

    public Map<String, Integer> generateLeagueTable(final List<GameScore> gameScoreList) {
        final Map<String, Integer> scoreTable = createScoreTable(gameScoreList);
        return scoreTable.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, _) -> e1, LinkedHashMap::new
                ));
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
