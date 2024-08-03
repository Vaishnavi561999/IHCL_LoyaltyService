package com.ihcl.qwikcilver.plugins

import com.google.gson.Gson
import com.ihcl.qwikcilver.config.Configuration
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.http.ContentType
import io.ktor.serialization.gson.gson
import io.ktor.serialization.gson.GsonConverter
import okhttp3.ConnectionPool
import okhttp3.Protocol
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

object ConfigureClient {
    val log: Logger = LoggerFactory.getLogger(javaClass)
    val prop = Configuration.env
    val client = HttpClient(OkHttp) {
        engine {
            config {
                // this: OkHttpClient.Builder
                connectionPool(ConnectionPool(100, 5, TimeUnit.MINUTES))
                readTimeout(prop.requestTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
                connectTimeout(prop.requestTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
                writeTimeout(prop.requestTimeoutMillis.toLong(), TimeUnit.MILLISECONDS)
                retryOnConnectionFailure(true)
                protocols(listOf(Protocol.HTTP_2, Protocol.HTTP_1_1))
                followRedirects(true)
            }
        }

        install(ContentNegotiation) {
            gson()
            register(contentType = ContentType.Text.Html, converter = GsonConverter(Gson()))
        }
        install(Logging) {
            level = LogLevel.ALL
        }
        install(HttpTimeout)
    }
}