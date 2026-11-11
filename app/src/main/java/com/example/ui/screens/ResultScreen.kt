package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlazeOrange
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CourtNavyCard
import com.example.ui.theme.CourtNavyDark
import com.example.ui.theme.CourtNavySurface
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.ElectricGreen
import com.example.ui.theme.GoldChampionship
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TriviaViewModel

@Composable
fun ResultScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val quizState by viewModel.quizState.collectAsState()
    val totalQ = quizState.questions.size
    val accuracy = if (totalQ > 0) ((quizState.correctCount.toFloat() / totalQ) * 100).toInt() else 0

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    val performanceTitle = when {
        accuracy >= 90 -> "FINALS MVP PERFORMANCE 🏆"
        accuracy >= 70 -> "ALL-STAR CLUTCH SHOOTER ⭐"
        accuracy >= 50 -> "STARTER ROTATION PLAYER 🏀"
        else -> "ROOKIE LEARNING CURVE ⚡"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CourtNavyDark)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Trophy Star Icon
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(
                        Brush.radialGradient(listOf(GoldChampionship, BlazeOrange)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Trophy",
                    tint = Color.White,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = performanceTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = GoldChampionship,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${quizState.score}",
                fontSize = 46.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
            Text(
                text = "TOTAL POINTS EARNED",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Stat Recap Matrix
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("result_stats_matrix_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(
                            title = "ACCURACY",
                            value = "$accuracy%",
                            color = if (accuracy >= 70) ElectricGreen else BlazeOrange
                        )
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(CardBorder))
                        StatItem(
                            title = "FG MADE",
                            value = "${quizState.correctCount}/$totalQ",
                            color = ElectricCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        StatItem(
                            title = "MAX STREAK",
                            value = "${quizState.bestStreak}x 🔥",
                            color = BlazeOrange
                        )
                        Box(modifier = Modifier.width(1.dp).height(40.dp).background(CardBorder))
                        StatItem(
                            title = "TOTAL TIME",
                            value = "${quizState.totalTimeTakenSeconds}s",
                            color = TextPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Leaderboard Rank Notification Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("result_leaderboard_promo_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Leaderboard,
                        contentDescription = null,
                        tint = GoldChampionship,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Score Posted to Daily Leaderboard!",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Check the live standings to see your climb into the Top 10.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Actions
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Button(
                onClick = { viewModel.restartCurrentQuiz() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("play_again_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange)
            ) {
                Icon(imageVector = Icons.Default.Replay, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "RUN IT BACK (PLAY AGAIN)",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.LEADERBOARD) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("view_leaderboard_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Icon(imageVector = Icons.Default.Leaderboard, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "STANDINGS", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("return_home_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "MAIN MENU", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun StatItem(title: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
    }
}
