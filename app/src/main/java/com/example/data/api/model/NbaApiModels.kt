package com.example.data.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NbaScoreboardResponse(
    @Json(name = "scoreboard") val scoreboard: NbaScoreboardData? = null
)

@JsonClass(generateAdapter = true)
data class NbaScoreboardData(
    @Json(name = "gameDate") val gameDate: String? = null,
    @Json(name = "games") val games: List<ApiGame>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class ApiGame(
    @Json(name = "gameId") val gameId: String,
    @Json(name = "gameStatusText") val gameStatusText: String? = null,
    @Json(name = "gameClock") val gameClock: String? = null,
    @Json(name = "period") val period: Int? = 0,
    @Json(name = "homeTeam") val homeTeam: ApiTeam,
    @Json(name = "awayTeam") val awayTeam: ApiTeam,
    @Json(name = "gameLeaders") val gameLeaders: ApiGameLeaders? = null
)

@JsonClass(generateAdapter = true)
data class ApiTeam(
    @Json(name = "teamId") val teamId: Int? = null,
    @Json(name = "teamName") val teamName: String? = null,
    @Json(name = "teamTricode") val teamTricode: String,
    @Json(name = "wins") val wins: Int? = 0,
    @Json(name = "losses") val losses: Int? = 0,
    @Json(name = "score") val score: Int? = 0
)

@JsonClass(generateAdapter = true)
data class ApiGameLeaders(
    @Json(name = "homeLeaders") val homeLeaders: ApiLeader? = null,
    @Json(name = "awayLeaders") val awayLeaders: ApiLeader? = null
)

@JsonClass(generateAdapter = true)
data class ApiLeader(
    @Json(name = "personId") val personId: Int? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "points") val points: Int? = 0,
    @Json(name = "rebounds") val rebounds: Int? = 0,
    @Json(name = "assists") val assists: Int? = 0
)

// Standings & Player Stat Leaders API Response Models
@JsonClass(generateAdapter = true)
data class NbaLeadersResponse(
    @Json(name = "leaders") val leaders: List<ApiPlayerStatLeader>? = emptyList()
)

@JsonClass(generateAdapter = true)
data class ApiPlayerStatLeader(
    @Json(name = "personId") val personId: Int,
    @Json(name = "name") val name: String,
    @Json(name = "team") val team: String,
    @Json(name = "category") val category: String,
    @Json(name = "statValue") val statValue: Double,
    @Json(name = "rank") val rank: Int
)
