package com.calc.qualification.repository

import com.calc.qualification.model.Group
import com.calc.qualification.model.Groups
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class GroupRepositoryImplTest {

    private val groupRepo = GroupRepositoryImpl
    @Test
    fun getAllGroups() {
        val groups : Groups
        runBlocking {
            groups = groupRepo.getAllGroups()
        }
        groups.groups.forEach { println(it) }
    }

    @Test
    fun getGroup() {
        val group : Group
        runBlocking {
            group = groupRepo.getGroup("F")
        }
        println(group)
    }

    @Test
    fun getGroupOfTeam() {
        val group : Group
        runBlocking {
            group = groupRepo.getGroupOfTeam("Morocco")
        }
        println(group)
    }
}