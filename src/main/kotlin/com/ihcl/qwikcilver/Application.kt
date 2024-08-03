package com.ihcl.qwikcilver

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.plugins.configureMonitoring
import com.ihcl.qwikcilver.plugins.configureSerialization
import com.ihcl.qwikcilver.plugins.configureRouting
import com.ihcl.qwikcilver.plugins.configureRedisConfig
import com.ihcl.qwikcilver.plugins.CORSConfig
import com.ihcl.qwikcilver.plugins.statusPages
import com.ihcl.qwikcilver.plugins.configureDatabaseCollections
import com.ihcl.qwikcilver.route.configureNeuCoinsRoute
import com.ihcl.qwikcilver.plugins.configureDependencyInjection
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)
fun Application.module() {
    Configuration.initConfig(this.environment)
    configureMonitoring()
    configureSerialization()
    configureDependencyInjection()
    configureRouting()
    statusPages()
    CORSConfig()
    configureDatabaseCollections()
    configureNeuCoinsRoute()
    configureRedisConfig()
}