package com.league.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    private final String validFilePath = "/Users/diananyabongo/IdeaProjects/League/tst/testresources/InputScores.txt";
    private final String invalidFilePath = "Fake/Path";

    private FileReader genericFileReader;

    private final String genericTeamScoreline = "MyTeam 10";
    private final String genericGameScoreline = "FirstTeam 3, SecondTeam 5";

    private final List<String> validTeamScorelineList = List.of(
            genericTeamScoreline,
            "My Team 5", // Space in team name
            "My very good team 10" // Many spaces in team name
    );

    private final List<String> invalidTeamScorelineList = List.of(
            "FirstTeam One", // Test score
            "FirstTeam 10.7", // Decimal score
            "FirstTeam", // No score
            "1 FirstTeam" // Flipped score and name
    );

    private final List<String> validGameScorelineList = List.of(
            genericGameScoreline,
            "FirstTeam 2 , SecondTeam 3", // Extra spaces between teams
            "FirstTeam 4,SecondTeam 2" // No spaces between teams
    );

    private final List<String> invalidGameScorelineList = List.of(
            "FirstTeam 4, SecondTeam 2, ThirdTeam 6", // More than 2 teams in game scoreline
            "FirstTeam 4, FirstTeam 2", // One team playing itself
            "SecondTeam 6" // Less than 2 teams in a game scoreline
    );


    @BeforeEach
    void setUp() throws FileNotFoundException {
        genericFileReader = new FileReader(validFilePath);
    }

    @Test
    void WhenConstructedWithHValidFilePath_ThenConstructionSuccess() {
        assertDoesNotThrow(() -> new FileReader(validFilePath));
    }

    @Test
    void WhenConstructedWithNullFilePath_ThenExpectedExceptionIsThrown() {
        assertThrows(NullPointerException.class, () -> new FileReader(null));
    }

    @Test
    void WhenConstructedWithInvalidFilePath_ThenExpectedExceptionIsThrown() {
        assertThrows(FileNotFoundException.class, () -> new FileReader(invalidFilePath));
    }

    @Test
    void GivenGenericFileReader_WhenParsingValidGameScorelines_ThenParsingSucceeds() throws Exception {
        validGameScorelineList.forEach(entry ->
                assertDoesNotThrow(() -> genericFileReader.parseGameScore(entry)));
    }

    @Test
    void GivenGenericFileReader_WhenParsingInValidGameScoreLine_ThenExpectedExceptionIsThrown() {
        invalidGameScorelineList.forEach(entry ->
                assertThrows(RuntimeException.class,
                        () -> genericFileReader.parseGameScore(entry),
                        String.format("Failed to throw for game scoreline: %s", entry)));
    }

    @Test
    void GivenGenericFileReader_WhenParsingValidTeamScorelines_ThenParsingSucceeds() {
        validTeamScorelineList.forEach(entry ->
                assertDoesNotThrow(() -> genericFileReader.parseTeamScore(entry)));
    }

    @Test
    void GivenGenericFileReader_WhenParsingInValidTeamScoreLine_ThenExpectedExceptionIsThrown() {
        invalidTeamScorelineList.forEach(entry ->
                assertThrows(RuntimeException.class,
                        () -> genericFileReader.parseTeamScore(entry),
                        String.format("Failed to throw for game scoreline: %s", entry)));
    }
}