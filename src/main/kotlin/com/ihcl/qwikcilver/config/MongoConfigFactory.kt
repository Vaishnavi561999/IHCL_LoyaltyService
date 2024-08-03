package com.ihcl.qwikcilver.config

import org.litote.kmongo.coroutine.CoroutineDatabase

object MongoConfigFactory {

    private val mongoClient = MongoConfig()
    fun getDatabase(): CoroutineDatabase {
        return mongoClient.getDatabase()
    }
}

