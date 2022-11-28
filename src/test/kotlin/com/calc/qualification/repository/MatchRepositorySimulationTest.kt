package com.calc.qualification.repository

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class MatchRepositorySimulationTest {

    @Test
    fun getAllMatches() {
        val matchRepo: MatchRepository = MatchRepositorySimulation(emptyList())
        runBlocking {
            matchRepo.getAllMatchesOfTeam("MAR")
            TODO("implement")
        }
    }
}