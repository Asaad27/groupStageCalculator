package com.calc.qualification.model

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    var home_team: MatchTeam,
    var away_team: MatchTeam,
    val id: Int,
    var status: String,
    var winner: String? = null,
    var winner_code: String? = null
){
    override fun equals(other: Any?): Boolean {
        return this.hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        return id
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

    fun getResult(): Pair<Int?, Int?> {
        return this.home_team.goals to this.away_team.goals
    }

    fun getFormattedResult(): String {
        val result = getResult()

        return "(${this.home_team.name} ${result.first} : ${result.second} ${this.away_team.name})"
    }

    fun isPlaying(teamName: String): Boolean {
        return this.home_team.name == teamName || this.away_team.name == teamName
    }

    fun isDraw(): Boolean {
        return this.home_team.goals == this.away_team.goals
    }

    fun swapTeams() {
        val temp = this.home_team
        this.home_team = away_team
        this.away_team = temp
    }

}

@Serializable
data class MatchTeam(
    val country: String,
    var goals: Int? = null,
    val name: String,
)



