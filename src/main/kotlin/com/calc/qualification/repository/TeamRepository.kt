package com.calc.qualification.repository


import com.calc.qualification.model.Team

sealed interface TeamRepository{
    suspend fun getTeam(countryCode: String) : Team
    suspend fun getTeamByName(name: String): Team
    suspend fun getAllTeams() : Collection<Team>
}