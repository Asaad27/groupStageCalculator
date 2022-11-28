package com.calc.qualification.core

import com.calc.qualification.model.Group
import com.calc.qualification.model.Match
import com.calc.qualification.model.Team
import com.calc.qualification.repository.GroupRepositoryImpl
import com.calc.qualification.repository.MatchRepositoryImpl
import com.calc.qualification.repository.TeamRepositoryImpl
import kotlinx.coroutines.runBlocking

class ConditionFinder(private val teamName: String) {
    private val groupRepo = GroupRepositoryImpl()
    private val teamRepo = TeamRepositoryImpl()
    private val matchRepo = MatchRepositoryImpl()

    private lateinit var group: Group
    private lateinit var team: Team

    init {
        runBlocking {
            group = groupRepo.getGroupOfTeam(teamName)
            team = teamRepo.getTeamByName(teamName)
        }
        TODO("configure")
        //RankingUtil.sortGroup(group)
    }

    suspend fun resultsToQualify(): List<List<Match>> {
        if (!groupHasRemainingMatches())
            return emptyList()
        val results = mutableListOf(mutableListOf<Match>())
        val remainingMatch = matchRepo.getRemainingMatchesGroup(group)
        val current = mutableListOf<Match>()

        helper(current, remainingMatch, results)
        return emptyList()
    }

    private fun simulateMatch(first: Int, second: Int, match: Match){
        TODO("to implement")
       /* if (first > second){
            match.winner = match.home_team.name
            match.winner_code = match.home_team.country
        }else if (first < second){
            match.winner = match.away_team.name
            match.winner_code = match.away_team_team.country
        }
        match.status = "completed"
        match.home_team.goals += first
        match.home_team.*/
    }
    private fun helper(
        current: MutableList<Match>,
        remainingMatch: List<Match>,
        results: MutableList<MutableList<Match>>
    ) {
        TODO("not yet implemented")
    }

    fun groupHasRemainingMatches() = group.teams.any { it.games_played < 4 }

}