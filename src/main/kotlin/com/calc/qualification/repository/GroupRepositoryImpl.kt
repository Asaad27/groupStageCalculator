package com.calc.qualification.repository

import com.calc.qualification.client.Client
import com.calc.qualification.model.Group
import com.calc.qualification.model.Groups
import io.ktor.client.call.*
import io.ktor.client.request.*

class GroupRepositoryImpl : GroupRepository {
    override suspend fun getAllGroups(): Groups = Client
        .getInstance()
        .get("https://worldcupjson.net/teams/")
        .body()

    override suspend fun getGroup(letter: String): Group =
       getAllGroups().groups.first { it.letter == letter }

    /**
     * @param teamName : name of the country i.e. Morocco
     */
    override suspend fun getGroupOfTeam(teamName: String): Group =
        getAllGroups().groups.first { group -> group.teams.map { it.name }.contains(teamName)}
}