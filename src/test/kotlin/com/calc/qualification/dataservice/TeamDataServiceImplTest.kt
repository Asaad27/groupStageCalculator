package com.calc.qualification.dataservice

import com.calc.qualification.dao.GroupDaoApi
import com.calc.qualification.dao.TeamDaoApi
import com.calc.qualification.model.Team
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class TeamDataServiceImplTest {

    private val teamRepo = TeamDataServiceImpl(TeamDaoApi(), GroupDaoApi())

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