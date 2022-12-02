package com.calc.qualification.dao

import com.calc.qualification.model.Team

sealed interface TeamDao {
    suspend fun fetchTeam(countryCode: String): Team
}