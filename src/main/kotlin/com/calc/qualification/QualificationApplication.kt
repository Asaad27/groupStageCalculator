package com.calc.qualification

import com.calc.qualification.core.ConditionFinder
import com.calc.qualification.model.Match
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class QualificationApplication

fun main(args: Array<String>) {
    /** runApplication<QualificationApplication>(*args) **/
    val teamName = "Morocco"
    val conditionFinder = ConditionFinder(teamName)
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
