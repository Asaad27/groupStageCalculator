package com.calc.qualification.dataservice


import com.calc.qualification.model.Group
import com.calc.qualification.model.Match

sealed interface MatchDataService {
    suspend fun getAllMatches(): List<Match>
    suspend fun getMatchResult(countryCodeA: String, countryCodeB: String): Pair<Int?, Int?>
    suspend fun getAllMatchesOfTeam(countryCode: String): List<Match>
    suspend fun getRemainingMatchesGroup(group: Group): List<Match>
    suspend fun getAllCompletedMatchesGroup(group: Group): List<Match>
    suspend fun getCurrentMatches(): List<Match>
    suspend fun getNextMatches(): List<Match>
    suspend fun getPrevMatches(): List<Match>

    suspend fun getTodayMatches(): List<Match>
}
