package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.data.model.TriviaCategory
import com.example.ui.components.LeaderboardRow
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
import com.example.ui.viewmodel.TriviaViewModel

@Composable
fun LeaderboardScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val dailyList by viewModel.dailyLeaderboard.collectAsState()
    val allTimeList by viewModel.allTimeLeaderboard.collectAsState()
    val selectedTab by viewModel.selectedLeaderboardTab.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    val currentList = if (selectedTab == 0) dailyList else allTimeList

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CourtNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "COMPETITIVE STANDINGS",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = "Global live ranking updated with every finished shot clock",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .background(GoldChampionship.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "DAY #42",
                        color = GoldChampionship,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Leaderboard Tabs (Daily vs All-Time)
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CourtNavySurface,
                contentColor = BlazeOrange,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = BlazeOrange
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { viewModel.setLeaderboardTab(0) },
                    text = {
                        Text(
                            text = "TODAY'S DAILY LEADERBOARD",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedTab == 0) BlazeOrange else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_daily_leaderboard")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { viewModel.setLeaderboardTab(1) },
                    text = {
                        Text(
                            text = "ALL-TIME LEGENDS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedTab == 1) BlazeOrange else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_alltime_leaderboard")
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Podium Preview for Top 3
        if (currentList.size >= 3) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("leaderboard_podium_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🏆 TOP THREE CONTENDERS",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldChampionship
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Rank 2 (Silver)
                            PodiumColumn(entry = currentList[1], rank = 2, medalColor = Color(0xFFC0C0C0), barHeight = 65)

                            // Rank 1 (Gold)
                            PodiumColumn(entry = currentList[0], rank = 1, medalColor = GoldChampionship, barHeight = 85)

                            // Rank 3 (Bronze)
                            PodiumColumn(entry = currentList[2], rank = 3, medalColor = Color(0xFFCD7F32), barHeight = 50)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Play To Climb CTA
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("climb_leaderboard_cta"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Want to claim the #1 spot?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Speed bonus gives +15 PTS per second remaining on shot clock!",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                    Button(
                        onClick = { viewModel.startQuiz(TriviaCategory.DAILY_CHALLENGE) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange),
                        modifier = Modifier.testTag("climb_now_button")
                    ) {
                        Text("COMPETE", fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Leaderboard List
        items(currentList) { entry ->
            LeaderboardRow(
                entry = entry,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun PodiumColumn(
    entry: com.example.data.model.LeaderboardEntry,
    rank: Int,
    medalColor: Color,
    barHeight: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(medalColor.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                color = medalColor,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = entry.username,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            maxLines = 1
        )
        Text(
            text = "${entry.score} pts",
            fontSize = 11.sp,
            color = medalColor,
            fontWeight = FontWeight.Black
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .width(60.dp)
                .height(barHeight.dp)
                .background(
                    Brush.verticalGradient(listOf(medalColor.copy(alpha = 0.6f), CourtNavyDark)),
                    RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
        )
    }
}
