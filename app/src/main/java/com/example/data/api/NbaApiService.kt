package com.example.data.api

import com.example.data.api.model.NbaLeadersResponse
import com.example.data.api.model.NbaScoreboardResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit NBA Data API Service
 * Interacts with public endpoints for live scoreboards and player stat leaderboards.
 */
interface NbaApiService {

    @GET("static/json/liveData/scoreboard/todaysScoreboard_00.json")
    suspend fun getTodaysScoreboard(): Response<NbaScoreboardResponse>

    @GET("stats/leaders")
    suspend fun getPlayerLeaders(
        @Query("statType") statType: String = "PTS",
        @Query("season") season: String = "2024-25"
    ): Response<NbaLeadersResponse>
}
