package com.calc.qualification.dataservice


import com.calc.qualification.model.Team

sealed interface TeamDataService {
    suspend fun getTeam(countryCode: String): Team
    suspend fun getTeamByName(name: String): Team
    suspend fun getAllTeams(): Collection<Team>
}