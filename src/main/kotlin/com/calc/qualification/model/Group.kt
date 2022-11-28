package com.calc.qualification.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val letter : String,
    val teams : MutableList<Team>,
){
    fun clone() : Group{
        val current = this
        return Group(current.letter, mutableListOf<Team>().apply { addAll(current.teams) })
    }
}

@Serializable
data class Groups(
    val groups: MutableList<Group>
)
