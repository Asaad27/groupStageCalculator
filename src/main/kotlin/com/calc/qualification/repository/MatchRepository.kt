package com.calc.qualification.repository


import com.calc.qualification.model.Match

sealed interface MatchRepository{
    suspend fun getAllMatches(): List<Match>
    suspend fun getMatchResult(teamA: String, teamB: String): Pair<Int?, Int?>
    suspend fun getAllMatchesOfTeam(team: String): List<Match>
}