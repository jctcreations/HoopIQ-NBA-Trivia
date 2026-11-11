package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameStatus
import com.example.ui.util.TriviaHaptics
import com.example.ui.components.CountdownTimerBar
import com.example.ui.theme.BlazeOrange
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CourtNavyCard
import com.example.ui.theme.CourtNavyDark
import com.example.ui.theme.CourtNavySurface
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricGreen
import com.example.ui.theme.GoldChampionship
import com.example.ui.theme.ScoreboardLed
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TriviaViewModel

@Composable
fun QuizScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val quizState by viewModel.quizState.collectAsState()
    val currentQuestion = quizState.questions.getOrNull(quizState.currentIndex)

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    if (currentQuestion == null) {
        Box(
            modifier = modifier.fillMaxSize().background(CourtNavyDark),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No questions available", color = TextPrimary)
        }
        return
    }

    val totalQuestions = quizState.questions.size
    val currentProgress = ((quizState.currentIndex + 1).toFloat() / totalQuestions).coerceIn(0f, 1f)
    val animatedScore by animateIntAsState(
        targetValue = quizState.score,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "animatedScore"
    )

    val totalShotsAnswered = if (quizState.isAnswerSubmitted) quizState.currentIndex + 1 else quizState.currentIndex
    val fgPct = if (totalShotsAnswered > 0) ((quizState.correctCount.toFloat() / totalShotsAnswered) * 100).toInt() else 100
    val quarterLabel = when {
        quizState.currentIndex < 3 -> "Q1"
        quizState.currentIndex < 6 -> "Q2"
        quizState.currentIndex < 9 -> "Q3"
        else -> "Q4 CLUTCH"
    }

    val liveGames by viewModel.liveGames.collectAsState()
    var activeGameIndex by remember { mutableIntStateOf(0) }
    val currentLiveGame = if (liveGames.isNotEmpty()) liveGames[activeGameIndex % liveGames.size] else null

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CourtNavyDark)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Header: Exit Button, Quarter Pill, Shot Progress & Animated Live Score Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("exit_quiz_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Quiz",
                        tint = TextSecondary
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Quarter indicator
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(listOf(BlazeOrange.copy(alpha = 0.25f), CourtNavyCard)),
                                RoundedCornerShape(10.dp)
                            )
                            .border(1.dp, BlazeOrange.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = quarterLabel,
                            color = BlazeOrange,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .background(CourtNavyCard, RoundedCornerShape(12.dp))
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "SHOT ${quizState.currentIndex + 1} OF $totalQuestions",
                            color = ElectricCyan,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Timeout / Streak Multiplier
                    Box(
                        modifier = Modifier
                            .background(CourtNavyCard, RoundedCornerShape(12.dp))
                            .border(1.dp, if (quizState.streak > 0) BlazeOrange else CardBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Whatshot,
                                contentDescription = "Streak",
                                tint = if (quizState.streak > 0) BlazeOrange else TextMuted,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${quizState.streak}x",
                                color = if (quizState.streak > 0) BlazeOrange else TextSecondary,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // Instant Rolling Score Ticker
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(listOf(CourtNavySurface, CourtNavyCard)),
                            RoundedCornerShape(12.dp)
                        )
                        .border(1.dp, GoldChampionship.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("live_score_display")
                ) {
                    Text(
                        text = "$animatedScore PTS",
                        color = GoldChampionship,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quiz Set Overall Progress
            LinearProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = BlazeOrange,
                trackColor = CourtNavySurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Live Arena Scoreboard Stats Bar (FG%, FGM, Streak, Best Streak)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("arena_live_stats_hud"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Field Goal Percentage (Accuracy)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(
                                    when {
                                        fgPct >= 75 -> ElectricGreen.copy(alpha = 0.2f)
                                        fgPct >= 50 -> BlazeOrange.copy(alpha = 0.2f)
                                        else -> CrimsonRed.copy(alpha = 0.2f)
                                    },
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SportsBasketball,
                                contentDescription = null,
                                tint = when {
                                    fgPct >= 75 -> ElectricGreen
                                    fgPct >= 50 -> BlazeOrange
                                    else -> CrimsonRed
                                },
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "FG% $fgPct%",
                                color = when {
                                    fgPct >= 75 -> ElectricGreen
                                    fgPct >= 50 -> BlazeOrange
                                    else -> CrimsonRed
                                },
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "${quizState.correctCount}/$totalShotsAnswered FGM",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // Best streak badge
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = GoldChampionship,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "BEST: ${quizState.bestStreak}x",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldChampionship
                        )
                    }

                    // Momentum status tag
                    Box(
                        modifier = Modifier
                            .background(CourtNavySurface, RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = when {
                                quizState.streak >= 3 -> "🔥 ON FIRE"
                                quizState.streak >= 1 -> "⚡ HEATING UP"
                                else -> "🎯 CLUTCH"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (quizState.streak >= 3) BlazeOrange else ElectricCyan
                        )
                    }
                }
            }

            // Real-Time NBA Arena Scoreboard Ticker Pill
            if (currentLiveGame != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (liveGames.isNotEmpty()) {
                                activeGameIndex = (activeGameIndex + 1) % liveGames.size
                            }
                        }
                        .testTag("nba_live_game_ticker"),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = CourtNavySurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            // Live indicator dot
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        if (currentLiveGame.status == GameStatus.LIVE) CrimsonRed else ElectricCyan,
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLiveGame.status == GameStatus.LIVE) "LIVE NBA" else "NBA SCORE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = if (currentLiveGame.status == GameStatus.LIVE) CrimsonRed else TextMuted
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${currentLiveGame.awayTeam} ${currentLiveGame.awayScore} @ ${currentLiveGame.homeTeam} ${currentLiveGame.homeScore}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLiveGame.status == GameStatus.LIVE) "• ${currentLiveGame.quarter} ${currentLiveGame.clock}" else "• FINAL",
                                fontSize = 10.sp,
                                color = if (currentLiveGame.status == GameStatus.LIVE) GoldChampionship else TextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Next NBA Game",
                            tint = TextMuted,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Shot Clock Countdown Timer Bar with Pause/Resume toggle
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shot_clock_timer_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    CountdownTimerBar(
                        remainingSeconds = quizState.timeRemainingSeconds,
                        totalSeconds = quizState.totalTimeLimitSeconds
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = GoldChampionship,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Speed bonus: +15 PTS / sec remaining",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }

                        if (!quizState.isAnswerSubmitted) {
                            Row(
                                modifier = Modifier
                                    .clickable { viewModel.togglePauseTimer() }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                    .testTag("pause_timer_toggle"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (quizState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                    contentDescription = "Pause Clock",
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = if (quizState.isPaused) "RESUME" else "TIMEOUT",
                                    color = ElectricCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Animated Question and Options Transition
            AnimatedContent(
                targetState = quizState.currentIndex,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally(
                            animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                            initialOffsetX = { fullWidth -> fullWidth }
                        ) + fadeIn(animationSpec = tween(durationMillis = 320))).togetherWith(
                            slideOutHorizontally(
                                animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                                targetOffsetX = { fullWidth -> -fullWidth }
                            ) + fadeOut(animationSpec = tween(durationMillis = 320))
                        )
                    } else {
                        (slideInHorizontally(
                            animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                            initialOffsetX = { fullWidth -> -fullWidth }
                        ) + fadeIn(animationSpec = tween(durationMillis = 320))).togetherWith(
                            slideOutHorizontally(
                                animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing),
                                targetOffsetX = { fullWidth -> fullWidth }
                            ) + fadeOut(animationSpec = tween(durationMillis = 320))
                        )
                    }.using(SizeTransform(clip = false))
                },
                label = "QuestionSlideTransition"
            ) { questionIndex ->
                val question = quizState.questions.getOrNull(questionIndex)
                if (question != null) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Difficulty & Category Info
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(CourtNavyCard, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${question.difficulty.name} (+${question.difficulty.points} BASE PTS)",
                                    color = GoldChampionship,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = question.category.title,
                                color = TextMuted,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Trivia Question Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quiz_question_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text(
                                    text = question.prompt,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    lineHeight = 24.sp
                                )

                                question.statContext?.let { statText ->
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = ElectricCyan,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = statText,
                                            fontSize = 12.sp,
                                            color = ElectricCyan,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Multiple-Choice Options List
                        question.options.forEachIndexed { index, optionText ->
                            val isSelected = quizState.selectedOptionIndex == index
                            val isCorrect = question.correctIndex == index

                            val optionBg by animateColorAsState(
                                targetValue = when {
                                    quizState.isAnswerSubmitted && isCorrect -> ElectricGreen.copy(alpha = 0.25f)
                                    quizState.isAnswerSubmitted && isSelected && !isCorrect -> CrimsonRed.copy(alpha = 0.25f)
                                    isSelected -> BlazeOrange.copy(alpha = 0.2f)
                                    else -> CourtNavySurface
                                },
                                animationSpec = tween(durationMillis = 200),
                                label = "optionBg_$index"
                            )

                            val optionBorder by animateColorAsState(
                                targetValue = when {
                                    quizState.isAnswerSubmitted && isCorrect -> ElectricGreen
                                    quizState.isAnswerSubmitted && isSelected && !isCorrect -> CrimsonRed
                                    isSelected -> BlazeOrange
                                    else -> CardBorder
                                },
                                animationSpec = tween(durationMillis = 200),
                                label = "optionBorder_$index"
                            )

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable(enabled = !quizState.isAnswerSubmitted && !quizState.isPaused) {
                                        val isCorrect = question.correctIndex == index
                                        if (isCorrect) {
                                            TriviaHaptics.vibrateCorrect(context, hapticFeedback)
                                        } else {
                                            TriviaHaptics.vibrateIncorrect(context, hapticFeedback)
                                        }
                                        viewModel.selectOption(index)
                                        viewModel.submitAnswer(index)
                                    }
                                    .testTag("quiz_option_${index}"),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = optionBg),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, optionBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(14.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(
                                                    if (isSelected) BlazeOrange else CourtNavyDark,
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = ('A' + index).toString(),
                                                color = if (isSelected) Color.White else TextSecondary,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = optionText,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    }

                                    if (quizState.isAnswerSubmitted) {
                                        if (isCorrect) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = "Correct",
                                                tint = ElectricGreen,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        } else if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Wrong",
                                                tint = CrimsonRed,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Instant Score Breakdown Pill on Submission
            AnimatedVisibility(
                visible = quizState.isAnswerSubmitted && quizState.lastScoreEarned != null,
                enter = fadeIn() + slideInVertically()
            ) {
                quizState.lastScoreEarned?.let { scoreRecap ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .testTag("instant_score_breakdown_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (scoreRecap.isCorrect) ElectricGreen.copy(alpha = 0.15f) else CrimsonRed.copy(alpha = 0.15f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (scoreRecap.isCorrect) ElectricGreen.copy(alpha = 0.6f) else CrimsonRed.copy(alpha = 0.6f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (scoreRecap.isCorrect) "SWISH! SHOT SCORED" else if (scoreRecap.wasTimeOut) "24-SEC VIOLATION (TIME EXPIRED)" else "MISSED SHOT",
                                    color = if (scoreRecap.isCorrect) ElectricGreen else CrimsonRed,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp
                                )
                                if (scoreRecap.isCorrect) {
                                    Text(
                                        text = "Base: +${scoreRecap.basePoints} | Speed: +${scoreRecap.speedBonus} | Streak: +${scoreRecap.streakBonus}",
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                            Text(
                                text = if (scoreRecap.isCorrect) "+${scoreRecap.totalEarned} PTS" else "+0 PTS",
                                color = if (scoreRecap.isCorrect) GoldChampionship else CrimsonRed,
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            // Explanation Card on submit
            AnimatedVisibility(visible = quizState.isAnswerSubmitted) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .testTag("explanation_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = GoldChampionship,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TRIVIA BREAKDOWN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldChampionship
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = currentQuestion.explanation,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )

                        // Gemini API Validation badge & trigger
                        val validationMap by viewModel.aiValidationMap.collectAsState()
                        val validation = validationMap[currentQuestion.id]
                        val selectedPlayer by viewModel.selectedAiPlayer.collectAsState()

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                        Spacer(modifier = Modifier.height(8.dp))

                        if (validation != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ElectricGreen.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = ElectricGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Gemini Fact-Check: Verified 100% Accurate",
                                        color = ElectricGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                    Text(
                                        text = validation.statAccuracyNotes,
                                        color = TextSecondary,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedPlayer?.let { p ->
                                            viewModel.validateCurrentAiQuestion(currentQuestion, p)
                                        }
                                    }
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = ElectricCyan,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Validate Stat Accuracy with Gemini API",
                                        fontSize = 11.sp,
                                        color = ElectricCyan,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = "VERIFY →",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldChampionship
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action (Next Shot or Finish Game)
        AnimatedVisibility(visible = quizState.isAnswerSubmitted) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Button(
                    onClick = { viewModel.nextQuestion() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("next_question_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange)
                ) {
                    Text(
                        text = if (quizState.currentIndex + 1 < totalQuestions) "NEXT SHOT" else "SEE GAME RESULTS",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                }
            }
        }
    }
}
