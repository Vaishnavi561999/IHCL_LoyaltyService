package com.ihcl.qwikcilver.plugins

import com.ihcl.qwikcilver.service.serviceModule
import com.ihcl.qwikcilver.util.utilModule
import io.ktor.server.application.Application
import org.koin.core.context.startKoin

fun Application.configureDependencyInjection(){
    startKoin {
        modules(serviceModule, utilModule)
    }
}