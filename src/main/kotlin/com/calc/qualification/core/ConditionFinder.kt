package com.calc.qualification.core

import com.calc.qualification.dao.GroupDaoApi
import com.calc.qualification.dao.MatchDaoApi
import com.calc.qualification.dao.TeamDaoApi
import com.calc.qualification.model.Group
import com.calc.qualification.model.Match
import com.calc.qualification.model.MatchTeam
import com.calc.qualification.model.Team
import com.calc.qualification.repository.GroupRepositoryImpl
import com.calc.qualification.repository.MatchRepositoryImpl
import com.calc.qualification.repository.TeamRepositoryImpl
import kotlinx.coroutines.runBlocking

class ConditionFinder(private val teamName: String) {
    private val groupRepo = GroupRepositoryImpl(GroupDaoApi())
    private val teamRepo = TeamRepositoryImpl(TeamDaoApi(), GroupDaoApi())
    private val matchRepo = MatchRepositoryImpl(MatchDaoApi())

    private var group: Group
    private var team: Team

    private val goalLimit = 5
    private val resultsComb = generateResultCombinations()

    init {
        runBlocking {
            group = groupRepo.getGroupOfTeam(teamName)
            team = teamRepo.getTeamByName(teamName)
        }
        RankingUtil(matchRepo).sortGroup(group)
    }

    private fun applyMatchToGroup(groupHomeTeam: Team, homeTeam: MatchTeam, awayTeam: MatchTeam, pointsHomeAway: Pair<Int, Int>){
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

    private fun computeResultToMatch(result: Pair<Int, Int>, match: Match): Match {
        TODO("not yet implemented")
    }

    private fun generateAllCombinations(): Any{
        TODO("not yet implemented")
    }
    private fun generateResultCombinations(): List<Pair<Int, Int>>{
        val results = mutableListOf<Pair<Int, Int>>()
        for (i in 0..goalLimit){
            for (j in 0..i){
                results.add(i to j)
                if (i != j)
                    results.add(j to i)
            }
        }

        return results
    }
    private fun dfs(i: Int, remaining: MutableList<Match>, result: MutableList<List<Match>>){
        if (i == remaining.size){
            if (willQualify(teamName, result))
                result.add(remaining.toList())
            return
        }

        for (score in resultsComb) {
            if (score == remaining[i].getResult())
                continue
            val previous = remaining[i]
            remaining[i] = computeResultToMatch(score, remaining[i].clone())
            dfs(i+1, remaining, result)
            remaining[i] = previous
        }

    }

    private fun willQualify(teamName: String, result: MutableList<List<Match>>): Boolean {
        TODO("not yet implemented")
    }

    fun groupHasRemainingMatches() = group.teams.any { it.games_played < 4 }

}