package com.calc.qualification.client

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class Client {

    companion object {
        private var client: HttpClient? = null

        private fun createClient(): HttpClient {
            return HttpClient(CIO) {

                expectSuccess = true
                install(HttpRequestRetry) {
                    exponentialDelay()
                    retryIf { _, response ->
                        !response.status.isSuccess()
                    }
                    retryOnExceptionOrServerErrors(maxRetries = 5)
                    delayMillis { retry ->
                        retry * 3000L
                    }
                }
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    })
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
