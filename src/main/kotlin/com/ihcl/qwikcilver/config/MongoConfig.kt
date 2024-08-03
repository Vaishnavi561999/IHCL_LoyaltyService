package com.ihcl.qwikcilver.config

import com.ihcl.qwikcilver.model.QCAuthToken
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.connection.ConnectionPoolSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.CoroutineFindPublisher
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.concurrent.TimeUnit

class MongoConfig {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val prop = Configuration.env
    private var client: CoroutineClient
    private var database: CoroutineDatabase

    init {
        log.info("Loading Mongo Config")
        client = KMongo.createClient(
            MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(prop.connectionString))
                .applyToConnectionPoolSettings {
                    ConnectionPoolSettings.builder().maxConnectionIdleTime(120000, TimeUnit.MILLISECONDS)
                        .minSize(prop.connectionPoolMinSize.toInt()).maxSize(prop.connectionPoolMaxSize.toInt())
                }
                .applicationName("QwikCilver")
                .build()
                ).coroutine
        database = client.getDatabase(prop.databaseName)
    }

    fun getDatabase(): CoroutineDatabase {
        return database
    }
}

object TokenCollection {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private var tokenCollection: CoroutineFindPublisher<QCAuthToken>
    init {
        val db = MongoConfigFactory.getDatabase().getCollection<QCAuthToken>()
        tokenCollection = db.find()
        log.info("Token Collection initialized : $db")
    }
}
