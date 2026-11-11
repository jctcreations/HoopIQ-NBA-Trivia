package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.TriviaCategory
import com.example.ui.components.CategoryCard
import com.example.ui.components.LiveScoreTickerCard
import com.example.ui.theme.BlazeOrange
import com.example.ui.theme.BlazeOrangeLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CourtNavyCard
import com.example.ui.theme.CourtNavyDark
import com.example.ui.theme.CourtNavySurface
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.GoldChampionship
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TriviaViewModel

@Composable
fun HomeScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val liveGames by viewModel.liveGames.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CourtNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Top Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                Brush.radialGradient(listOf(BlazeOrange, GoldChampionship)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsBasketball,
                            contentDescription = "NBA Logo",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "HOOPIQ TRIVIA",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "NBA Live Stats & Competitive Arenas",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.refreshScoresManually() },
                    modifier = Modifier.testTag("refresh_live_scores_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh scores",
                        tint = ElectricCyan
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Hero Banner with Generated Asset
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(id = R.drawable.img_trivia_hero),
                        contentDescription = "Court Arena Hero Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentScale = ContentScale.Crop
                    )
                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, CourtNavyDark.copy(alpha = 0.92f)),
                                    startY = 50f
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(BlazeOrange, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "TODAY'S DAILY SHOWDOWN",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🏆 5,000 PTS PRIZE POOL",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldChampionship
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Beat The Clutch Clock",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Live Real-Time NBA Games Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = "Live",
                        tint = BlazeOrange,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Real-Time NBA Scoreboard",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
                Text(
                    text = "Live Sync",
                    fontSize = 12.sp,
                    color = ElectricCyan,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(liveGames) { game ->
                    LiveScoreTickerCard(game = game)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // User Quick Status Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("user_quick_stats_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "CURRENT STREAK", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "${userProfile.currentStreak} 🔥", fontSize = 18.sp, fontWeight = FontWeight.Black, color = BlazeOrange)
                    }
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(CardBorder))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "DAILY BEST", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "${userProfile.dailyBestScore}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = GoldChampionship)
                    }
                    Box(modifier = Modifier.width(1.dp).height(30.dp).background(CardBorder))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "TIER", fontSize = 10.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "All-NBA 1st", fontSize = 15.sp, fontWeight = FontWeight.Black, color = ElectricCyan)
                    }
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
        }

        // Daily Play CTA
        item {
            Button(
                onClick = { viewModel.startQuiz(TriviaCategory.DAILY_CHALLENGE) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("play_daily_challenge_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlazeOrange
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "START DAILY CLUTCH CHALLENGE",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Gemini Dynamic Player Stat Quiz Feature Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { viewModel.navigateTo(AppScreen.PLAYER_AI_QUIZ) }
                    .testTag("open_gemini_player_quiz_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(ElectricCyan.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Gemini Player Stat Quiz",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(ElectricCyan.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "AI GENERATOR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ElectricCyan)
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Select any NBA player to generate & validate custom stat quizzes",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Text(
                        text = "TRY NOW →",
                        color = GoldChampionship,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Computer-Style Players (Scaling Difficulty) Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { viewModel.startComputerDuel(1) }
                    .testTag("open_computer_duel_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldChampionship.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(GoldChampionship.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🤖", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Play vs Computer AI Bots",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(GoldChampionship.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "SCALING IQ", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = GoldChampionship)
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Battle computer players that get faster & harder • Rotates every 3 days (${viewModel.daysUntilRegeneration}d left)",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Text(
                        text = "PLAY ⚔️",
                        color = BlazeOrange,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
        }

        // Categories Header
        item {
            Text(
                text = "Competitive Trivia Arenas",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Trivia Categories
        items(TriviaCategory.values().filter { it != TriviaCategory.DAILY_CHALLENGE }) { category ->
            CategoryCard(
                category = category,
                onClick = { viewModel.startQuiz(category) },
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}
