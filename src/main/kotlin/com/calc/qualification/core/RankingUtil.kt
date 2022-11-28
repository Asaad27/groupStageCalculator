package com.calc.qualification.core

import com.calc.qualification.model.Group
import com.calc.qualification.model.Team
import com.calc.qualification.repository.MatchRepositoryImpl
import kotlinx.coroutines.runBlocking


/**
 * 1- points or
 * 2- superior goal difference in all group matches
 * 3- number of points obtained between tied teams
 * 4- goal difference between tied teams
 * 5- number of goals scored between tied teams
 */
class RankingUtil {
    companion object {
        private val matchRepo = MatchRepositoryImpl

        private suspend fun getPointsBetween(teamName1: String, teamName2: String): Pair<Int, Int> {
            val result = matchRepo.getMatchResult(teamName1, teamName2)
            return if (result.first == null || result.second == null)
                0 to 0
            else if (result.first!! > result.second!!)
                3 to 0
            else if (result.first == result.second)
                1 to 1
            else
                0 to 3
        }

        private suspend fun getGoalsBetween(teamName1: String, teamName2: String): Pair<Int?, Int?> {
            return matchRepo.getMatchResult(teamName1, teamName2)
        }

        fun sortGroup(group: Group) {
            group.teams.sort()
            runBlocking {
                tieSolver(group.teams)
            }
            group.teams.reverse()
        }

        private suspend fun tieSolver(teams: MutableList<Team>) {

            val tiedTeamSet = HashSet<Team>()
            teams.forEach { team ->
                teams.filterTo(tiedTeamSet) { it != team && it.compareTo(team) == 0 }
            }

            if (tiedTeamSet.isEmpty())
                return

            val teamsToPoints = tiedTeamSet.associateWith { 0 }.toMutableMap()
            val teamsToGoalDiff = tiedTeamSet.associateWith { 0 }.toMutableMap()
            val teamsToGoals = tiedTeamSet.associateWith { 0 }.toMutableMap()

            val tiedTeams = teams.subList(
                teams.indexOfFirst { tiedTeamSet.contains(it) },
                teams.indexOfLast { tiedTeamSet.contains(it) } + 1
            )

            for (team in tiedTeamSet) {
                var goalDiff = 0
                var goalScored = 0
                for (other in tiedTeamSet) {
                    if (team == other)
                        continue
                    val goals = getGoalsBetween(team.country, other.country)
                    goalDiff += (goals.first ?: 0) - (goals.second ?: 0)
                    goalScored += (goals.first ?: 0)
                    teamsToPoints[team] = teamsToPoints[team]!! + getPointsBetween(team.country, other.country).first
                }
                teamsToGoalDiff[team] = goalDiff
                teamsToGoals[team] = goalScored
            }

            tiedTeams.sortWith(
                compareBy({ teamsToPoints[it] },
                    { teamsToGoalDiff[it] },
                    { teamsToGoals[it] })
            )

        }
    }
}
