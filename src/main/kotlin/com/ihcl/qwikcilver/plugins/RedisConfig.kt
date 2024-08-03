package com.ihcl.qwikcilver.plugins

import com.ihcl.qwikcilver.config.RedisConfig
import io.ktor.server.application.*

fun Application.configureRedisConfig(){
    RedisConfig
}