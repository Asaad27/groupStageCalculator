package com.calc.qualification.repository


import com.calc.qualification.dao.GroupDao
import com.calc.qualification.dao.TeamDao
import com.calc.qualification.model.Team

class TeamRepositoryImpl(private val teamDao: TeamDao, private val groupDao: GroupDao) : TeamRepository {

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