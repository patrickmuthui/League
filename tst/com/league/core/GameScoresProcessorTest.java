package com.league.core;

import com.league.util.GameScore;
import com.league.util.TeamScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


class GameScoresProcessorTest {

    private GameScoresProcessor genericProcessor;

    private List<GameScore> genericGameScorelist;

    private static final LinkedHashMap<String, Integer> genericExpectedLeagueTable = new LinkedHashMap<>();

    @BeforeEach
    void setUp() {
        genericProcessor = new GameScoresProcessor();
        genericGameScorelist = List.of(
                new GameScore(new TeamScore("Lions", 3), new TeamScore("Snakes", 3)),
                new GameScore(new TeamScore("Tarantulas", 1), new TeamScore("FC Awesome", 0)),
                new GameScore(new TeamScore("Lions", 1), new TeamScore("FC Awesome", 1)),
                new GameScore(new TeamScore("Tarantulas", 3), new TeamScore("Snakes", 1)),
                new GameScore(new TeamScore("Lions", 4), new TeamScore("Grouches", 0))
        );
        genericExpectedLeagueTable.clear();
        genericExpectedLeagueTable.put("Tarantulas", 6);
        genericExpectedLeagueTable.put("Lions", 5);
        genericExpectedLeagueTable.put("FC Awesome", 1);
        genericExpectedLeagueTable.put("Snakes", 1);
        genericExpectedLeagueTable.put("Grouches", 0);
    }

    @Test
    void WhenGenericGameScorelistSupplied_ThenExpectedLeagueTableProduced() {
        final Map<String, Integer> leagueTable = genericProcessor.generateLeagueTable(genericGameScorelist);

        assertEquals(genericExpectedLeagueTable.size(), leagueTable.size());
        assertEquals(genericExpectedLeagueTable, leagueTable);
        assertIterableEquals(Arrays.asList(genericExpectedLeagueTable.keySet().toArray()), Arrays.asList(leagueTable.keySet().toArray()));
    }
}