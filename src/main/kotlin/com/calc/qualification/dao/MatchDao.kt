package com.calc.qualification.dao

import com.calc.qualification.client.Client
import com.calc.qualification.model.Match
import io.ktor.client.call.*
import io.ktor.client.request.*

abstract class MatchDao {
    abstract suspend fun fetchAllMatches(): List<Match>
    val url = "https://worldcupjson.net/matches/"

    suspend fun fetchCurrentMatches(): List<Match> {
        return Client
            .getInstance()
            .get(url + "current?details=true")
            .body()
    }

    suspend fun fetchTodayMatches(): List<Match> {
        return Client
            .getInstance()
            .get(url + "today?details=true")
            .body()
    }

    suspend fun fetchMatchesSortedByDate(): List<Match> {
        return Client
            .getInstance()
            .get(url) {
                parameter("by_date", "asc")
                parameter("details", "true")
            }.body()
    }


}