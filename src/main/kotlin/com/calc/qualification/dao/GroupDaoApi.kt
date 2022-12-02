package com.calc.qualification.dao

import com.calc.qualification.client.Client
import com.calc.qualification.model.Groups
import io.ktor.client.call.*
import io.ktor.client.request.*

class GroupDaoApi : GroupDao {

    private val url = "https://worldcupjson.net/teams/"

    override suspend fun fetchAllGroups(): Groups = Client
        .getInstance()
        .get(url)
        .body()

}