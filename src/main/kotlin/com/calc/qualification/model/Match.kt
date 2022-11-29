package com.calc.qualification.model

import kotlinx.serialization.Serializable

@Serializable
data class Match(
    val home_team: MatchTeam,
    val away_team: MatchTeam,
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
}

@Serializable
data class MatchTeam(
    val country: String,
    var goals: Int? = null,
    val name: String,
)



