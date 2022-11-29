package com.calc.qualification.repository

import com.calc.qualification.dao.MatchDao
import com.calc.qualification.model.Group
import com.calc.qualification.model.Match

open class MatchRepositoryImpl(private val matchDao: MatchDao) : MatchRepository {

    override suspend fun getAllMatches(): List<Match> = matchDao.fetchAllMatches()

    override suspend fun getAllMatchesOfTeam(countryCode: String): List<Match> {
        return getAllMatches()
            .filter { it.home_team.country == countryCode || it.away_team.country == countryCode }
    }

    override suspend fun getRemainingMatchesGroup(group: Group): List<Match> {
        val matchSet = HashSet<Match>()
        group.teams.forEach {
            matchSet.addAll(getAllMatchesOfTeam(it.country).filter { match -> match.status != "completed" })
        }

        return matchSet.toList()
    }

    override suspend fun getAllCompletedMatchesGroup(group: Group): List<Match> {
        val matchSet = HashSet<Match>()
        group.teams.forEach {
            matchSet.addAll(getAllMatchesOfTeam(it.country).filter { match: Match -> match.status == "completed" })
        }

        return matchSet.toList()
    }

    override suspend fun getMatchResult(countryCodeA: String, countryCodeB: String): Pair<Int?, Int?> {
        return getAllMatchesOfTeam(countryCodeA)
            .filter { it.away_team.country == countryCodeB || it.home_team.country == countryCodeB }
            .map { (it.home_team.country to it.home_team.goals) to (it.away_team.country to it.away_team.goals) }
            .map { if (it.first.first == countryCodeA) return it.first.second to it.second.second else return it.second.second to it.first.second }
            .first<Pair<Int, Int>>()
    }
}