package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ChallengesScreen
import com.example.ui.screens.HeadToHeadDuelScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LeaderboardScreen
import com.example.ui.screens.LiveStatsScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.theme.BlazeOrange
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CourtNavyDark
import com.example.ui.theme.CourtNavySurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.TriviaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.firebase.configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp(
    viewModel: TriviaViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    val showBottomBar = currentScreen in listOf(
        AppScreen.HOME,
        AppScreen.LIVE_STATS,
        AppScreen.CHALLENGES,
        AppScreen.LEADERBOARD,
        AppScreen.PROFILE
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = CourtNavyDark,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = CourtNavySurface,
                    contentColor = BlazeOrange,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .border(width = 1.dp, color = CardBorder)
                        .testTag("main_bottom_nav_bar")
                ) {
                    NavigationBarItem(
                        selected = currentScreen == AppScreen.HOME,
                        onClick = { viewModel.navigateTo(AppScreen.HOME) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Home",
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == AppScreen.HOME) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = BlazeOrange,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted,
                            indicatorColor = BlazeOrange
                        ),
                        modifier = Modifier.testTag("nav_item_home")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.CHALLENGES,
                        onClick = { viewModel.navigateTo(AppScreen.CHALLENGES) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.CHALLENGES) Icons.Filled.SportsEsports else Icons.Outlined.SportsEsports,
                                contentDescription = "Duels",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "1v1 Duels",
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == AppScreen.CHALLENGES) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = BlazeOrange,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted,
                            indicatorColor = BlazeOrange
                        ),
                        modifier = Modifier.testTag("nav_item_duels")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.LIVE_STATS,
                        onClick = { viewModel.navigateTo(AppScreen.LIVE_STATS) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.LIVE_STATS) Icons.Filled.Bolt else Icons.Outlined.Bolt,
                                contentDescription = "Live NBA",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Live NBA",
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == AppScreen.LIVE_STATS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = BlazeOrange,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted,
                            indicatorColor = BlazeOrange
                        ),
                        modifier = Modifier.testTag("nav_item_live_stats")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.LEADERBOARD,
                        onClick = { viewModel.navigateTo(AppScreen.LEADERBOARD) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.LEADERBOARD) Icons.Filled.Leaderboard else Icons.Outlined.Leaderboard,
                                contentDescription = "Leaderboard",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Rankings",
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == AppScreen.LEADERBOARD) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = BlazeOrange,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted,
                            indicatorColor = BlazeOrange
                        ),
                        modifier = Modifier.testTag("nav_item_rankings")
                    )

                    NavigationBarItem(
                        selected = currentScreen == AppScreen.PROFILE,
                        onClick = { viewModel.navigateTo(AppScreen.PROFILE) },
                        icon = {
                            Icon(
                                imageVector = if (currentScreen == AppScreen.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Profile",
                                fontSize = 10.sp,
                                fontWeight = if (currentScreen == AppScreen.PROFILE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = BlazeOrange,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted,
                            indicatorColor = BlazeOrange
                        ),
                        modifier = Modifier.testTag("nav_item_profile")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.QUIZ -> QuizScreen(viewModel = viewModel)
                AppScreen.RESULT -> ResultScreen(viewModel = viewModel)
                AppScreen.LIVE_STATS -> LiveStatsScreen(viewModel = viewModel)
                AppScreen.LEADERBOARD -> LeaderboardScreen(viewModel = viewModel)
                AppScreen.PROFILE -> ProfileScreen(viewModel = viewModel)
                AppScreen.CHALLENGES -> ChallengesScreen(viewModel = viewModel)
                AppScreen.HEAD_TO_HEAD_DUEL -> HeadToHeadDuelScreen(viewModel = viewModel)
                AppScreen.PLAYER_AI_QUIZ -> com.example.ui.screens.PlayerAiQuizScreen(viewModel = viewModel)
                AppScreen.COMPUTER_VS_PLAYER -> com.example.ui.screens.ComputerVsPlayerScreen(viewModel = viewModel)
            }
        }
    }
}
