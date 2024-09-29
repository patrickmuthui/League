package com.league.core;

import com.league.util.GameScore;
import com.league.util.TeamScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameScoresProcessorTest {

    private GameScoresProcessor genericProcessor;

    private List<GameScore> genericGameScorelist;

    private final LinkedHashMap<String, Integer> genericExpectedLeagueTable = new LinkedHashMap<>();

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
    void GivenGenericGameScorelist_WhenGeneratingLeagueTable_ThenExpectedLeagueTableProduced() {
        final Map<String, Integer> resultTable = genericProcessor.generateLeagueTable(genericGameScorelist);

        assertLeagueTableEntries(genericExpectedLeagueTable, resultTable);
    }

    @Test
    void GivenGenericGameScorelist_WhenCreatingScoreTable_ThenExpectedPointsTableProduced() {
        final Map<String, Integer> resultTable = genericProcessor.createPointsTable(genericGameScorelist);

        assertUnsortedScoreTableEntries(genericExpectedLeagueTable, resultTable);
    }

    @Test
    void GivenWinLossScore_WhenCreatingScoreTable_ThenExpectedPointsTableProduced() {
        final List<GameScore> winLossGameScoreList = List.of(
                new GameScore(new TeamScore("FirstTeam", 2), new TeamScore("SecondTeam", 1)));
        final Map<String, Integer> expectedScoreTable = Map.of("FirstTeam", 3, "SecondTeam", 0);

        final Map<String, Integer> resultTable = genericProcessor.createPointsTable(winLossGameScoreList);

        assertUnsortedScoreTableEntries(expectedScoreTable, resultTable);
    }

    @Test
    void GivenDrawScore_WhenCreatingScoreTable_ThenExpectedPointsTableProduced() {
        final List<GameScore> winLossGameScoreList = List.of(
                new GameScore(new TeamScore("FirstTeam", 4), new TeamScore("SecondTeam", 4)));
        final Map<String, Integer> expectedScoreTable = Map.of("FirstTeam", 1, "SecondTeam", 1);

        final Map<String, Integer> resultTable = genericProcessor.createPointsTable(winLossGameScoreList);

        assertUnsortedScoreTableEntries(expectedScoreTable, resultTable);
    }

    @Test
    void GivenExistingTeamWins_WhenUpdatingScoreTable_ThenExpectedUpdateIsMade() {
        final Map<String, Integer> existingPointsTable = getPointsTableWithSingleTeam();
        final String teamName = (String) existingPointsTable.keySet().toArray()[0];
        final Integer priorTeamPoints = existingPointsTable.get(teamName);

        genericProcessor.updateTeamPoints(existingPointsTable, teamName, GameScoresProcessor.WIN_POINTS);

        assertTrue(existingPointsTable.containsKey(teamName));
        assertEquals(priorTeamPoints + GameScoresProcessor.WIN_POINTS, existingPointsTable.get(teamName));
    }

    @Test
    void GivenExistingTeamLoses_WhenUpdatingPointsTable_ThenExpectedUpdateIsMade() {
        final Map<String, Integer> existingPointsTable = getPointsTableWithSingleTeam();
        final String teamName = (String) existingPointsTable.keySet().toArray()[0];
        final Integer priorTeamPoints = existingPointsTable.get(teamName);

        genericProcessor.updateTeamPoints(existingPointsTable, teamName, GameScoresProcessor.LOSS_POINTS);

        assertTrue(existingPointsTable.containsKey(teamName));
        assertEquals(priorTeamPoints + GameScoresProcessor.LOSS_POINTS, existingPointsTable.get(teamName));
    }

    @Test
    void GivenExistingTeamDraws_WhenUpdatingPointsTable_ThenExpectedUpdateIsMade() {
        final Map<String, Integer> existingPointsTable = getPointsTableWithSingleTeam();
        final String teamName = (String) existingPointsTable.keySet().toArray()[0];
        final Integer priorTeamPoints = existingPointsTable.get(teamName);

        genericProcessor.updateTeamPoints(existingPointsTable, teamName, GameScoresProcessor.DRAW_POINTS);

        assertTrue(existingPointsTable.containsKey(teamName));
        assertEquals(priorTeamPoints + GameScoresProcessor.DRAW_POINTS, existingPointsTable.get(teamName));
    }

    @Test
    void GivenNewTeamWins_WhenUpdatingScoreTable_ThenExpectedUpdateIsMade() {
        final Map<String, Integer> existingPointsTable = new HashMap<>();
        final String teamName = "FirstTeam";

        genericProcessor.updateTeamPoints(existingPointsTable, teamName, GameScoresProcessor.WIN_POINTS);

        assertTrue(existingPointsTable.containsKey(teamName));
        assertEquals(GameScoresProcessor.WIN_POINTS, existingPointsTable.get(teamName));
    }

    @Test
    void GivenNewTeamLoses_WhenUpdatingPointsTable_ThenExpectedUpdateIsMade() {
        final Map<String, Integer> existingPointsTable = new HashMap<>();
        final String teamName = "FirstTeam";

        genericProcessor.updateTeamPoints(existingPointsTable, teamName, GameScoresProcessor.LOSS_POINTS);

        assertTrue(existingPointsTable.containsKey(teamName));
        assertEquals(GameScoresProcessor.LOSS_POINTS, existingPointsTable.get(teamName));
    }

    @Test
    void GivenNewTeamDraws_WhenUpdatingPointsTable_ThenExpectedUpdateIsMade() {
        final Map<String, Integer> existingPointsTable = new HashMap<>();
        final String teamName = "FirstTeam";

        genericProcessor.updateTeamPoints(existingPointsTable, teamName, GameScoresProcessor.DRAW_POINTS);

        assertTrue(existingPointsTable.containsKey(teamName));
        assertEquals(GameScoresProcessor.DRAW_POINTS, existingPointsTable.get(teamName));
    }

    private Map<String, Integer> getPointsTableWithSingleTeam() {
        final Map<String, Integer> pointsTable = new HashMap<>();
        pointsTable.put("FirstTeam", 3);
        return pointsTable;
    }

    private void assertUnsortedScoreTableEntries(final Map<String, Integer> expectedTable, final Map<String, Integer> resultTable) {
        assertEquals(expectedTable.size(), resultTable.size());
        assertEquals(expectedTable, resultTable);
    }

    private void assertLeagueTableEntries(final Map<String, Integer> expectedTable, final Map<String, Integer> resultTable) {
        assertEquals(expectedTable.size(), resultTable.size());
        assertEquals(expectedTable, resultTable);
        assertIterableEquals(Arrays.asList(expectedTable.keySet().toArray()), Arrays.asList(resultTable.keySet().toArray()));
        assertIterableEquals(Arrays.asList(expectedTable.values().toArray()), Arrays.asList(resultTable.values().toArray()));
    }
}