package com.calc.qualification

import com.calc.qualification.core.ConditionFinder
import com.calc.qualification.core.RankingUtil
import com.calc.qualification.dao.GroupDaoApi
import com.calc.qualification.dao.MatchDaoApi
import com.calc.qualification.dao.TeamDaoApi
import com.calc.qualification.model.Groups
import com.calc.qualification.model.Match
import com.calc.qualification.model.Team
import com.calc.qualification.repository.GroupRepositoryImpl
import com.calc.qualification.repository.MatchRepositoryImpl
import com.calc.qualification.repository.TeamRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.component.SingleItemSelector
import org.springframework.shell.component.support.SelectorItem
import org.springframework.shell.standard.AbstractShellComponent
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.util.*


@ShellComponent
class WorldCupCommands {

    @Autowired
    var shellHelper: ShellHelper? = null

    var groupRepo = GroupRepositoryImpl(GroupDaoApi())
    var matchRepo = MatchRepositoryImpl(MatchDaoApi())
    var teamRepo: TeamRepositoryImpl = TeamRepositoryImpl(TeamDaoApi(), GroupDaoApi())

    @ShellMethod("Prints the needed results for a team to qualify.")
    fun qualify(@ShellOption("-t", "--team", arity = 5) vararg nameOfTeams: String) {
        for (nameOfTeam in nameOfTeams) {
            val teamName =
                nameOfTeam.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val conditionFinder = ConditionFinder(teamName)
            val qualified: List<List<Match>>

            println("computing results...")
            runBlocking {
                qualified = conditionFinder.findPossibleResults(teamName)

                //todo : get winners of the group
                println("=============Results for $teamName to qualify:==============")
                qualified.forEach { matchList ->
                    matchList.forEach {
                        print(shellHelper!!.getColored("${it.getFormattedResult()} ", PromptColor.BLUE))
                    }

                    println(shellHelper!!.getColored(" or better result for $teamName", PromptColor.WHITE))
                }
            }
        }
    }

    @ShellMethod("List world cup team names.")
    fun ls(
        @ShellOption("--groups", "-g", arity = 0, defaultValue = "false") groupFlag: Boolean,
        @ShellOption("--teams", "-t", arity = 0, defaultValue = "true") teamsFlag: Boolean
    ) {

        var groups: Groups
        var teams: List<Team>

        if (teamsFlag && !groupFlag) {
            println("Getting teams...")

            runBlocking {
                teams = teamRepo.getAllTeams().toList()
            }

            if (teams.isEmpty())
                throw Exception("no team could be found")


            for (team in teams) {
                println(shellHelper!!.getColored(team.name, PromptColor.BLUE))
            }

        }
        if (groupFlag) {
            println("Getting groups...")
            runBlocking {
                groups = groupRepo.getAllGroups()
            }
            groups.groups.forEach {
                println(shellHelper!!.getColored("${it.letter} :", PromptColor.YELLOW))
                it.teams.forEach { team ->
                    println(shellHelper!!.getColored("\t${team.country}", PromptColor.BLUE))
                }
            }
        }
    }

    @ShellMethod("Lists groups standings")
    fun standings() {

        val allGroups : Groups
        val rankingUtil = RankingUtil(MatchRepositoryImpl(MatchDaoApi()))
        runBlocking {
            allGroups = groupRepo.getAllGroups()
            for (group in allGroups.groups) {
                rankingUtil.sortGroup(group)
            }
        }
        for (group in allGroups.groups) {
            println(shellHelper!!.getColored("=======${group.letter}=======", PromptColor.RED))
            println("Name\tPt\tGF\tGA\tGDF")
            for (team in group.teams){
                println(shellHelper!!.getColored("${team.country} \t${team.group_points} \t${team.goals_for} \t${team.goals_against} \t${team.goal_differential}",
                    PromptColor.BLUE
                ))
            }
        }
    }

}

