package com.calc.qualification.cli

import com.calc.qualification.core.ConditionFinder
import com.calc.qualification.core.RankingUtil
import com.calc.qualification.dao.MatchDaoApi
import com.calc.qualification.dataservice.MatchDataServiceImpl
import com.calc.qualification.model.Groups
import com.calc.qualification.model.Match
import com.calc.qualification.model.MatchStatus
import com.calc.qualification.model.Team
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption
import java.util.*


@ShellComponent
class WorldCupCommands {

    @Autowired
    private val shellHelper: ShellHelper? = null

    @ShellMethod("Prints the needed results for a team to qualify.")
    fun qualify(
        @ShellOption(
            "-t",
            "--team",
            arity = 5,
            valueProvider = TeamValueProvider::class
        ) vararg nameOfTeams: String
    ) {
        for (nameOfTeam in nameOfTeams) {
            val teamName =
                nameOfTeam.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val conditionFinder = ConditionFinder(teamName)
            val qualified: List<List<Match>>

            println("computing results...")
            runBlocking {
                qualified = conditionFinder.findPossibleResults(teamName)


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
                teams = Data.teamRepo.getAllTeams().toList()
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
                groups = Data.groupRepo.getAllGroups()
            }
            groups.groups.forEach {
                println(shellHelper!!.getColored("${it.letter} :", PromptColor.YELLOW))
                it.teams.forEach { team ->
                    println(shellHelper.getColored("\t${team.country}", PromptColor.BLUE))
                }
            }
        }
    }

    @ShellMethod("Lists groups standings")
    fun standings() {

        val allGroups: Groups
        val rankingUtil = RankingUtil(MatchDataServiceImpl(MatchDaoApi()))
        runBlocking {
            allGroups = Data.groupRepo.getAllGroups()
            for (group in allGroups.groups) {
                rankingUtil.sortGroup(group)
            }
        }
        for (group in allGroups.groups) {
            println(shellHelper!!.getColored("${group.letter}:", PromptColor.RED))
            println("Name\tPt\tGF\tGA\tGDF")
            for (team in group.teams) {
                println(
                    shellHelper.getColored(
                        "${team.country} \t${team.group_points} \t${team.goals_for} \t${team.goals_against} \t${team.goal_differential}",
                        PromptColor.BLUE
                    )
                )
            }
        }
    }

    @ShellMethod("Lists matches")
    fun matches(
        @ShellOption("--today", "-t", arity = 0, defaultValue = "false") todayFlag: Boolean,
        @ShellOption("--current", "-c", arity = 0, defaultValue = "false") currentFlag: Boolean,
        @ShellOption("--next", "-n", arity = 1, defaultValue = ShellOption.NULL) next: Int?,
        @ShellOption("--prev", "-p", arity = 1, defaultValue = ShellOption.NULL) prev: Int?
    ) {

        if (!currentFlag && !todayFlag && next == null && prev == null) {
            runBlocking {
                println("Fetching All matches...")
                Data.matchRepo.getAllMatches().onEach {
                    println(
                        shellHelper!!.getColored(
                            it.toString(),
                            when (it.status) {
                                MatchStatus.completed -> PromptColor.YELLOW
                                MatchStatus.in_progress -> PromptColor.GREEN
                                MatchStatus.future_scheduled -> PromptColor.BLUE
                                else -> PromptColor.RED
                            }
                        )
                    )
                }
            }
        }

        if (currentFlag) {
            runBlocking {
                println("Fetching current matches...")
                Data.matchRepo.getCurrentMatches().onEach {
                    println(
                        shellHelper!!.getColored(
                            it.toString(),
                            when (it.status) {
                                MatchStatus.completed -> PromptColor.YELLOW
                                MatchStatus.in_progress -> PromptColor.GREEN
                                MatchStatus.future_scheduled -> PromptColor.BLUE
                                else -> PromptColor.RED
                            }
                        )
                    )
                }
            }

        }

        if (todayFlag) {
            runBlocking {
                println("Fetching today matches...")
                Data.matchRepo.getTodayMatches().onEach {
                    println(
                        shellHelper!!.getColored(
                            it.toString(),
                            when (it.status) {
                                MatchStatus.completed -> PromptColor.YELLOW
                                MatchStatus.in_progress -> PromptColor.GREEN
                                MatchStatus.future_scheduled -> PromptColor.BLUE
                                else -> PromptColor.RED
                            }
                        )
                    )
                }
            }
        }

        if (next != null) {
            runBlocking {
                println("Fetching next $next matches...")
                Data.matchRepo.getNextMatches().take(next).onEach {
                    println(
                        shellHelper!!.getColored(
                            it.toString(),
                            when (it.status) {
                                MatchStatus.completed -> PromptColor.YELLOW
                                MatchStatus.in_progress -> PromptColor.GREEN
                                MatchStatus.future_scheduled -> PromptColor.BLUE
                                else -> PromptColor.RED
                            }
                        )
                    )
                }
            }
        }

        if (prev != null) {
            runBlocking {
                println("Fetching previous $prev matches...")
                Data.matchRepo.getNextMatches().take(prev).onEach {
                    println(
                        shellHelper!!.getColored(
                            it.toString(),
                            when (it.status) {
                                MatchStatus.completed -> PromptColor.YELLOW
                                MatchStatus.in_progress -> PromptColor.GREEN
                                MatchStatus.future_scheduled -> PromptColor.BLUE
                                else -> PromptColor.RED
                            }
                        )
                    )
                }
            }
        }
    }

    // matches (--today  or --current or -next 3  -prev 4) [--details]
    // todo: matches select live details, but only after cache is added

}




