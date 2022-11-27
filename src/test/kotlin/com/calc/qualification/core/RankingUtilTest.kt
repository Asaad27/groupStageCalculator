package com.calc.qualification.core

import com.calc.qualification.model.Groups
import com.calc.qualification.repository.GroupRepositoryImpl
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset

internal class RankingUtilTest{

    private val groupRepo = GroupRepositoryImpl

    @Test
    fun sortAllGroupsTest() {
        val allGroups : Groups
        runBlocking {
            allGroups = groupRepo.getAllGroups()
        }
        for (group in allGroups.groups) {
            RankingUtil.sortGroup(group)
        }

        val encodedJson = Json.encodeToString(allGroups)
        val path = "src/test/kotlin/com/calc/qualification/core/standings.json"

        PrintWriter(FileWriter(path, Charset.defaultCharset()))
            .use { it.write(encodedJson) }

    }


}