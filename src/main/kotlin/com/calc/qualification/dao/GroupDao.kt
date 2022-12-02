package com.calc.qualification.dao

import com.calc.qualification.model.Groups

sealed interface GroupDao {
    suspend fun fetchAllGroups(): Groups
}