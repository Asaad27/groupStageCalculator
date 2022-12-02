package com.calc.qualification.model

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.*

@Serializable
data class Match(
    var home_team: MatchTeam,
    var away_team: MatchTeam,
    val id: Int,
    var status: MatchStatus,
    var winner: String? = null,
    var winner_code: String? = null,
    val datetime: String? = null,
    val detailed_time: MatchTime? = null,
    val home_team_lineup: TeamLineup? = null,
    val away_team_lineup: TeamLineup? = null,
    val home_team_events: List<MatchEvent>? = null,
    val away_team_event: List<MatchEvent>? = null
) {
    override fun equals(other: Any?): Boolean {
        return this.hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }

    override fun toString(): String {
        val result = getResult()
        return "${home_team.name} ${result.first ?: ""}:${result.second ?: ""} ${away_team.name} Time: ${detailed_time?.current_time ?: getDate()} Status : $status"
    }

    fun clone(): Match {
        return this.copy(
            id = id,
            status = status,
            winner = winner,
            winner_code = winner_code,
            home_team = home_team.copy(),
            away_team = away_team.copy()
        )
    }

    fun getDate(): Date =
        Date.from(Instant.parse(datetime))

    private fun getResult(): Pair<Int?, Int?> =
        this.home_team.goals to this.away_team.goals


    fun getFormattedResult(): String {
        val result = getResult()

        return "(${this.home_team.name} ${result.first} : ${result.second} ${this.away_team.name})"
    }

    fun isPlaying(teamName: String): Boolean = this.home_team.name == teamName || this.away_team.name == teamName

    fun isDraw(): Boolean = this.home_team.goals == this.away_team.goals

    fun swapTeams() {
        val temp = this.home_team
        this.home_team = away_team
        this.away_team = temp
    }

}

@Serializable
data class MatchTime(
    val current_time: String? = null,
    val first_half_time: String? = null,
    val first_half_extra_time: String? = null,
    val second_half_extra_time: String? = null,
    val second_half_time: String? = null
)

@Serializable
data class MatchTeam(
    val country: String,
    var goals: Int? = null,
    val name: String,
)

@Serializable
data class TeamLineup(
    val country: String? = null,
    val tactics: String? = null,
    val starting_eleven: List<Player>? = null,
    val substitutes: List<Player>? = null
) {
    override fun toString(): String {
        val sb = StringBuilder()
        starting_eleven?.forEach { sb.append(it.toString() + "\n") }
        return """
            $country: $tactics
            $sb
        """.trimIndent()
    }
}

@Serializable
data class Player(
    val shirt_number: Int? = null,
    val name: String? = null,
) {
    override fun toString(): String = "$shirt_number : $name"
}

@Serializable
data class MatchEvent(
    val id: Int,
    val type_of_event: String?,
    val player: String?,
    val time: String?
)


