package com.calc.qualification.core

import com.calc.qualification.model.Match
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.TreeMap

internal class ConditionFinderTest {

    private val teamName = "Croatia"
    private val conditionFinder = ConditionFinder(teamName)


    @Test
    fun test() {

        val qualified: List<List<Match>>
        runBlocking {
            qualified = conditionFinder.findPossibleResults(teamName)

            //todo : get winners of the group
            println("=============to qualify==============")
            qualified.forEach { matchList ->
                matchList.forEach {
                    print("${it.getFormattedResult()}  ")
                }

                println(" or better result for $teamName")
            }

        }

    }
}