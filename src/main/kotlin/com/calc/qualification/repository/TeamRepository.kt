package com.calc.qualification.repository


import com.calc.qualification.model.Team

sealed interface TeamRepository{
    suspend fun getTeam(name: String) : Team
    suspend fun getAllTeams() : Collection<Team>
}