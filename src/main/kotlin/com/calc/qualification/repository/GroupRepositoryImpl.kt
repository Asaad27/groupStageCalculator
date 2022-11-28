package com.calc.qualification.repository

import com.calc.qualification.dao.GroupDao
import com.calc.qualification.model.Group
import com.calc.qualification.model.Groups

class GroupRepositoryImpl(private val groupDao: GroupDao) : GroupRepository {
    override suspend fun getAllGroups(): Groups = groupDao.fetchAllGroups()

    override suspend fun getGroup(letter: String): Group =
       getAllGroups().groups.first { it.letter == letter }

    /**
     * @param teamName : name of the country i.e. Morocco
     */
    override suspend fun getGroupOfTeam(teamName: String): Group =
        getAllGroups().groups.first { group -> group.teams.map { it.name }.contains(teamName)}
}