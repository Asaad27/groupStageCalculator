package com.calc.qualification

import com.calc.qualification.core.ConditionFinder
import com.calc.qualification.model.Match
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.util.*


@ShellComponent
class WorldCupCommands {

    @Autowired
    var shellHelper: ShellHelper? = null

    @ShellMethod("Prints the needed results for a team to qualify.")
    fun qualify(@ShellOption("-t", "--team") nameOfTeam: String) {
        val teamName =
            nameOfTeam.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val conditionFinder = ConditionFinder(teamName)
        val qualified: List<List<Match>>

        println("computing results...")
        runBlocking {
            qualified = conditionFinder.findPossibleResults(teamName)

            //todo : get winners of the group
            println("=============results for $teamName to qualify==============")
            qualified.forEach { matchList ->
                matchList.forEach {
                    print(shellHelper!!.getColored("${it.getFormattedResult()} ", PromptColor.BLUE))
                }

                println(shellHelper!!.getColored(" or better result for $teamName", PromptColor.WHITE))
            }

        }
    }
}