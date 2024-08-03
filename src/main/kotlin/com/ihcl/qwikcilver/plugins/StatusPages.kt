package com.ihcl.qwikcilver.plugins

import com.ihcl.qwikcilver.dto.BadRequest
import com.ihcl.qwikcilver.dto.InternalServerError
import com.ihcl.qwikcilver.dto.ValidationError
import com.ihcl.qwikcilver.exception.*
import com.ihcl.qwikcilver.exception.GravtyCardFetchingException
import com.ihcl.qwikcilver.exception.PrimaryCardCreationException
import com.ihcl.qwikcilver.exception.QCBadRequestException
import com.ihcl.qwikcilver.exception.QCInternalServerException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory

fun Application.statusPages() {
    val log = LoggerFactory.getLogger(javaClass)
    install(StatusPages) {
        exception<QCInternalServerException>{ call, cause ->
            log.error("Exception occurred while calling api"+cause.stackTraceToString())
            call.respond(HttpStatusCode.InternalServerError,InternalServerError(cause.message!!))
        }
        exception<QCBadRequestException> { call, cause ->
            log.error("Validation error occurred: ${cause.errorMessage}")
            call.respond(HttpStatusCode.BadRequest, ValidationError(
                cause.statusCode,
                cause.errorMessage,
                 cause.message
            ))
        }
        exception<Throwable> { call, cause ->
            log.error("InternalServerError error occurred: ${cause.message} ${cause.stackTraceToString()}")
            call.respond(HttpStatusCode.InternalServerError,InternalServerError(cause.message!!))

        }
        exception<PrimaryCardCreationException> { call, cause ->
            log.error("BadRequest error occurred: ${cause.message} ${cause.stackTraceToString()}")
            call.respond(HttpStatusCode.BadRequest, BadRequest(cause.message!!))

        }
        exception<GravtyCardFetchingException> { call, cause ->
            log.error("BadRequest error occurred: ${cause.message} ${cause.stackTraceToString()}")
            call.respond(HttpStatusCode.BadRequest, BadRequest(cause.message!!))

        }
        exception<BadRequestException> { call, cause ->
            log.error("BadRequest error occurred: ${cause.message} ${cause.stackTraceToString()}")
            call.respond(HttpStatusCode.BadRequest, BadRequest(cause.message!!))

        }
    }
}

