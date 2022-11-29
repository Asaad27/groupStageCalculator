package com.calc.qualification.dao

import com.calc.qualification.model.Match

class MatchDaoSimulation(private val matches : List<Match>): MatchDao {
    override suspend fun fetchAllMatches(): List<Match> {
        return matches
    }
}