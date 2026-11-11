package com.example.data.model

data class NbaPlayer(
    val id: String,
    val name: String,
    val team: String,
    val position: String,
    val jerseyNumber: Int,
    val ppg: Double,
    val rpg: Double,
    val apg: Double,
    val spg: Double,
    val bpg: Double,
    val fgPct: Double,
    val ftPct: Double,
    val threePct: Double,
    val championships: Int,
    val allStarAppearances: Int,
    val draftYear: Int,
    val draftPick: Int,
    val collegeOrCountry: String,
    val notableFact: String
)

data class LiveNbaGame(
    val gameId: String,
    val homeTeam: String,
    val homeScore: Int,
    val awayTeam: String,
    val awayScore: Int,
    val quarter: String,
    val clock: String,
    val status: GameStatus,
    val topPerformer: String,
    val topStatLine: String
)

enum class GameStatus {
    LIVE, FINAL, UPCOMING
}

enum class TriviaCategory(val title: String, val icon: String, val description: String) {
    DAILY_CHALLENGE("Daily Clutch Challenge", "🔥", "10 timed questions synced for today's leaderboard"),
    LIVE_STATS("Live Stats & Box Scores", "⚡", "Questions based on today's real-time NBA stats & leaders"),
    PLAYER_SHOWDOWN("Who Averaged More?", "⚔️", "Head-to-head stat comparisons between superstars"),
    FRANCHISE_LORE("Franchise & Finals Lore", "🏆", "Championship history, MVP races, draft steals & records"),
    GUESS_THE_PLAYER("Who Am I?", "🕵️", "Deduce the player from their stat line and accolades")
}

data class TriviaQuestion(
    val id: String,
    val prompt: String,
    val category: TriviaCategory,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val statContext: String? = null,
    val difficulty: QuestionDifficulty = QuestionDifficulty.MEDIUM,
    val timeLimitSeconds: Int = 15
)

enum class QuestionDifficulty(val points: Int) {
    EASY(100),
    MEDIUM(200),
    HARD(350),
    CLUTCH(500)
}

data class LeaderboardEntry(
    val rank: Int,
    val username: String,
    val score: Int,
    val streak: Int,
    val accuracyPct: Int,
    val timeTakenSeconds: Int,
    val favoriteTeam: String,
    val isCurrentUser: Boolean = false
)

data class UserProfile(
    val username: String = "CourtGeneral",
    val favoriteTeam: String = "Golden State Warriors",
    val totalScore: Int = 14250,
    val gamesPlayed: Int = 28,
    val dailyBestScore: Int = 2850,
    val currentStreak: Int = 7,
    val bestStreak: Int = 14,
    val winRate: Int = 82,
    val tierTitle: String = "All-NBA First Team"
)
