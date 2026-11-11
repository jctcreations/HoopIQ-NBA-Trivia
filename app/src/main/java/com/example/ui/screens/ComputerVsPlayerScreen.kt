package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.components.CountdownTimerBar
import com.example.ui.util.TriviaHaptics
import com.example.ui.theme.BlazeOrange
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CourtNavyCard
import com.example.ui.theme.CourtNavyDark
import com.example.ui.theme.CourtNavySurface
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricGreen
import com.example.ui.theme.GoldChampionship
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TriviaViewModel

@Composable
fun ComputerVsPlayerScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val duelState by viewModel.botDuelState.collectAsState()
    val bot = duelState.bot
    val currentQuestion = duelState.questions.getOrNull(duelState.currentQuestionIndex)
    val userProfile by viewModel.userProfile.collectAsState()

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    if (duelState.isDuelFinished || currentQuestion == null) {
        val playerWon = duelState.playerScore >= duelState.computerScore
        val isMaxLevel = duelState.currentDifficultyLevel >= 5

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(CourtNavyDark)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        Brush.radialGradient(listOf(if (playerWon) GoldChampionship else BlazeOrange, CourtNavyCard)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (playerWon) "AI OPPONENT DEFEATED! 🏆" else "COMPUTER CLUTCH WIN! 🤖",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = if (playerWon) GoldChampionship else CrimsonRed
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${userProfile.username} ${duelState.playerScore}  -  ${duelState.computerScore} ${bot.name}",
                fontSize = 19.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (playerWon) {
                Box(
                    modifier = Modifier
                        .background(ElectricGreen.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (!isMaxLevel) "Difficulty Upgraded to Level ${duelState.currentDifficultyLevel}!" else "Maximum Legend Difficulty Cleared!",
                        color = ElectricGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            } else {
                Text(
                    text = "Practice your shooting speed to beat ${bot.name}'s ${bot.baseAccuracyPct}% accuracy.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.retryOrProgressBotDuel() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("retry_bot_duel_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange)
            ) {
                Icon(imageVector = Icons.Default.Replay, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (playerWon && !isMaxLevel) "NEXT HARDER COMPUTER TIER" else "PLAY AGAIN",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("return_home_from_bot_duel_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CourtNavySurface)
            ) {
                Text("RETURN TO MAIN HUB", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CourtNavyDark)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            // Top Header: Exit, Difficulty Progression Badge & Scoreboard
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("bot_duel_scoreboard_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(BlazeOrange.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "LEVEL ${duelState.currentDifficultyLevel} / 5",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlazeOrange
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Shot ${duelState.currentQuestionIndex + 1}/${duelState.questions.size}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }

                        IconButton(
                            onClick = { viewModel.navigateTo(AppScreen.HOME) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Exit", tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Player Score vs Bot Score
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "YOU", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlazeOrange)
                            Text(text = "${duelState.playerScore}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                            Text(text = "${duelState.playerStreak}x streak", fontSize = 10.sp, color = TextSecondary)
                        }

                        Text(text = "VS", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextMuted)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = bot.avatarIcon, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = bot.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                            }
                            Text(text = "${duelState.computerScore}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                            Text(text = "${bot.baseAccuracyPct}% acc", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scaling Difficulty Notice Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CourtNavySurface, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = GoldChampionship,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Opponent IQ: ${bot.title}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldChampionship
                    )
                }

                Text(
                    text = "Rotates in ${viewModel.daysUntilRegeneration}d",
                    fontSize = 10.sp,
                    color = ElectricCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Shot Clock Countdown
            CountdownTimerBar(
                remainingSeconds = duelState.timeRemainingSeconds,
                totalSeconds = duelState.totalTimeLimitSeconds
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Computer Action Ticker
            duelState.computerActionLog?.let { action ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CourtNavySurface, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🤖 $action",
                        fontSize = 11.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Animated Question and Options Transition
            AnimatedContent(
                targetState = duelState.currentQuestionIndex,
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
                label = "BotDuelQuestionSlideTransition"
            ) { questionIndex ->
                val question = duelState.questions.getOrNull(questionIndex)
                if (question != null) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Question Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("bot_duel_question_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .background(CourtNavySurface, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${question.difficulty.name} (+${question.difficulty.points} PTS)",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldChampionship
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = question.prompt,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    lineHeight = 23.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Multiple-Choice Options List
                        question.options.forEachIndexed { index, optionText ->
                            val isSelected = duelState.playerSelectedOption == index
                            val isCorrect = question.correctIndex == index

                            val optionBg by animateColorAsState(
                                targetValue = when {
                                    duelState.isAnswerSubmitted && isCorrect -> ElectricGreen.copy(alpha = 0.25f)
                                    duelState.isAnswerSubmitted && isSelected && !isCorrect -> CrimsonRed.copy(alpha = 0.25f)
                                    isSelected -> BlazeOrange.copy(alpha = 0.2f)
                                    else -> CourtNavySurface
                                },
                                animationSpec = tween(durationMillis = 200),
                                label = "optionBg_$index"
                            )

                            val optionBorder by animateColorAsState(
                                targetValue = when {
                                    duelState.isAnswerSubmitted && isCorrect -> ElectricGreen
                                    duelState.isAnswerSubmitted && isSelected && !isCorrect -> CrimsonRed
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
                                    .clickable(enabled = !duelState.isAnswerSubmitted) {
                                        val isCorrect = question.correctIndex == index
                                        if (isCorrect) {
                                            TriviaHaptics.vibrateCorrect(context, hapticFeedback)
                                        } else {
                                            TriviaHaptics.vibrateIncorrect(context, hapticFeedback)
                                        }
                                        viewModel.submitBotDuelAnswer(index)
                                    }
                                    .testTag("bot_option_${index}"),
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
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(if (isSelected) BlazeOrange else CourtNavyDark, CircleShape),
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

                                    if (duelState.isAnswerSubmitted) {
                                        if (isCorrect) {
                                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = ElectricGreen)
                                        } else if (isSelected) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = null, tint = CrimsonRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom Action (Next Shot)
        AnimatedVisibility(visible = duelState.isAnswerSubmitted) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Button(
                    onClick = { viewModel.nextBotDuelQuestion() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("bot_next_shot_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange)
                ) {
                    Text(
                        text = if (duelState.currentQuestionIndex + 1 < duelState.questions.size) "NEXT DUEL SHOT" else "FINAL MATCH RESULTS",
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
