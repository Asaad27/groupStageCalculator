package com.calc.qualification.repository

import com.calc.qualification.client.Client
import com.calc.qualification.model.Match
import io.ktor.client.call.*
import io.ktor.client.request.*

object MatchRepositoryImpl : MatchRepository {
    override suspend fun getAllMatches(): List<Match> {
        return Client
            .getInstance()
            .get("https://worldcupjson.net/matches/")
            .body()
    }

    override suspend fun getAllMatchesOfTeam(team: String): List<Match> {
        return getAllMatches()
            .filter { it.home_team.country == team || it.away_team.country == team }
    }

    override suspend fun getMatchResult(teamA: String, teamB: String): Pair<Int?, Int?> {
        return getAllMatchesOfTeam(teamA)
            .filter { it.away_team.country == teamB || it.home_team.country == teamB }
            .map { (it.home_team.country to it.home_team.goals) to (it.away_team.country to it.away_team.goals) }
            .map {
                if (it.first.first == teamA){
                    return it.first.second to it.second.second
                }else{
                    return it.second.second to it.first.second
                }
            }.first<Pair<Int, Int>>()
    }
}