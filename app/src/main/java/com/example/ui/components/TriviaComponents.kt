package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.model.GameStatus
import com.example.data.model.LeaderboardEntry
import com.example.data.model.LiveNbaGame
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

@Composable
fun LiveScoreTickerCard(
    game: LiveNbaGame,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(220.dp)
            .testTag("live_game_card_${game.gameId}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = CourtNavyCard
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
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
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${game.quarter} ${game.clock}",
                            color = CrimsonRed,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    } else {
                        Text(
                            text = game.quarter,
                            color = TextSecondary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    }
                }
                Text(
                    text = "NBA LIVE",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Score Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = game.awayTeam,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = game.homeTeam,
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = TextPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = game.awayScore.toString(),
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = if (game.awayScore > game.homeScore) GoldChampionship else TextPrimary
                    )
                    Text(
                        text = game.homeScore.toString(),
                        fontWeight = FontWeight.Black,
                        fontSize = 17.sp,
                        color = if (game.homeScore >= game.awayScore) GoldChampionship else TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CourtNavyDark, RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${game.topPerformer}: ${game.topStatLine}",
                    fontSize = 10.sp,
                    color = ElectricCyan,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: TriviaCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("category_card_${category.name.lowercase()}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Brush.linearGradient(listOf(BlazeOrange, GoldChampionship)),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = category.icon,
                    fontSize = 22.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.title,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.description,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun LeaderboardRow(
    entry: LeaderboardEntry,
    modifier: Modifier = Modifier
) {
    val rankColor = when (entry.rank) {
        1 -> GoldChampionship
        2 -> Color(0xFFC0C0C0)
        3 -> Color(0xFFCD7F32)
        else -> TextMuted
    }

    val backgroundColor = if (entry.isCurrentUser) {
        BlazeOrange.copy(alpha = 0.15f)
    } else {
        CourtNavyCard
    }

    val borderColor = if (entry.isCurrentUser) BlazeOrange else CardBorder

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("leaderboard_row_${entry.rank}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(rankColor.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${entry.rank}",
                    color = rankColor,
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.username,
                        color = if (entry.isCurrentUser) BlazeOrange else TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    if (entry.isCurrentUser) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .background(BlazeOrange, RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(text = "YOU", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Fav: ${entry.favoriteTeam} • ${entry.accuracyPct}% ACC • ${entry.timeTakenSeconds}s",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${entry.score}",
                    color = GoldChampionship,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp
                )
                Text(
                    text = "${entry.streak}x STREAK",
                    color = ElectricCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CountdownTimerBar(
    remainingSeconds: Int,
    totalSeconds: Int,
    modifier: Modifier = Modifier
) {
    val progress = (remainingSeconds.toFloat() / totalSeconds.coerceAtLeast(1)).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "timerProgress"
    )

    val progressColor = when {
        remainingSeconds <= 3 -> CrimsonRed
        remainingSeconds <= 7 -> GoldChampionship
        else -> ElectricCyan
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SHOT CLOCK",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${remainingSeconds}s",
                color = progressColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = progressColor,
            trackColor = CourtNavyDark
        )
    }
}
