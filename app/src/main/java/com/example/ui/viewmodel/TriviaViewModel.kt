package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.StadiumAudioPlayer
import com.example.data.datasource.LeaderboardDataSource
import com.example.data.model.ChallengeStatus
import com.example.data.model.HeadToHeadChallenge
import com.example.data.model.HeadToHeadSessionState
import com.example.data.model.LeaderboardEntry
import com.example.data.model.LiveNbaGame
import com.example.data.model.NbaPlayer
import com.example.data.model.TriviaCategory
import com.example.data.model.TriviaQuestion
import com.example.data.model.UserProfile
import com.example.data.repository.TriviaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class AppScreen {
    HOME,
    QUIZ,
    RESULT,
    LIVE_STATS,
    LEADERBOARD,
    PROFILE,
    CHALLENGES,
    HEAD_TO_HEAD_DUEL,
    PLAYER_AI_QUIZ,
    COMPUTER_VS_PLAYER
}

/**
 * Breakdown of points awarded for the most recent answer submission
 */
data class LastScoreEarned(
    val basePoints: Int = 0,
    val speedBonus: Int = 0,
    val streakBonus: Int = 0,
    val totalEarned: Int = 0,
    val isCorrect: Boolean = false,
    val wasTimeOut: Boolean = false
)

/**
 * Comprehensive UI State for the Quiz Screen
 */
data class QuizSessionState(
    val category: TriviaCategory = TriviaCategory.DAILY_CHALLENGE,
    val questions: List<TriviaQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val score: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val correctCount: Int = 0,
    val timeRemainingSeconds: Int = 15,
    val totalTimeLimitSeconds: Int = 15,
    val totalTimeTakenSeconds: Int = 0,
    val isPaused: Boolean = false,
    val lastScoreEarned: LastScoreEarned? = null,
    val isQuizFinished: Boolean = false
)

