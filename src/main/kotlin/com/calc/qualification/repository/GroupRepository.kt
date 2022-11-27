package com.calc.qualification.repository

import com.calc.qualification.model.Group
import com.calc.qualification.model.Groups

sealed interface GroupRepository {
    suspend fun getAllGroups(): Groups
    suspend fun getGroup(letter: String): Group
    suspend fun getGroupOfTeam(teamName: String): Group
}