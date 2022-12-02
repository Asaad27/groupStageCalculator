package com.calc.qualification.dao

import com.calc.qualification.client.Client
import com.calc.qualification.model.Match
import io.ktor.client.call.*
import io.ktor.client.request.*

class MatchDaoApi : MatchDao() {

    override suspend fun fetchAllMatches(): List<Match> {
        return Client
            .getInstance()
            .get(url)
            .body()
    }
}