package com.calc.qualification.repository

import com.calc.qualification.model.Match

class MatchRepositorySimulation(private val matches :List<Match>
) : MatchRepository, MatchRepositoryImpl() {

    override suspend fun getAllMatches(): List<Match> {
        assert(1 == 5)
        return matches
    }

}