package com.calc.qualification.repository


import com.calc.qualification.model.Group
import com.calc.qualification.model.Match

sealed interface MatchRepository{
    suspend fun getAllMatches(): List<Match>
    suspend fun getMatchResult(countryCodeA: String, countryCodeB: String): Pair<Int?, Int?>
    suspend fun getAllMatchesOfTeam(countryCode: String): List<Match>
    suspend fun getRemainingMatchesGroup(group: Group): List<Match>
}