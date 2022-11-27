package com.calc.qualification.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val letter : String,
    val teams : MutableList<Team>,
)

@Serializable
data class Groups(
    val groups: MutableList<Group>
)
