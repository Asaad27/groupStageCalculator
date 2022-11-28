package com.calc.qualification.core

import org.junit.jupiter.api.Test

internal class ConditionFinderTest {

    private val conditionFinder = ConditionFinder("Morocco")

    @Test
    fun groupHasRemainingMatches() {
        println(conditionFinder.groupHasRemainingMatches())
    }
}