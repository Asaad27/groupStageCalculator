package com.calc.qualification.dataservice

import com.calc.qualification.dao.MatchDao
import com.calc.qualification.model.Group
import com.calc.qualification.model.Match
import com.calc.qualification.model.MatchStatus

class MatchDataServiceImpl(private val matchDao: MatchDao) : MatchDataService {

    override suspend fun getAllMatches(): List<Match> = matchDao.fetchAllMatches()

    override suspend fun getAllMatchesOfTeam(countryCode: String): List<Match> =
        getAllMatches()
            .filter { it.home_team.country == countryCode || it.away_team.country == countryCode }


    override suspend fun getRemainingMatchesGroup(group: Group): List<Match> {
        val matchSet = HashSet<Match>()
        group.teams.forEach {
            matchSet.addAll(getAllMatchesOfTeam(it.country).filter { match -> match.status != MatchStatus.completed })
        }

        return matchSet.toList()
    }

    override suspend fun getAllCompletedMatchesGroup(group: Group): List<Match> {
        val matchSet = HashSet<Match>()
        group.teams.forEach {
            matchSet.addAll(getAllMatchesOfTeam(it.country).filter { match: Match -> match.status == MatchStatus.completed })
        }

        return matchSet.toList()
    }

    override suspend fun getCurrentMatches(): List<Match> =
        matchDao.fetchCurrentMatches()


    override suspend fun getNextMatches(): List<Match> =
        matchDao.fetchMatchesSortedByDate()
            .filter { it.status == MatchStatus.future_scheduled }

    override suspend fun getPrevMatches(): List<Match> =
        matchDao.fetchMatchesSortedByDate()
            .dropLastWhile { it.status != MatchStatus.completed }

    override suspend fun getTodayMatches(): List<Match> =
        matchDao.fetchTodayMatches()


    override suspend fun getMatchResult(countryCodeA: String, countryCodeB: String): Pair<Int?, Int?> {
        return getAllMatchesOfTeam(countryCodeA)
            .filter { it.away_team.country == countryCodeB || it.home_team.country == countryCodeB }
            .map { (it.home_team.country to it.home_team.goals) to (it.away_team.country to it.away_team.goals) }
            .map { if (it.first.first == countryCodeA) return it.first.second to it.second.second else return it.second.second to it.first.second }
            .first<Pair<Int, Int>>()
    }
}