package com.calc.qualification.repository

import com.calc.qualification.model.Team
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class TeamRepositoryImplTest {

    private val teamRepo = TeamRepositoryImpl()

    @Test
    fun getTeam() {
        val team: Team
        runBlocking {
            team = teamRepo.getTeam("MAR")
        }
        println(team)
    }

    @Test
    fun getAllTeams() {
        val teams: List<Team>
        runBlocking {
            teams = teamRepo.getAllTeams().toList()
        }
        assert(teams.size == 32)
    }

    @Test
    fun getTeamByName() {
        val teamByName: Team
        val teamByCode: Team
        runBlocking {
            teamByName = teamRepo.getTeamByName("Morocco")
            teamByCode = teamRepo.getTeam("MAR")
        }

        assert(teamByName == teamByCode)
    }
}