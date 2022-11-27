package com.calc.qualification.repository


import com.calc.qualification.client.Client
import com.calc.qualification.model.Team
import io.ktor.client.call.*
import io.ktor.client.request.*

object TeamRepositoryImpl : TeamRepository {

    override suspend fun getTeam(countryCode: String): Team = Client
        .getInstance()
        .get("https://worldcupjson.net/teams/${countryCode}")
        .body()

    override suspend fun getAllTeams(): Collection<Team> = GroupRepositoryImpl
        .getAllGroups()
        .groups
        .flatMap { it.teams }
}