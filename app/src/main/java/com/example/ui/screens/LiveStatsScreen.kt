package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameStatus
import com.example.data.model.NbaPlayer
import com.example.data.model.TriviaCategory
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
import com.example.ui.viewmodel.TriviaViewModel

@Composable
fun LiveStatsScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val liveGames by viewModel.liveGames.collectAsState()
    val players by viewModel.players.collectAsState()
    val selectedPlayer by viewModel.selectedPlayerDetail.collectAsState()
    val syncStatus by viewModel.syncStatus.collectAsState()

    var selectedStatTab by remember { mutableStateOf(0) } // 0: Live Games, 1: Player Stat Leaders

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CourtNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Dedicated Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "NBA LIVE STATS DASHBOARD",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                    }
                    Text(
                        text = "Retrofit API Client • Real-Time Scoreboard & Leaders",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
                IconButton(
                    onClick = { viewModel.refreshScoresManually() },
                    modifier = Modifier.testTag("refresh_live_tab_btn")
                ) {
                    if (syncStatus.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = BlazeOrange,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh", tint = ElectricCyan)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Network API Connection Status Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("api_connection_badge_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (syncStatus.isLiveFromNetwork) Icons.Default.CloudDone else Icons.Default.CloudSync,
                            contentDescription = null,
                            tint = if (syncStatus.isLiveFromNetwork) ElectricGreen else ElectricCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (syncStatus.isLiveFromNetwork) "API Client Online (cdn.nba.com)" else "API Live Sync Active",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (syncStatus.isLiveFromNetwork) ElectricGreen else TextPrimary
                        )
                    }

                    Text(
                        text = "Updated: ${syncStatus.lastSyncTime}",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Tabs
        item {
            TabRow(
                selectedTabIndex = selectedStatTab,
                containerColor = CourtNavySurface,
                contentColor = BlazeOrange,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedStatTab]),
                        color = BlazeOrange
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedStatTab == 0,
                    onClick = { selectedStatTab = 0 },
                    text = {
                        Text(
                            text = "LIVE GAMES (${liveGames.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedStatTab == 0) BlazeOrange else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_live_games")
                )
                Tab(
                    selected = selectedStatTab == 1,
                    onClick = { selectedStatTab = 1 },
                    text = {
                        Text(
                            text = "PLAYER STAT LEADERS (${players.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedStatTab == 1) BlazeOrange else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_player_stats")
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Active Quiz Launch CTA from stats
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.startQuiz(TriviaCategory.LIVE_STATS) }
                    .testTag("live_stats_quiz_banner"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(BlazeOrange.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.SportsBasketball, contentDescription = null, tint = BlazeOrange)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Test Your Live Stat IQ",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TextPrimary
                            )
                            Text(
                                text = "Play live questions drawn directly from these box scores",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Text(
                        text = "PLAY ⚡",
                        color = GoldChampionship,
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (selectedStatTab == 0) {
            // Live Games List
            items(liveGames) { game ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .testTag("live_stat_game_${game.gameId}"),
                    shape = RoundedCornerShape(14.dp),
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
                                if (game.status == GameStatus.LIVE) {
                                    Icon(
                                        imageVector = Icons.Default.FiberManualRecord,
                                        contentDescription = "Live",
                                        tint = CrimsonRed,
                                        modifier = Modifier.size(10.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${game.quarter} • ${game.clock}",
                                        color = CrimsonRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                } else {
                                    Text(
                                        text = game.quarter,
                                        color = TextSecondary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Text(
                                text = "OFFICIAL NBA FEED",
                                color = TextMuted,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = game.awayTeam,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                                Text(
                                    text = game.homeTeam,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TextPrimary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = game.awayScore.toString(),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (game.awayScore > game.homeScore) GoldChampionship else TextPrimary
                                )
                                Text(
                                    text = game.homeScore.toString(),
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (game.homeScore >= game.awayScore) GoldChampionship else TextPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CourtNavyDark, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Game Leader: ${game.topPerformer}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricCyan
                                )
                                Text(
                                    text = game.topStatLine,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Player Stat Leaders
            items(players) { player ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .clickable { viewModel.selectPlayerForDetail(if (selectedPlayer?.id == player.id) null else player) }
                        .testTag("player_row_${player.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(BlazeOrange.copy(alpha = 0.2f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "#${player.jerseyNumber}",
                                        color = BlazeOrange,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = player.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "${player.team} • ${player.position}",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${player.ppg} PPG",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = GoldChampionship
                                )
                                Text(
                                    text = "${player.rpg} RPG • ${player.apg} APG",
                                    fontSize = 11.sp,
                                    color = ElectricCyan
                                )
                            }
                        }

                        // Expanded Stat Sheet for trivia study
                        AnimatedVisibility(visible = selectedPlayer?.id == player.id) {
                            Column(modifier = Modifier.padding(top = 12.dp)) {
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    PlayerMiniStat("FG%", "${player.fgPct}%")
                                    PlayerMiniStat("3PT%", "${player.threePct}%")
                                    PlayerMiniStat("FT%", "${player.ftPct}%")
                                    PlayerMiniStat("RINGS", "${player.championships} 💍")
                                    PlayerMiniStat("ALL-STAR", "${player.allStarAppearances}x")
                                }

                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Trivia Note: ${player.notableFact}",
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerMiniStat(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 9.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
    }
}
