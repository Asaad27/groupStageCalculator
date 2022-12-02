package com.calc.qualification.dataservice


import com.calc.qualification.dao.GroupDao
import com.calc.qualification.dao.TeamDao
import com.calc.qualification.model.Team

class TeamDataServiceImpl(private val teamDao: TeamDao, private val groupDao: GroupDao) : TeamDataService {

    //todo: cache data
    override suspend fun getTeam(countryCode: String): Team = teamDao.fetchTeam(countryCode)

    override suspend fun getTeamByName(name: String): Team {
        return getAllTeams()
            .first { it.name == name }
    }

    override suspend fun getAllTeams(): Collection<Team> = groupDao
        .fetchAllGroups()
        .groups
        .flatMap { it.teams }
}