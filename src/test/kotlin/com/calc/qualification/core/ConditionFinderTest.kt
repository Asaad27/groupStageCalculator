package com.calc.qualification.core

import com.calc.qualification.model.Match
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class ConditionFinderTest {

    private val conditionFinder = ConditionFinder("Morocco")


    @Test
    fun test() {
        val results : Pair<List<List<Match>>, List<List<Match>>>
        val qualified: List<List<Match>>
        val failed: List<List<Match>>
        runBlocking {
            results = conditionFinder.findPossibleResults()
            qualified = results.first
            failed = results.second

            println("=============to qualify==============")
            qualified.forEach { matchList ->
                matchList.forEach {
                    print("${it.getFormattedResult()}  ")
                }

                println()
            }

            println("=============to fail==============")
            failed.forEach { matchList ->
                matchList.forEach {
                    print("${it.getFormattedResult()}  ")
                }

                println()
            }
        }

    }
}