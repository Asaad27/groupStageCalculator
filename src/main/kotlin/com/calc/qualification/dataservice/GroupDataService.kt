package com.calc.qualification.dataservice

import com.calc.qualification.model.Group
import com.calc.qualification.model.Groups

sealed interface GroupDataService {
    suspend fun getAllGroups(): Groups
    suspend fun getGroup(letter: String): Group
    suspend fun getGroupOfTeam(teamName: String): Group
}