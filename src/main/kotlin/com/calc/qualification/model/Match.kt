package com.calc.qualification.model

import kotlinx.serialization.Serializable


@Serializable
data class Match(
    val home_team: MatchTeam,
    val away_team: MatchTeam,
    val id: Int,
    val status: String,
    val winner: String? = null,
    val winner_code: String? = null
)

@Serializable
data class MatchTeam(
    val country: String,
    val goals: Int? = null,
    val name: String,
)



