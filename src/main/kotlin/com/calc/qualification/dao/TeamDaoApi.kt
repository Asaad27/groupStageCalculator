package com.calc.qualification.dao

import com.calc.qualification.client.Client
import com.calc.qualification.model.Team
import io.ktor.client.call.*
import io.ktor.client.request.*

class TeamDaoApi : TeamDao {

    private val url = "https://worldcupjson.net/teams/"

    override suspend fun fetchTeam(countryCode: String): Team {
        return Client.getInstance()
            .get(url + countryCode)
            .body()
    }

}