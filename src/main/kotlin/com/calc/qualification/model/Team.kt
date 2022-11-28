package com.calc.qualification.model

import kotlinx.serialization.Serializable

@Serializable
data class Team(
    val name: String,
    val country: String,
    var group_points: Int,
    var wins: Int,
    var draws: Int,
    var losses: Int,
    var games_played: Int,
    var goals_for: Int,
    var goals_against: Int,
    var goal_differential: Int
) : Comparable<Team> {
    override fun compareTo(other: Team): Int {

        val comparator = compareBy<Team> { it.group_points }
            .thenBy { it.goal_differential }
            .thenBy { it.goals_for }

        return comparator.compare(this, other)
    }

}

