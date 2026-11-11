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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import com.example.ui.util.TriviaHaptics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun ProfileScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val profile by viewModel.userProfile.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CourtNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        item {
            Text(
                text = "PLAYER DOSSIER",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimary
            )
            Text(
                text = "Career trivia stats and unlocked accolades",
                fontSize = 11.sp,
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Profile Header Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_header_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                Brush.radialGradient(listOf(BlazeOrange, GoldChampionship)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(42.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = profile.username,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .background(BlazeOrange.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                            .border(1.dp, BlazeOrange, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = profile.tierTitle,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlazeOrange
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Representing: ${profile.favoriteTeam}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Career Numbers Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("career_numbers_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "CAREER TRIVIA METRICS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = GoldChampionship
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileMetricItem("TOTAL POINTS", "${profile.totalScore}", GoldChampionship)
                        ProfileMetricItem("GAMES PLAYED", "${profile.gamesPlayed}", TextPrimary)
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CardBorder))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ProfileMetricItem("DAILY BEST", "${profile.dailyBestScore}", ElectricCyan)
                        ProfileMetricItem("WIN RATE", "${profile.winRate}%", ElectricGreen)
                        ProfileMetricItem("BEST STREAK", "${profile.bestStreak}x 🔥", BlazeOrange)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tactile Feedback & Arena Controls Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tactile_feedback_settings_card"),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ElectricCyan.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Vibration,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Tactile Answer Haptics",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Crisp tap on correct • Dual-buzz on wrong",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(ElectricGreen.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                .border(1.dp, ElectricGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "ACTIVE",
                                color = ElectricGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Buttons to test tactile feedback
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { TriviaHaptics.vibrateCorrect(context, hapticFeedback) },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("test_correct_haptic_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricGreen.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = "✓ Test Swish",
                                color = ElectricGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { TriviaHaptics.vibrateIncorrect(context, hapticFeedback) },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("test_incorrect_haptic_btn"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = "✕ Test Brick",
                                color = CrimsonRed,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

        // Unlocked Accolades / Badges
        item {
            Text(
                text = "Unlocked Accolades",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            AccoladeRow(
                icon = "🎯",
                title = "Clutch Gene",
                description = "Answered 5 consecutive questions with under 3 seconds on the shot clock",
                isUnlocked = true
            )
        }
        item {
            AccoladeRow(
                icon = "🔥",
                title = "On Fire",
                description = "Achieved a 10-answer streak in competitive daily play",
                isUnlocked = true
            )
        }
        item {
            AccoladeRow(
                icon = "🏆",
                title = "Banner Raiser",
                description = "Placed in the daily top 10 leaderboard standings",
                isUnlocked = true
            )
        }
        item {
            AccoladeRow(
                icon = "👑",
                title = "GOAT Status",
                description = "Accumulate over 50,000 career trivia points",
                isUnlocked = false
            )
        }
    }
}

@Composable
private fun ProfileMetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 10.sp, color = TextMuted, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
    }
}

@Composable
private fun AccoladeRow(
    icon: String,
    title: String,
    description: String,
    isUnlocked: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) CourtNavyCard else CourtNavySurface.copy(alpha = 0.5f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isUnlocked) CardBorder else CardBorder.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(if (isUnlocked) BlazeOrange.copy(alpha = 0.2f) else CourtNavyDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUnlocked) TextPrimary else TextMuted
                    )
                    if (isUnlocked) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "UNLOCKED", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = ElectricGreen)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 11.sp,
                    color = if (isUnlocked) TextSecondary else TextMuted,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
