package com.calc.qualification.core

import org.junit.jupiter.api.Test

internal class ConditionFinderTest {

    private val conditionFinder = ConditionFinder("Morocco")

    @Test
    fun generateAllCombinations(){
        val default = List(4) {0 to 0}
        val result = mutableListOf<List<Pair<Int, Int>>>()
        dfs(0, default, mutableListOf(), result)
        result.forEach { lis ->
            lis.forEach {
                print("(${it.first},${it.second})")
            }
            println()
        }
    }

    val combs = generatePairCombinations(5)
    fun dfs(i: Int, default: List<Pair<Int, Int>>, current: MutableList<Pair<Int, Int>>, result : MutableList<List<Pair<Int, Int>>>){
        if (i == default.size){
            result.add(current.toList())
            return
        }

        for (comb in combs) {
            if (comb == default[i])
                continue
            current.add(comb)
            dfs(i+1, default, current, result)
            current.removeLast()
        }
    }

    private fun generatePairCombinations(limit: Int): List<Pair<Int, Int>>{
        val result = mutableListOf<Pair<Int, Int>>()
        for (i in 0..limit){
            for (j in 0..i){
                result.add(i to j)
                if (i != j)
                    result.add(j to i)
            }
        }

        return result
    }

    @Test
    fun groupHasRemainingMatches() {
        println(conditionFinder.groupHasRemainingMatches())
    }
}