package com.calc.qualification.core

import com.calc.qualification.dao.GroupDaoApi
import com.calc.qualification.dao.MatchDaoApi
import com.calc.qualification.model.Groups
import com.calc.qualification.dataservice.GroupDataServiceImpl
import com.calc.qualification.dataservice.MatchDataServiceImpl
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset

internal class RankingUtilTest{

    private val groupRepo = GroupDataServiceImpl(GroupDaoApi())


    @Test
     fun sortAllGroupsTest() {
        runBlocking {
            val allGroups : Groups
            val rankingUtil = RankingUtil(MatchDataServiceImpl(MatchDaoApi()))
            runBlocking {
                allGroups = groupRepo.getAllGroups()
            }
            for (group in allGroups.groups) {
                rankingUtil.sortGroup(group)
            }

            val encodedJson = Json.encodeToString(allGroups)
            val path = "src/test/kotlin/com/calc/qualification/core/standings.json"

            PrintWriter(FileWriter(path, Charset.defaultCharset()))
                .use { it.write(encodedJson) }
        }

    }


}