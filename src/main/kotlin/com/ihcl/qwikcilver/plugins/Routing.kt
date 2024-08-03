package com.ihcl.qwikcilver.plugins

import com.ihcl.qwikcilver.route.*
import io.ktor.server.application.Application
fun Application.configureRouting() {
    transactions()
    manageCardsRoute()
    buyingCardsRoute()
    configureGravtyRouting()
    configureNeuCoinsRoute()
}
