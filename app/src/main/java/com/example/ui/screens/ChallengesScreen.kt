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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.ChallengeStatus
import com.example.data.model.HeadToHeadChallenge
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengesScreen(
    viewModel: TriviaViewModel,
    modifier: Modifier = Modifier
) {
    val challenges by viewModel.challenges.collectAsState()
    val isMusicPlaying by viewModel.isAudioPlaying.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var opponentInput by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TriviaCategory.DAILY_CHALLENGE) }
    var selectedFilterTab by remember { mutableStateOf(0) } // 0: Incoming/Active, 1: History

    val incomingChallenges = challenges.filter { it.status == ChallengeStatus.PENDING || it.status == ChallengeStatus.ACCEPTED }
    val historyChallenges = challenges.filter { it.status == ChallengeStatus.COMPLETED || it.status == ChallengeStatus.DECLINED }
    val displayList = if (selectedFilterTab == 0) incomingChallenges else historyChallenges

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CourtNavyDark)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "HEAD-TO-HEAD DUELS",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = "Invite friends to 1v1 real-time trivia showdowns",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                // Generic Background Music Toggle Button
                IconButton(
                    onClick = { viewModel.toggleBackgroundMusic() },
                    modifier = Modifier
                        .background(if (isMusicPlaying) BlazeOrange.copy(alpha = 0.2f) else CourtNavySurface, CircleShape)
                        .border(1.dp, if (isMusicPlaying) BlazeOrange else CardBorder, CircleShape)
                        .testTag("toggle_background_music_btn")
                ) {
                    Icon(
                        imageVector = if (isMusicPlaying) Icons.Default.MusicNote else Icons.Default.MusicOff,
                        contentDescription = "Background Music",
                        tint = if (isMusicPlaying) BlazeOrange else TextSecondary
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Music Soundtrack Status Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleBackgroundMusic() }
                    .testTag("soundtrack_banner_card"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = if (isMusicPlaying) ElectricCyan else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isMusicPlaying) "Generic Arena Soundtrack: PLAYING" else "Generic Arena Soundtrack: MUTED",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMusicPlaying) ElectricCyan else TextPrimary
                            )
                            Text(
                                text = "136 BPM courtside synthesized pulse (AudioTrack)",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Text(
                        text = if (isMusicPlaying) "MUTE" else "PLAY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlazeOrange
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Create Challenge CTA Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("invite_challenge_action_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = CourtNavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, BlazeOrange.copy(alpha = 0.5f))
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
                                    .size(38.dp)
                                    .background(BlazeOrange.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SportsEsports,
                                    contentDescription = null,
                                    tint = BlazeOrange
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Challenge a Rival",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Send an invite to start a 5-shot duel",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = { showCreateDialog = !showCreateDialog },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange),
                            modifier = Modifier.testTag("open_invite_modal_button")
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (showCreateDialog) "CLOSE" else "INVITE", fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }

                    // Expandable Create Invite Form
                    AnimatedVisibility(visible = showCreateDialog) {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            OutlinedTextField(
                                value = opponentInput,
                                onValueChange = { opponentInput = it },
                                label = { Text("Opponent Username or Friend Handle") },
                                placeholder = { Text("e.g. KobeLegend_24") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("opponent_username_input"),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BlazeOrange,
                                    unfocusedBorderColor = CardBorder,
                                    focusedTextColor = TextPrimary,
                                    unfocusedTextColor = TextPrimary,
                                    cursorColor = BlazeOrange
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Select Challenge Arena Category:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                TriviaCategory.values().take(3).forEach { cat ->
                                    val isCatSelected = selectedCategory == cat
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isCatSelected) BlazeOrange.copy(alpha = 0.25f) else CourtNavySurface)
                                            .border(1.dp, if (isCatSelected) BlazeOrange else CardBorder, RoundedCornerShape(8.dp))
                                            .clickable { selectedCategory = cat }
                                            .padding(vertical = 8.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = cat.icon + " " + cat.name.split("_").first(),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCatSelected) BlazeOrange else TextSecondary,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Button(
                                onClick = {
                                    viewModel.sendChallenge(opponentInput, selectedCategory)
                                    opponentInput = ""
                                    showCreateDialog = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("send_challenge_confirm_button"),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange)
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("SEND MATCH INVITATION", fontWeight = FontWeight.Black, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Tabs: Active Requests vs Match History
        item {
            TabRow(
                selectedTabIndex = selectedFilterTab,
                containerColor = CourtNavySurface,
                contentColor = BlazeOrange,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedFilterTab]),
                        color = BlazeOrange
                    )
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedFilterTab == 0,
                    onClick = { selectedFilterTab = 0 },
                    text = {
                        Text(
                            text = "ACTIVE INVITATIONS (${incomingChallenges.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedFilterTab == 0) BlazeOrange else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_active_challenges")
                )
                Tab(
                    selected = selectedFilterTab == 1,
                    onClick = { selectedFilterTab = 1 },
                    text = {
                        Text(
                            text = "MATCH LOGS (${historyChallenges.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = if (selectedFilterTab == 1) BlazeOrange else TextSecondary
                        )
                    },
                    modifier = Modifier.testTag("tab_challenge_history")
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // List of Challenges
        if (displayList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (selectedFilterTab == 0) "No active challenges. Invite a friend above!" else "No completed match history yet.",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            items(displayList) { challenge ->
                ChallengeCard(
                    challenge = challenge,
                    onAccept = { viewModel.acceptChallenge(challenge) },
                    onDecline = { viewModel.declineChallenge(challenge.id) }
                )
            }
        }
    }
}

@Composable
private fun ChallengeCard(
    challenge: HeadToHeadChallenge,
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .testTag("challenge_card_${challenge.id}"),
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
                    Box(
                        modifier = Modifier
                            .background(CourtNavyDark, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = challenge.category.icon + " " + challenge.category.title,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricCyan
                        )
                    }
                }

                Text(
                    text = challenge.timestamp,
                    fontSize = 10.sp,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Matchup Names & Scores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${challenge.challengerName}  vs  ${challenge.opponentName}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = "5-Question Speed Match",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }

                if (challenge.status == ChallengeStatus.COMPLETED) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${challenge.opponentScore} - ${challenge.challengerScore}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = GoldChampionship
                        )
                        Text(
                            text = "Winner: ${challenge.winnerName ?: "Draw"}",
                            fontSize = 10.sp,
                            color = ElectricGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (challenge.status == ChallengeStatus.PENDING) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onAccept,
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("accept_challenge_button_${challenge.id}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BlazeOrange)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "ACCEPT & PLAY", fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDecline,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(44.dp)
                            .testTag("decline_challenge_button_${challenge.id}"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CourtNavySurface)
                    ) {
                        Text(text = "DECLINE", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
