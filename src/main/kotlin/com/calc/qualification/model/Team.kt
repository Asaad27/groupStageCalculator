package com.calc.qualification.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val name: String,
    val country: String,
    val group_points: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val games_played: Int,
    val goals_for: Int,
    val goals_against: Int,
    val goal_differential: Int
) : Comparable<Team> {
    override fun compareTo(other: Team): Int {

        val comparator = compareBy<Team> { it.group_points }
            .thenBy { it.goal_differential }
            .thenBy { it.goals_for }

        return comparator.compare(this, other)
    }
}

