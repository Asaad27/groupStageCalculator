package com.calc.qualification.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class Client {

    companion object {
        private var client: HttpClient? = null

        private fun createClient(): HttpClient {
            return HttpClient(CIO) {
                expectSuccess = true
                install(HttpRequestRetry) {
                    retryOnServerErrors(maxRetries = 5)
                    exponentialDelay()
                }
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
                }
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.HEADERS
                    filter { request ->
                        request.url.host.contains("worldcupjson.net")
                    }
                }
            }
        }

        fun getInstance(): HttpClient {
            if (client == null) {
                client = createClient()
            }
            return client!!
        }


        fun close() = client?.close()
    }
}
