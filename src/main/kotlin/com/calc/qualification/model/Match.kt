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
        TODO("not yet implemented")
    }

    fun getResult(): Pair<Int, Int> {
        TODO("not yet implemented")
    }
}

@Serializable
data class MatchTeam(
    val country: String,
    val goals: Int? = null,
    val name: String,
)



