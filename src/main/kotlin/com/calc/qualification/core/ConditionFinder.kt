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
        }
        RankingUtil(matchRepo).sortGroup(group)
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
        val results = mutableListOf<Pair<Int, Int>>()
        results.add(0 to 0)
        for (i in 1..goalLimit) {
            results.add(i to 0)
            results.add(0 to i)
            if (i < 4)
                results.add(i to i)
        }

        return results
    }

    fun findPossibleResults(): Pair<List<List<Match>>, List<List<Match>>> {
        val qualified = mutableListOf<List<Match>>()
        val failed = mutableListOf<List<Match>>()
        val remaining: List<Match>
        runBlocking {
            remaining = matchRepo.getRemainingMatchesGroup(group)
        }

        if (remaining.isEmpty())
            throw Exception("no remaining match")

        dfs(0, remaining.toMutableList(), qualified, failed)

        return qualified to failed
    }

    private fun dfs(i: Int, remaining: MutableList<Match>, qualified: MutableList<List<Match>>, failed: MutableList<List<Match>>) {
        if (i == remaining.size) {
            if (willQualify(remaining))
                qualified.add(remaining.toList())
            else
                failed.add(remaining.toList())
            return
        }

        for (score in resultsComb) {
            if (score == remaining[i].getResult())
                continue
            val previous = remaining[i].clone()
            computeResultToMatch(score, remaining[i])
            dfs(i + 1, remaining, qualified, failed)
            remaining[i] = previous
        }

    }


    private fun willQualify(matches: List<Match>): Boolean {

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

    fun groupHasRemainingMatches() = group.teams.any { it.games_played < 4 }

}