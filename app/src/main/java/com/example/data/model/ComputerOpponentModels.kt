package com.example.data.model

data class ComputerOpponent(
    val id: String,
    val name: String,
    val title: String,
    val avatarIcon: String,
    val level: Int, // 1 to 5
    val baseAccuracyPct: Int, // e.g. 55% to 95%
    val reactionSpeedSeconds: Int, // 3 to 10 seconds
    val isClutchSpecialist: Boolean,
    val loreDescription: String
)

data class BotDuelRound(
    val roundNumber: Int,
    val totalRounds: Int = 5,
    val computerOpponent: ComputerOpponent,
    val playerRoundScore: Int = 0,
    val computerRoundScore: Int = 0,
    val isPlayerAnswered: Boolean = false,
    val isComputerAnswered: Boolean = false,
    val computerActionLog: String = ""
)

data class ComputerDuelSessionState(
    val bot: ComputerOpponent,
    val questions: List<TriviaQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val playerSelectedOption: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val playerScore: Int = 0,
    val computerScore: Int = 0,
    val playerStreak: Int = 0,
    val computerStreak: Int = 0,
    val computerReactionTimeLeft: Int = 0,
    val computerActionLog: String? = null,
    val timeRemainingSeconds: Int = 12,
    val totalTimeLimitSeconds: Int = 12,
    val isDuelFinished: Boolean = false,
    val currentDifficultyLevel: Int = 1,
    val difficultyProgressionLabel: String = "Level 1: Rookie Warmup"
)
