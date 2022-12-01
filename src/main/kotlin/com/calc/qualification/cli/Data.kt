package com.calc.qualification.cli

import com.calc.qualification.dao.GroupDaoApi
import com.calc.qualification.dao.MatchDaoApi
import com.calc.qualification.dao.TeamDaoApi
import com.calc.qualification.repository.GroupRepositoryImpl
import com.calc.qualification.repository.MatchRepositoryImpl
import com.calc.qualification.repository.TeamRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component


class Data {
    companion object {
        val groupRepo = GroupRepositoryImpl(GroupDaoApi())
        val matchRepo = MatchRepositoryImpl(MatchDaoApi())
        val teamRepo: TeamRepositoryImpl = TeamRepositoryImpl(TeamDaoApi(), GroupDaoApi())
        var teamNames: List<String>
        var groupNames: List<String>

        init {
            runBlocking {
                teamNames = teamRepo.getAllTeams().map { it.name }.toList()
                groupNames = groupRepo.getAllGroups().groups.map { it.letter }.toList()
            }
        }
    }
}