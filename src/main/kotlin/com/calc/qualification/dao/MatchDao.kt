package com.calc.qualification.dao

import com.calc.qualification.model.Match

sealed interface MatchDao {
    suspend fun fetchAllMatches(): List<Match>
}