package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.NbaPlayer
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
fun PlayerAiQuizScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val players by viewModel.players.collectAsState()
    val selectedPlayer by viewModel.selectedAiPlayer.collectAsState()
    val isGenerating by viewModel.isGeneratingAiQuiz.collectAsState()
    val errorMessage by viewModel.aiQuizError.collectAsState()

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

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
                            .size(38.dp)
                            .background(
                                Brush.radialGradient(listOf(ElectricCyan, BlazeOrange)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Gemini AI",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "GEMINI PLAYER QUIZ",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Text(
                            text = "AI-Generated Player Stat Quizzes & Verification",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) }
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Gemini AI Badge Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_quiz_banner_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(ElectricCyan.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Powered by Gemini AI",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricCyan
                            )
                        }
                        Text(
                            text = "Dynamically crafts customized multiple-choice trivia on career shooting splits, clutch records, and validates each answer.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Step 1: Select Player
        item {
            Text(
                text = "1. Select an NBA Superstar to Challenge",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(players) { player ->
                    val isSelected = selectedPlayer?.id == player.id
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { viewModel.selectAiPlayer(player) }
                            .testTag("player_chip_${player.id}"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) BlazeOrange.copy(alpha = 0.25f) else CourtNavySurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.5.dp,
                            if (isSelected) BlazeOrange else CardBorder
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "#${player.jerseyNumber}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isSelected) BlazeOrange else GoldChampionship
                                )
                                Text(
                                    text = player.position,
                                    fontSize = 10.sp,
                                    color = TextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = player.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = player.team,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${player.ppg} PPG • ${player.threePct}% 3P",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ElectricCyan
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Step 2: Selected Player Stat Context Preview
        selectedPlayer?.let { player ->
            item {
                Text(
                    text = "2. Player Stat Profile for Generation",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("selected_player_ai_profile_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = player.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "${player.team} • Drafted ${player.draftYear} (Pick #${player.draftPick})",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .background(BlazeOrange.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${player.championships} RINGS 💍",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BlazeOrange
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats Matrix
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "PPG", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                Text(text = "${player.ppg}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = GoldChampionship)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "RPG", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                Text(text = "${player.rpg}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "APG", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                Text(text = "${player.apg}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = TextPrimary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "FG%", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                Text(text = "${player.fgPct}%", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ElectricCyan)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "3PT%", fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
                                Text(text = "${player.threePct}%", fontSize = 16.sp, fontWeight = FontWeight.Black, color = ElectricGreen)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Lore: ${player.notableFact}",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            lineHeight = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Error Banner if present
        errorMessage?.let { error ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CrimsonRed.copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonRed)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = CrimsonRed)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = error, color = TextPrimary, fontSize = 12.sp)
                    }
                }
            }
        }

        // Step 3: Trigger AI Generation CTA
        item {
            Button(
                onClick = {
                    selectedPlayer?.let { player ->
                        viewModel.startAiPlayerQuiz(player)
                    }
                },
                enabled = selectedPlayer != null && !isGenerating,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("generate_player_quiz_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BlazeOrange,
                    disabledContainerColor = CourtNavySurface
                )
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GEMINI GENERATING STAT TRIVIA...",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedPlayer != null) "GENERATE ${selectedPlayer?.name?.uppercase()} QUIZ" else "SELECT A PLAYER ABOVE",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = if (selectedPlayer != null) Color.White else TextMuted
                    )
                }
            }
        }
    }
}
