package com.example.data.datasource

import com.example.data.model.LeaderboardEntry

object LeaderboardDataSource {

    val dailyEntries = listOf(
        LeaderboardEntry(
            rank = 1,
            username = "SplashBrother99",
            score = 3850,
            streak = 12,
            accuracyPct = 100,
            timeTakenSeconds = 64,
            favoriteTeam = "GSW"
        ),
        LeaderboardEntry(
            rank = 2,
            username = "MambaMentality_24",
            score = 3720,
            streak = 10,
            accuracyPct = 100,
            timeTakenSeconds = 72,
            favoriteTeam = "LAL"
        ),
        LeaderboardEntry(
            rank = 3,
            username = "JokerPassMaster",
            score = 3590,
            streak = 9,
            accuracyPct = 90,
            timeTakenSeconds = 68,
            favoriteTeam = "DEN"
        ),
        LeaderboardEntry(
            rank = 4,
            username = "GreekFreak_Dunk",
            score = 3410,
            streak = 8,
            accuracyPct = 90,
            timeTakenSeconds = 81,
            favoriteTeam = "MIL"
        ),
        LeaderboardEntry(
            rank = 5,
            username = "BostonBanner18",
            score = 3280,
            streak = 7,
            accuracyPct = 85,
            timeTakenSeconds = 77,
            favoriteTeam = "BOS"
        ),
        LeaderboardEntry(
            rank = 6,
            username = "CourtGeneral",
            score = 2850,
            streak = 7,
            accuracyPct = 80,
            timeTakenSeconds = 85,
            favoriteTeam = "GSW",
            isCurrentUser = true
        ),
        LeaderboardEntry(
            rank = 7,
            username = "WembyBlockParty",
            score = 2650,
            streak = 6,
            accuracyPct = 80,
            timeTakenSeconds = 94,
            favoriteTeam = "SAS"
        ),
        LeaderboardEntry(
            rank = 8,
            username = "LukaMagic77",
            score = 2480,
            streak = 5,
            accuracyPct = 75,
            timeTakenSeconds = 99,
            favoriteTeam = "DAL"
        ),
        LeaderboardEntry(
            rank = 9,
            username = "ThunderUpShai",
            score = 2390,
            streak = 5,
            accuracyPct = 75,
            timeTakenSeconds = 104,
            favoriteTeam = "OKC"
        ),
        LeaderboardEntry(
            rank = 10,
            username = "SlimReaperKD",
            score = 2210,
            streak = 4,
            accuracyPct = 70,
            timeTakenSeconds = 110,
            favoriteTeam = "PHX"
        )
    )

    val allTimeEntries = listOf(
        LeaderboardEntry(
            rank = 1,
            username = "KobeLegend_8_24",
            score = 98450,
            streak = 45,
            accuracyPct = 96,
            timeTakenSeconds = 58,
            favoriteTeam = "LAL"
        ),
        LeaderboardEntry(
            rank = 2,
            username = "SplashBrother99",
            score = 87920,
            streak = 38,
            accuracyPct = 94,
            timeTakenSeconds = 62,
            favoriteTeam = "GSW"
        ),
        LeaderboardEntry(
            rank = 3,
            username = "JokerPassMaster",
            score = 82400,
            streak = 32,
            accuracyPct = 92,
            timeTakenSeconds = 66,
            favoriteTeam = "DEN"
        ),
        LeaderboardEntry(
            rank = 4,
            username = "TheLogoWest",
            score = 79150,
            streak = 29,
            accuracyPct = 91,
            timeTakenSeconds = 69,
            favoriteTeam = "LAL"
        ),
        LeaderboardEntry(
            rank = 5,
            username = "RedAuerbachCeltics",
            score = 74300,
            streak = 27,
            accuracyPct = 89,
            timeTakenSeconds = 74,
            favoriteTeam = "BOS"
        ),
        LeaderboardEntry(
            rank = 14,
            username = "CourtGeneral",
            score = 42250,
            streak = 14,
            accuracyPct = 82,
            timeTakenSeconds = 85,
            favoriteTeam = "GSW",
            isCurrentUser = true
        )
    )
}
