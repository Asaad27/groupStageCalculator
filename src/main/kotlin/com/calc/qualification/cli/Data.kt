package com.calc.qualification.cli

import com.calc.qualification.dao.GroupDaoApi
import com.calc.qualification.dao.MatchDaoApi
import com.calc.qualification.dao.TeamDaoApi
import com.calc.qualification.dataservice.GroupDataServiceImpl
import com.calc.qualification.dataservice.MatchDataServiceImpl
import com.calc.qualification.dataservice.TeamDataServiceImpl
import kotlinx.coroutines.runBlocking


class Data {
    companion object {
        val groupRepo = GroupDataServiceImpl(GroupDaoApi())
        val matchRepo = MatchDataServiceImpl(MatchDaoApi())
        val teamRepo: TeamDataServiceImpl = TeamDataServiceImpl(TeamDaoApi(), GroupDaoApi())
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