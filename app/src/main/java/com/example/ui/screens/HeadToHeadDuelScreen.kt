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
import androidx.compose.material.icons.filled.SportsEsports
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
fun HeadToHeadDuelScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val h2hState by viewModel.h2hState.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val challenge = h2hState.activeChallenge
    val currentQuestion = h2hState.questions.getOrNull(h2hState.currentQuestionIndex)

    BackHandler {
        viewModel.navigateTo(AppScreen.CHALLENGES)
    }

    if (h2hState.isMatchFinished || currentQuestion == null) {
        // Match Finished Recap Card
        val userWon = h2hState.userScore > h2hState.opponentScore
        val isDraw = h2hState.userScore == h2hState.opponentScore

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
                        Brush.radialGradient(listOf(if (userWon) GoldChampionship else BlazeOrange, CourtNavyCard)),
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
                text = if (userWon) "DUEL VICTORY! 🏆" else if (isDraw) "TIED MATCHUP! 🤝" else "DEFEAT IN THE CLUTCH! ⚡",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = if (userWon) GoldChampionship else if (isDraw) ElectricCyan else CrimsonRed
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${userProfile.username} ${h2hState.userScore}  -  ${h2hState.opponentScore} ${challenge?.challengerName ?: "Opponent"}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.navigateTo(AppScreen.CHALLENGES) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("return_to_challenges_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange)
            ) {
                Text("RETURN TO DUELS HUB", fontWeight = FontWeight.Black, fontSize = 14.sp)
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
            // Live Head-to-Head Duel Scoreboard Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("h2h_live_scoreboard_card"),
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
                        Text(
                            text = "1v1 DUEL SHOT ${h2hState.currentQuestionIndex + 1}/${h2hState.questions.size}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                        IconButton(
                            onClick = { viewModel.navigateTo(AppScreen.CHALLENGES) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Exit", tint = TextSecondary)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Player Score vs Opponent Score
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "YOU", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlazeOrange)
                            Text(text = "${h2hState.userScore}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                            Text(text = "${h2hState.userStreak}x streak", fontSize = 10.sp, color = TextSecondary)
                        }

                        Text(text = "VS", fontSize = 14.sp, fontWeight = FontWeight.Black, color = TextMuted)

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = challenge?.challengerName ?: "OPPONENT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                            Text(text = "${h2hState.opponentScore}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                            Text(text = "${h2hState.opponentStreak}x streak", fontSize = 10.sp, color = TextSecondary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Shot Clock Countdown
            CountdownTimerBar(
                remainingSeconds = h2hState.timeRemainingSeconds,
                totalSeconds = h2hState.totalTimeLimitSeconds
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Opponent Live Reaction Ticker
            h2hState.opponentLastAction?.let { action ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CourtNavySurface, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "⚡ $action",
                        fontSize = 11.sp,
                        color = ElectricCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Animated Question and Options Transition
            AnimatedContent(
                targetState = h2hState.currentQuestionIndex,
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
                label = "H2HQuestionSlideTransition"
            ) { questionIndex ->
                val question = h2hState.questions.getOrNull(questionIndex)
                if (question != null) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Question Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("h2h_question_card"),
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
                                    lineHeight = 23.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Options List
                        question.options.forEachIndexed { index, optionText ->
                            val isSelected = h2hState.userSelectedOption == index
                            val isCorrect = question.correctIndex == index

                            val optionBg by animateColorAsState(
                                targetValue = when {
                                    h2hState.isAnswerSubmitted && isCorrect -> ElectricGreen.copy(alpha = 0.25f)
                                    h2hState.isAnswerSubmitted && isSelected && !isCorrect -> CrimsonRed.copy(alpha = 0.25f)
                                    isSelected -> BlazeOrange.copy(alpha = 0.2f)
                                    else -> CourtNavySurface
                                },
                                animationSpec = tween(durationMillis = 200),
                                label = "optionBg_$index"
                            )

                            val optionBorder by animateColorAsState(
                                targetValue = when {
                                    h2hState.isAnswerSubmitted && isCorrect -> ElectricGreen
                                    h2hState.isAnswerSubmitted && isSelected && !isCorrect -> CrimsonRed
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
                                    .clickable(enabled = !h2hState.isAnswerSubmitted) {
                                        val isCorrect = question.correctIndex == index
                                        if (isCorrect) {
                                            TriviaHaptics.vibrateCorrect(context, hapticFeedback)
                                        } else {
                                            TriviaHaptics.vibrateIncorrect(context, hapticFeedback)
                                        }
                                        viewModel.submitH2hAnswer(index)
                                    }
                                    .testTag("h2h_option_${index}"),
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

                                    if (h2hState.isAnswerSubmitted) {
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
        AnimatedVisibility(visible = h2hState.isAnswerSubmitted) {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Button(
                    onClick = { viewModel.nextH2hQuestion() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("h2h_next_shot_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange)
                ) {
                    Text(
                        text = if (h2hState.currentQuestionIndex + 1 < h2hState.questions.size) "NEXT DUEL SHOT" else "FINAL MATCH RESULTS",
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
