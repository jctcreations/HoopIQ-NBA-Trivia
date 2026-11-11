package com.example.data.datasource

import com.example.data.model.ComputerOpponent

object ComputerOpponentsDataSource {

    val opponents = listOf(
        ComputerOpponent(
            id = "bot_rookie",
            name = "Rookie Reese",
            title = "First-Round Lottery Pick",
            avatarIcon = "🧢",
            level = 1,
            baseAccuracyPct = 50,
            reactionSpeedSeconds = 9,
            isClutchSpecialist = false,
            loreDescription = "Still adjusting to NBA pace. Makes frequent rookie mistakes on vintage stat trivia."
        ),
        ComputerOpponent(
            id = "bot_sixth_man",
            name = "Bench Mob Spark",
            title = "Sixth Man of the Year",
            avatarIcon = "⚡",
            level = 2,
            baseAccuracyPct = 65,
            reactionSpeedSeconds = 7,
            isClutchSpecialist = false,
            loreDescription = "Reliable instant offense. Solid recall of modern box scores and All-Star rosters."
        ),
        ComputerOpponent(
            id = "bot_starter",
            name = "Franchise Starter",
            title = "All-Star Contender",
            avatarIcon = "🏀",
            level = 3,
            baseAccuracyPct = 78,
            reactionSpeedSeconds = 5,
            isClutchSpecialist = true,
            loreDescription = "Knows team lore inside out. Ramps up accuracy in the final seconds of the shot clock."
        ),
        ComputerOpponent(
            id = "bot_all_nba",
            name = "All-NBA Maestro",
            title = "All-NBA First Team",
            avatarIcon = "🔥",
            level = 4,
            baseAccuracyPct = 88,
            reactionSpeedSeconds = 4,
            isClutchSpecialist = true,
            loreDescription = "Elite basketball IQ. Fast buzzer-beater answers on playoff records and MVP seasons."
        ),
        ComputerOpponent(
            id = "bot_hall_of_fame",
            name = "HOF Legends AI",
            title = "Finals MVP & Hall of Fame",
            avatarIcon = "👑",
            level = 5,
            baseAccuracyPct = 96,
            reactionSpeedSeconds = 3,
            isClutchSpecialist = true,
            loreDescription = "God-tier basketball encyclopedic engine. Rarely misses a shot and punishes hesitation."
        )
    )

    fun getOpponentForLevel(level: Int): ComputerOpponent {
        val clamped = level.coerceIn(1, opponents.size)
        return opponents.firstOrNull { it.level == clamped } ?: opponents.first()
    }
}
