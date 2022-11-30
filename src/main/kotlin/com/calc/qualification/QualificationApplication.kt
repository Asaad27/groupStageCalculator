package com.calc.qualification

import com.calc.qualification.core.ConditionFinder
import com.calc.qualification.model.Match
import kotlinx.coroutines.runBlocking
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import java.util.*

@SpringBootApplication
class QualificationApplication

fun main(args: Array<String>) {
    runApplication<QualificationApplication>(*args)
}

@ShellComponent
class WorldCupCommands{
    @ShellMethod("prints the needed results for a team to qualify")
    fun qualify(nameOfTeam: String){
        val teamName = nameOfTeam.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val conditionFinder = ConditionFinder(teamName)
        val qualified: List<List<Match>>

        println("computing results...")
        runBlocking {
            qualified = conditionFinder.findPossibleResults(teamName)

            //todo : get winners of the group
            println("=============results for $teamName to qualify==============")
            qualified.forEach { matchList ->
                matchList.forEach {
                    print("${it.getFormattedResult()}  ")
                }

                println(" or better result for $teamName")
            }

        }
    }
}
