package com.example.data.model

enum class ChallengeStatus {
    PENDING,
    ACCEPTED,
    COMPLETED,
    DECLINED
}

data class HeadToHeadChallenge(
    val id: String,
    val challengerName: String,
    val opponentName: String,
    val category: TriviaCategory,
    val challengerScore: Int = 0,
    val opponentScore: Int = 0,
    val status: ChallengeStatus = ChallengeStatus.PENDING,
    val timestamp: String = "Just now",
    val questionsCount: Int = 5,
    val winnerName: String? = null
)

data class HeadToHeadSessionState(
    val activeChallenge: HeadToHeadChallenge? = null,
    val questions: List<TriviaQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val userSelectedOption: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val userScore: Int = 0,
    val opponentScore: Int = 0,
    val userStreak: Int = 0,
    val opponentStreak: Int = 0,
    val opponentLastAction: String? = null,
    val timeRemainingSeconds: Int = 12,
    val totalTimeLimitSeconds: Int = 12,
    val isMatchFinished: Boolean = false
)
