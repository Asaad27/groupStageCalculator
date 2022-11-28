package com.calc.qualification.dao

import com.calc.qualification.client.Client
import com.calc.qualification.model.Match
import io.ktor.client.call.*
import io.ktor.client.request.*

class MatchDaoApi : MatchDao {

    private val url = "https://worldcupjson.net/matches/"

    override suspend fun fetchAllMatches(): List<Match> {
        return Client
            .getInstance()
            .get(url)
            .body()
    }
}