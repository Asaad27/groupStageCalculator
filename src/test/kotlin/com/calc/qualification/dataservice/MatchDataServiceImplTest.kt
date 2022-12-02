package com.calc.qualification.dataservice

import com.calc.qualification.dao.GroupDaoApi
import com.calc.qualification.dao.MatchDaoApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class MatchDataServiceImplTest {

    private val matchRepo = MatchDataServiceImpl(MatchDaoApi())
    @Test
    fun getAllMatches() {
        runBlocking {
            val matches = matchRepo.getAllMatches()
            matches.forEach { println(it) }
        }
    }

    @Test
    fun getAllMatchesOfTeam() {
        runBlocking {
            val matches = matchRepo.getAllMatchesOfTeam("MAR")
            println(matches)
        }
    }

    @Test
    fun getMatchResult() {
        runBlocking {
            val result = matchRepo.getMatchResult("MAR", "BEL")
            val secondResult = matchRepo.getMatchResult("BEL", "MAR")
            assert(secondResult.first == result.second &&
            result.first == secondResult.second)
        }
    }

    @Test
    fun getRemainingMatchesGroup(){
        val groupRepo = GroupDataServiceImpl(GroupDaoApi())
        runBlocking {
            val result = matchRepo.getRemainingMatchesGroup(groupRepo.getGroup("F"))
            result.forEach { println(it) }
        }
    }

    @Test
    fun getTodayMatches(){
        runBlocking {
            val results = matchRepo.getTodayMatches().onEach { println(it) }
        }
    }

    @Test
    fun getCurrentMatches(){
        runBlocking {
            val results = matchRepo.getCurrentMatches().onEach { println(it) }
        }
    }

    @Test
    fun getNextMatches(){
        runBlocking {
            val results = matchRepo.getNextMatches().onEach { println(it) }
        }
    }

    @Test
    fun getPrevMatches(){
        runBlocking {
            val results = matchRepo.getPrevMatches().onEach { println(it) }
        }
    }

}