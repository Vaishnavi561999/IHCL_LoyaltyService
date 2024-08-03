package com.ihcl.qwikcilver.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*


fun Application.CORSConfig() {
    install(DefaultHeaders){
        header("Content-Security-Policy", "script-src ‘self’;")
        header("X-Content-Type-Options", "nosniff")
        header("X-XSS-Protection", "1")
     }
    install(CORS){
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader("Authorization")
        exposeHeader("Authorization")
    }
}