class TriviaViewModel(
    private val repository: TriviaRepository = TriviaRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    val liveGames: StateFlow<List<LiveNbaGame>> = repository.liveGames
    val players: StateFlow<List<NbaPlayer>> = repository.players
    val dailyLeaderboard: StateFlow<List<LeaderboardEntry>> = repository.dailyLeaderboard
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val syncStatus: StateFlow<com.example.data.repository.ApiSyncStatus> = repository.syncStatus

    private val _allTimeLeaderboard = MutableStateFlow(LeaderboardDataSource.allTimeEntries)
    val allTimeLeaderboard: StateFlow<List<LeaderboardEntry>> = _allTimeLeaderboard.asStateFlow()

    private val _selectedLeaderboardTab = MutableStateFlow(0) // 0: Daily, 1: All-Time
    val selectedLeaderboardTab: StateFlow<Int> = _selectedLeaderboardTab.asStateFlow()

    private val _quizState = MutableStateFlow(QuizSessionState())
    val quizState: StateFlow<QuizSessionState> = _quizState.asStateFlow()

    private val _selectedPlayerDetail = MutableStateFlow<NbaPlayer?>(null)
    val selectedPlayerDetail: StateFlow<NbaPlayer?> = _selectedPlayerDetail.asStateFlow()

    // Background Generic Music State
    val isAudioPlaying: StateFlow<Boolean> = StadiumAudioPlayer.isPlaying

    // Challenge Request System State
    private val _challenges = MutableStateFlow<List<HeadToHeadChallenge>>(
        listOf(
            HeadToHeadChallenge(
                id = "c_1",
                challengerName = "SplashBrother99",
                opponentName = "CourtGeneral",
                category = TriviaCategory.LIVE_STATS,
                status = ChallengeStatus.PENDING,
                timestamp = "2m ago"
            ),
            HeadToHeadChallenge(
                id = "c_2",
                challengerName = "CourtGeneral",
                opponentName = "MambaMentality_24",
                category = TriviaCategory.DAILY_CHALLENGE,
                challengerScore = 2450,
                opponentScore = 2100,
                status = ChallengeStatus.COMPLETED,
                winnerName = "CourtGeneral",
                timestamp = "1h ago"
            ),
            HeadToHeadChallenge(
                id = "c_3",
                challengerName = "JokerPassMaster",
                opponentName = "CourtGeneral",
                category = TriviaCategory.PLAYER_SHOWDOWN,
                status = ChallengeStatus.PENDING,
                timestamp = "15m ago"
            )
        )
    )
    val challenges: StateFlow<List<HeadToHeadChallenge>> = _challenges.asStateFlow()

    // Head-to-Head Duel Live State
    private val _h2hState = MutableStateFlow(HeadToHeadSessionState())
    val h2hState: StateFlow<HeadToHeadSessionState> = _h2hState.asStateFlow()

    // Gemini AI Player Quiz Generation State
    private val _selectedAiPlayer = MutableStateFlow<NbaPlayer?>(null)
    val selectedAiPlayer: StateFlow<NbaPlayer?> = _selectedAiPlayer.asStateFlow()

    private val _isGeneratingAiQuiz = MutableStateFlow(false)
    val isGeneratingAiQuiz: StateFlow<Boolean> = _isGeneratingAiQuiz.asStateFlow()

    private val _aiQuizError = MutableStateFlow<String?>(null)
    val aiQuizError: StateFlow<String?> = _aiQuizError.asStateFlow()

    private val _aiValidationMap = MutableStateFlow<Map<String, com.example.ai.QuestionValidationResult>>(emptyMap())
    val aiValidationMap: StateFlow<Map<String, com.example.ai.QuestionValidationResult>> = _aiValidationMap.asStateFlow()

    // Computer Opponent Scaling Duel State
    private val _botDuelState = MutableStateFlow(
        com.example.data.model.ComputerDuelSessionState(
            bot = com.example.data.datasource.ComputerOpponentsDataSource.getOpponentForLevel(1)
        )
    )
    val botDuelState: StateFlow<com.example.data.model.ComputerDuelSessionState> = _botDuelState.asStateFlow()

    val daysUntilRegeneration: Int
        get() = com.example.data.datasource.TriviaQuestionsDataSource.getDaysUntilNextRegeneration()

    private var timerJob: Job? = null
    private var h2hTimerJob: Job? = null
    private var botTimerJob: Job? = null
    private var liveTickerJob: Job? = null

    init {
        startLiveGameTicker()
    }

    private fun startLiveGameTicker() {
        liveTickerJob?.cancel()
        liveTickerJob = viewModelScope.launch {
            repository.fetchRealTimeScoresFromApi()
            while (true) {
                delay(12000)
                repository.fetchRealTimeScoresFromApi()
            }
        }
    }

    fun toggleBackgroundMusic() {
        StadiumAudioPlayer.togglePlayback()
    }

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun setLeaderboardTab(tabIndex: Int) {
        _selectedLeaderboardTab.value = tabIndex
    }

    fun selectPlayerForDetail(player: NbaPlayer?) {
        _selectedPlayerDetail.value = player
    }

    // --- Challenge Invitation Methods ---
    fun sendChallenge(opponentName: String, category: TriviaCategory) {
        val newChallenge = HeadToHeadChallenge(
            id = "c_${System.currentTimeMillis()}",
            challengerName = userProfile.value.username,
            opponentName = opponentName.ifBlank { "CourtRival" },
            category = category,
            status = ChallengeStatus.PENDING,
            timestamp = "Just now"
        )
        _challenges.value = listOf(newChallenge) + _challenges.value
    }

    fun acceptChallenge(challenge: HeadToHeadChallenge) {
        // Update challenge status to ACCEPTED
        _challenges.value = _challenges.value.map {
            if (it.id == challenge.id) it.copy(status = ChallengeStatus.ACCEPTED) else it
        }
        // Launch Head-to-Head live session
        startHeadToHeadSession(challenge)
    }

    fun declineChallenge(challengeId: String) {
        _challenges.value = _challenges.value.map {
            if (it.id == challengeId) it.copy(status = ChallengeStatus.DECLINED) else it
        }
    }

    private fun startHeadToHeadSession(challenge: HeadToHeadChallenge) {
        val questions = repository.getQuestions(challenge.category).take(challenge.questionsCount)
        _h2hState.value = HeadToHeadSessionState(
            activeChallenge = challenge,
            questions = questions,
            currentQuestionIndex = 0,
            userSelectedOption = null,
            isAnswerSubmitted = false,
            userScore = 0,
            opponentScore = 0,
            userStreak = 0,
            opponentStreak = 0,
            opponentLastAction = "${challenge.challengerName} is locked in",
            timeRemainingSeconds = 12,
            totalTimeLimitSeconds = 12,
            isMatchFinished = false
        )
        _currentScreen.value = AppScreen.HEAD_TO_HEAD_DUEL
        startH2hTimer()
    }

    private fun startH2hTimer() {
        h2hTimerJob?.cancel()
        _h2hState.value = _h2hState.value.copy(
            timeRemainingSeconds = 12,
            userSelectedOption = null,
            isAnswerSubmitted = false
        )

        h2hTimerJob = viewModelScope.launch {
            while (_h2hState.value.timeRemainingSeconds > 0 && !_h2hState.value.isAnswerSubmitted) {
                delay(1000)
                if (!_h2hState.value.isAnswerSubmitted) {
                    val remaining = _h2hState.value.timeRemainingSeconds - 1
                    _h2hState.value = _h2hState.value.copy(timeRemainingSeconds = remaining)
                    if (remaining <= 0) {
                        submitH2hAnswer(-1)
                    }
                }
            }
        }
    }

    fun submitH2hAnswer(optionIndex: Int) {
        if (_h2hState.value.isAnswerSubmitted) return
        h2hTimerJob?.cancel()

        val state = _h2hState.value
        val currentQ = state.questions.getOrNull(state.currentQuestionIndex) ?: return
        val isUserCorrect = optionIndex == currentQ.correctIndex

        val userSpeed = if (isUserCorrect) (state.timeRemainingSeconds * 15) else 0
        val userEarned = if (isUserCorrect) (currentQ.difficulty.points + userSpeed) else 0
        val newUserScore = state.userScore + userEarned
        val newUserStreak = if (isUserCorrect) state.userStreak + 1 else 0

        // Opponent simulated turn
        val opponentIsCorrect = Random.nextFloat() > 0.35f
        val opponentEarned = if (opponentIsCorrect) (currentQ.difficulty.points + Random.nextInt(40, 150)) else 0
        val newOpponentScore = state.opponentScore + opponentEarned
        val newOpponentStreak = if (opponentIsCorrect) state.opponentStreak + 1 else 0

        val opponentFeedback = if (opponentIsCorrect) {
            "${state.activeChallenge?.challengerName ?: "Opponent"} answered correctly (+${opponentEarned} PTS)"
        } else {
            "${state.activeChallenge?.challengerName ?: "Opponent"} missed the shot (+0 PTS)"
        }

        _h2hState.value = state.copy(
            userSelectedOption = if (optionIndex >= 0) optionIndex else null,
            isAnswerSubmitted = true,
            userScore = newUserScore,
            opponentScore = newOpponentScore,
            userStreak = newUserStreak,
            opponentStreak = newOpponentStreak,
            opponentLastAction = opponentFeedback
        )
    }

    fun nextH2hQuestion() {
        val state = _h2hState.value
        val nextIdx = state.currentQuestionIndex + 1
        if (nextIdx < state.questions.size) {
            _h2hState.value = state.copy(currentQuestionIndex = nextIdx)
            startH2hTimer()
        } else {
            finishH2hDuel()
        }
    }

    private fun finishH2hDuel() {
        h2hTimerJob?.cancel()
        val state = _h2hState.value
        val winner = when {
            state.userScore > state.opponentScore -> userProfile.value.username
            state.opponentScore > state.userScore -> state.activeChallenge?.challengerName ?: "Opponent"
            else -> "Draw"
        }

        _h2hState.value = state.copy(isMatchFinished = true)

        // Mark completed in list
        state.activeChallenge?.let { challenge ->
            _challenges.value = _challenges.value.map {
                if (it.id == challenge.id) {
                    it.copy(
                        status = ChallengeStatus.COMPLETED,
                        challengerScore = state.opponentScore,
                        opponentScore = state.userScore,
                        winnerName = winner
                    )
                } else it
            }
        }
    }

    // --- Gemini AI Dynamic Player Stat Quiz Generation & Validation ---
    fun selectAiPlayer(player: NbaPlayer) {
        _selectedAiPlayer.value = player
        _aiQuizError.value = null
    }

    fun startAiPlayerQuiz(player: NbaPlayer) {
        _selectedAiPlayer.value = player
        _isGeneratingAiQuiz.value = true
        _aiQuizError.value = null

        viewModelScope.launch {
            val genResult = com.example.ai.PlayerStatQuizGenerator.generateQuestionsForPlayer(player, count = 3)
            _isGeneratingAiQuiz.value = false

            genResult.onSuccess { generatedQuestions ->
                if (generatedQuestions.isNotEmpty()) {
                    val firstQ = generatedQuestions[0]
                    _quizState.value = QuizSessionState(
                        category = TriviaCategory.GUESS_THE_PLAYER,
                        questions = generatedQuestions,
                        currentIndex = 0,
                        selectedOptionIndex = null,
                        isAnswerSubmitted = false,
                        score = 0,
                        streak = 0,
                        bestStreak = 0,
                        correctCount = 0,
                        timeRemainingSeconds = firstQ.timeLimitSeconds,
                        totalTimeLimitSeconds = firstQ.timeLimitSeconds,
                        totalTimeTakenSeconds = 0,
                        isPaused = false,
                        lastScoreEarned = null,
                        isQuizFinished = false
                    )
                    _currentScreen.value = AppScreen.QUIZ
                    startQuestionTimer()
                } else {
                    _aiQuizError.value = "AI generation completed but returned 0 questions."
                }
            }.onFailure { error ->
                _aiQuizError.value = error.localizedMessage ?: "Failed to generate AI questions with Gemini API."
            }
        }
    }

    fun validateCurrentAiQuestion(question: TriviaQuestion, player: NbaPlayer) {
        viewModelScope.launch {
            val validationResult = com.example.ai.PlayerStatQuizGenerator.validateQuestion(
                question = question,
                playerContext = "${player.name} (${player.team}, #${player.jerseyNumber})"
            )
            validationResult.onSuccess { res ->
                val currentMap = _aiValidationMap.value.toMutableMap()
                currentMap[question.id] = res
                _aiValidationMap.value = currentMap
            }
        }
    }

    // --- Scaling Computer Opponent (Bot) Duel Logic ---
    fun startComputerDuel(level: Int = 1) {
        val bot = com.example.data.datasource.ComputerOpponentsDataSource.getOpponentForLevel(level)
        val questions = com.example.data.datasource.TriviaQuestionsDataSource.getQuestionsForDifficultyLevel(level, count = 5)

        _botDuelState.value = com.example.data.model.ComputerDuelSessionState(
            bot = bot,
            questions = questions,
            currentQuestionIndex = 0,
            playerSelectedOption = null,
            isAnswerSubmitted = false,
            playerScore = 0,
            computerScore = 0,
            playerStreak = 0,
            computerStreak = 0,
            computerReactionTimeLeft = bot.reactionSpeedSeconds,
            computerActionLog = "${bot.name} is sizing up the floor",
            timeRemainingSeconds = 12,
            totalTimeLimitSeconds = 12,
            isDuelFinished = false,
            currentDifficultyLevel = level,
            difficultyProgressionLabel = "Tier $level: ${bot.title}"
        )
        _currentScreen.value = AppScreen.COMPUTER_VS_PLAYER
        startBotTimer()
    }

    private fun startBotTimer() {
        botTimerJob?.cancel()
        val bot = _botDuelState.value.bot
        _botDuelState.value = _botDuelState.value.copy(
            timeRemainingSeconds = 12,
            totalTimeLimitSeconds = 12,
            playerSelectedOption = null,
            isAnswerSubmitted = false,
            computerReactionTimeLeft = bot.reactionSpeedSeconds
        )

        botTimerJob = viewModelScope.launch {
            while (_botDuelState.value.timeRemainingSeconds > 0 && !_botDuelState.value.isAnswerSubmitted) {
                delay(1000)
                if (!_botDuelState.value.isAnswerSubmitted) {
                    val remaining = _botDuelState.value.timeRemainingSeconds - 1
                    val botRemaining = (_botDuelState.value.computerReactionTimeLeft - 1).coerceAtLeast(0)

                    _botDuelState.value = _botDuelState.value.copy(
                        timeRemainingSeconds = remaining,
                        computerReactionTimeLeft = botRemaining
                    )

                    if (remaining <= 0) {
                        submitBotDuelAnswer(-1)
                    }
                }
            }
        }
    }

    fun submitBotDuelAnswer(optionIndex: Int) {
        if (_botDuelState.value.isAnswerSubmitted) return
        botTimerJob?.cancel()

        val state = _botDuelState.value
        val currentQ = state.questions.getOrNull(state.currentQuestionIndex) ?: return
        val isPlayerCorrect = optionIndex == currentQ.correctIndex

        val playerSpeedBonus = if (isPlayerCorrect) (state.timeRemainingSeconds * 15) else 0
        val playerEarned = if (isPlayerCorrect) (currentQ.difficulty.points + playerSpeedBonus) else 0
        val newPlayerScore = state.playerScore + playerEarned
        val newPlayerStreak = if (isPlayerCorrect) state.playerStreak + 1 else 0

        // Computer AI simulation scaling with opponent difficulty level
        val bot = state.bot
        // As level increases, bot accuracy increases and clutch boost applies under 4 seconds
        val clutchBonusProb = if (bot.isClutchSpecialist && state.timeRemainingSeconds <= 4) 10 else 0
        val effectiveAccuracy = (bot.baseAccuracyPct + clutchBonusProb).coerceAtMost(98)
        val isBotCorrect = Random.nextInt(1, 101) <= effectiveAccuracy

        val botSpeedBonus = if (isBotCorrect) (state.computerReactionTimeLeft * 12) else 0
        val botEarned = if (isBotCorrect) (currentQ.difficulty.points + botSpeedBonus) else 0
        val newComputerScore = state.computerScore + botEarned
        val newComputerStreak = if (isBotCorrect) state.computerStreak + 1 else 0

        val botLog = if (isBotCorrect) {
            "${bot.name} swished the answer in ${12 - state.computerReactionTimeLeft}s (+${botEarned} PTS)!"
        } else {
            "${bot.name} clanked off the rim (+0 PTS)."
        }

        _botDuelState.value = state.copy(
            playerSelectedOption = if (optionIndex >= 0) optionIndex else null,
            isAnswerSubmitted = true,
            playerScore = newPlayerScore,
            computerScore = newComputerScore,
            playerStreak = newPlayerStreak,
            computerStreak = newComputerStreak,
            computerActionLog = botLog
        )
    }

    fun nextBotDuelQuestion() {
        val state = _botDuelState.value
        val nextIdx = state.currentQuestionIndex + 1
        if (nextIdx < state.questions.size) {
            _botDuelState.value = state.copy(currentQuestionIndex = nextIdx)
            startBotTimer()
        } else {
            // Check if player won to progress difficulty
            val playerWon = state.playerScore >= state.computerScore
            val nextLevel = if (playerWon) (state.currentDifficultyLevel + 1).coerceAtMost(5) else state.currentDifficultyLevel
            _botDuelState.value = state.copy(
                isDuelFinished = true,
                currentDifficultyLevel = nextLevel
            )
        }
    }

    fun retryOrProgressBotDuel() {
        startComputerDuel(_botDuelState.value.currentDifficultyLevel)
    }

    // --- Standard Quiz Logic ---
    fun startQuiz(category: TriviaCategory) {
        val questions = repository.getQuestions(category)
        if (questions.isEmpty()) return

        val firstQ = questions[0]
        _quizState.value = QuizSessionState(
            category = category,
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            score = 0,
            streak = 0,
            bestStreak = 0,
            correctCount = 0,
            timeRemainingSeconds = firstQ.timeLimitSeconds,
            totalTimeLimitSeconds = firstQ.timeLimitSeconds,
            totalTimeTakenSeconds = 0,
            isPaused = false,
            lastScoreEarned = null,
            isQuizFinished = false
        )
        _currentScreen.value = AppScreen.QUIZ
        startQuestionTimer()
    }

    private fun startQuestionTimer() {
        timerJob?.cancel()
        val currentQ = _quizState.value.questions.getOrNull(_quizState.value.currentIndex) ?: return
        _quizState.value = _quizState.value.copy(
            timeRemainingSeconds = currentQ.timeLimitSeconds,
            totalTimeLimitSeconds = currentQ.timeLimitSeconds,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            lastScoreEarned = null,
            isPaused = false
        )

        timerJob = viewModelScope.launch {
            while (_quizState.value.timeRemainingSeconds > 0 && !_quizState.value.isAnswerSubmitted) {
                delay(1000)
                if (!_quizState.value.isAnswerSubmitted && !_quizState.value.isPaused) {
                    val remaining = _quizState.value.timeRemainingSeconds - 1
                    _quizState.value = _quizState.value.copy(
                        timeRemainingSeconds = remaining,
                        totalTimeTakenSeconds = _quizState.value.totalTimeTakenSeconds + 1
                    )
                    if (remaining <= 0) {
                        submitAnswer(selectedIndex = -1, wasTimeOut = true)
                    }
                }
            }
        }
    }

    fun togglePauseTimer() {
        if (_quizState.value.isAnswerSubmitted) return
        _quizState.value = _quizState.value.copy(isPaused = !_quizState.value.isPaused)
    }

    fun selectOption(index: Int) {
        if (_quizState.value.isAnswerSubmitted) return
        _quizState.value = _quizState.value.copy(selectedOptionIndex = index)
    }

    fun submitAnswer(selectedIndex: Int = _quizState.value.selectedOptionIndex ?: -1, wasTimeOut: Boolean = false) {
        if (_quizState.value.isAnswerSubmitted) return
        timerJob?.cancel()

        val state = _quizState.value
        val currentQ = state.questions.getOrNull(state.currentIndex) ?: return
        val isCorrect = selectedIndex == currentQ.correctIndex

        val basePoints = if (isCorrect) currentQ.difficulty.points else 0
        val speedBonus = if (isCorrect) (state.timeRemainingSeconds * 15) else 0
        val streakBonus = if (isCorrect) (state.streak * 50) else 0
        val totalEarned = basePoints + speedBonus + streakBonus

        val newStreak = if (isCorrect) state.streak + 1 else 0
        val newBestStreak = maxOf(state.bestStreak, newStreak)
        val newScore = state.score + totalEarned
        val newCorrectCount = if (isCorrect) state.correctCount + 1 else state.correctCount

        _quizState.value = state.copy(
            selectedOptionIndex = if (selectedIndex >= 0) selectedIndex else null,
            isAnswerSubmitted = true,
            score = newScore,
            streak = newStreak,
            bestStreak = newBestStreak,
            correctCount = newCorrectCount,
            lastScoreEarned = LastScoreEarned(
                basePoints = basePoints,
                speedBonus = speedBonus,
                streakBonus = streakBonus,
                totalEarned = totalEarned,
                isCorrect = isCorrect,
                wasTimeOut = wasTimeOut
            )
        )
    }

    fun nextQuestion() {
        val state = _quizState.value
        val nextIndex = state.currentIndex + 1
        if (nextIndex < state.questions.size) {
            _quizState.value = state.copy(currentIndex = nextIndex)
            startQuestionTimer()
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        timerJob?.cancel()
        val state = _quizState.value
        _quizState.value = state.copy(isQuizFinished = true)

        repository.submitQuizScore(
            score = state.score,
            correctCount = state.correctCount,
            totalQuestions = state.questions.size,
            timeElapsed = state.totalTimeTakenSeconds
        )
        _currentScreen.value = AppScreen.RESULT
    }

    fun restartCurrentQuiz() {
        startQuiz(_quizState.value.category)
    }

    fun refreshScoresManually() {
        viewModelScope.launch {
            repository.fetchRealTimeScoresFromApi()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        h2hTimerJob?.cancel()
        botTimerJob?.cancel()
        liveTickerJob?.cancel()
        StadiumAudioPlayer.pause()
    }
}
