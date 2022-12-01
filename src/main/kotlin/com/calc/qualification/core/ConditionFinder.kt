package com.calc.qualification.core

import com.calc.qualification.dao.GroupDaoApi
import com.calc.qualification.dao.MatchDaoApi
import com.calc.qualification.dao.MatchDaoSimulation
import com.calc.qualification.dao.TeamDaoApi
import com.calc.qualification.model.Group
import com.calc.qualification.model.Match
import com.calc.qualification.model.MatchTeam
import com.calc.qualification.model.Team
import com.calc.qualification.repository.GroupRepositoryImpl
import com.calc.qualification.repository.MatchRepositoryImpl
import com.calc.qualification.repository.TeamRepositoryImpl
import kotlinx.coroutines.runBlocking
import java.util.*

class ConditionFinder(private val teamName: String) {
    private val groupRepo = GroupRepositoryImpl(GroupDaoApi())
    private val teamRepo = TeamRepositoryImpl(TeamDaoApi(), GroupDaoApi())
    private val matchRepo = MatchRepositoryImpl(MatchDaoApi())

    private var group: Group
    private var team: Team
    private var completedMatches: List<Match>

    private val goalLimit = 5
    private val resultsComb = generateResultCombinations()

    init {
        runBlocking {
            group = groupRepo.getGroupOfTeam(teamName)
            team = teamRepo.getTeamByName(teamName)
            completedMatches = matchRepo.getAllCompletedMatchesGroup(group)
            RankingUtil(matchRepo).sortGroup(group)
        }

    }

    private fun applyMatchToGroup(
        groupHomeTeam: Team,
        homeTeam: MatchTeam,
        awayTeam: MatchTeam,
        pointsHomeAway: Pair<Int, Int>
    ) {
        groupHomeTeam.apply {
            group_points += pointsHomeAway.first
            wins += if (pointsHomeAway.first == 3) 1 else 0
            draws += if (pointsHomeAway.first == 1) 1 else 0
            losses += if (pointsHomeAway.first == 0) 1 else 0
            games_played += 1
            goals_against += (awayTeam.goals ?: 0)
            goals_for += (homeTeam.goals ?: 0)
            goal_differential += ((homeTeam.goals ?: 0) - (awayTeam.goals ?: 0))
        }
    }

    private fun computeMatchToGroup(match: Match, group: Group) {
        val homeTeam = match.home_team
        val awayTeam = match.away_team
        val groupHomeTeam = group.teams.firstOrNull { it.country == homeTeam.country }
        val groupAwayTeam = group.teams.firstOrNull { it.country == awayTeam.country }
        val pointsHomeAway = RankingUtil.getPointsBetween(homeTeam.goals to awayTeam.goals)
        val pointAwayHome = pointsHomeAway.second to pointsHomeAway.first
        applyMatchToGroup(groupHomeTeam!!, homeTeam, awayTeam, pointsHomeAway)
        applyMatchToGroup(groupAwayTeam!!, awayTeam, homeTeam, pointAwayHome)
    }

    private fun computeResultToMatch(result: Pair<Int, Int>, match: Match) {
        match.apply {
            home_team.goals = result.first
            away_team.goals = result.second
            status = "completed"
            winner = when {
                result.first > result.second -> home_team.name
                result.first < result.second -> away_team.name
                else -> null
            }
            winner_code = when {
                result.first > result.second -> home_team.country
                result.first < result.second -> away_team.country
                else -> null
            }
        }
    }

    private fun generateResultCombinations(): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()
        for (i in goalLimit downTo 0)
            result.add(i to 0)
        for (i in 1..goalLimit)
            result.add(i to i)
        for (i in 1..goalLimit)
            result.add(0 to i)

        return result
    }

    suspend fun findPossibleResults(teamName: String): List<List<Match>> {
        val qualified = mutableListOf<List<Match>>()
        mutableListOf<List<Match>>()
        val remaining: MutableList<Match>
        runBlocking {
            remaining = matchRepo.getRemainingMatchesGroup(group).toMutableList()
        }

        when {
            remaining.isEmpty() -> throw Exception("no remaining match")
            remaining.size > 2 -> throw Exception("not yet implemented")
            remaining.size == 2 -> {
                if (remaining[0].isPlaying(teamName))
                    Collections.swap(remaining, 0, 1)
            }
        }

        val matchOfTeam = remaining.first { it.isPlaying(teamName) }
        if (matchOfTeam.home_team.name == teamName){
            matchOfTeam.swapTeams()
        }

        dfs(0, remaining, qualified)

        return qualified
    }

    private suspend fun dfs(i: Int, remaining: MutableList<Match>, qualified: MutableList<List<Match>>): Boolean {
        if (i == remaining.size) {
            val willQualify = willQualify(remaining)
            if (willQualify) {
                qualified.add(remaining.deepCopy())
            }

            return willQualify
        }

        for (score in resultsComb) {

            val previous = remaining[i].clone()
            computeResultToMatch(score, remaining[i])
            if (dfs(i + 1, remaining, qualified)) {
                break
            }
            remaining[i] = previous
        }

        return false
    }


    private suspend fun willQualify(matches: List<Match>): Boolean {

        if (matches.isEmpty())
            throw Exception("empty list of matches")

        val simulateGroup = group.clone()

        matches.forEach {
            computeMatchToGroup(it, simulateGroup)
        }

        val matchRepo = MatchRepositoryImpl(MatchDaoSimulation(matches + completedMatches))
        val rankingUtil = RankingUtil(matchRepo)
        rankingUtil.sortGroup(simulateGroup)

        val result = simulateGroup.teams

        val rankingOfTeam = result.indexOf(team)

        return rankingOfTeam <= 1
    }

}

private fun MutableList<Match>.deepCopy(): List<Match> {
    return this.map { it.clone() }.toList()
}
