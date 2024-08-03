package com.ihcl.qwikcilver.route

import com.ihcl.qwikcilver.dto.preauthcancel.OriginalPreauthCancelRequest
import com.ihcl.qwikcilver.dto.preauthcancel.validatePreauthCancelRequest
import com.ihcl.qwikcilver.dto.preauthcompletecancel.OriginalPreauthCompleteCancel
import com.ihcl.qwikcilver.dto.preauthcompletecancel.validatePreauthCompleteCancel
import com.ihcl.qwikcilver.dto.preauthcomplete.OriginalPreauthCompleteRequest
import com.ihcl.qwikcilver.dto.preauthcomplete.validatePreauthCompleteRequest
import com.ihcl.qwikcilver.dto.preauth.OriginalPreauthRequest
import com.ihcl.qwikcilver.dto.preauth.validatePreauthRequest
import com.ihcl.qwikcilver.dto.preauthreversal.OriginalPreauthReversalRequest
import com.ihcl.qwikcilver.dto.preauthreversal.validatePreauthReversalRequest
import com.ihcl.qwikcilver.service.*
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.validateRequestBody
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.transactions() {
    val preAuthReversalService by KoinJavaComponent.inject<PreauthReversalService>(PreauthReversalService::class.java)
    val preAuthCompleteCancelService by KoinJavaComponent.inject<PreauthCompleteCancelService>(
        PreauthCompleteCancelService::class.java
    )
    val preauthCompleteReversalService by KoinJavaComponent.inject<PreauthCompleteReversalService>(
        PreauthCompleteReversalService::class.java
    )
    val preauthCancelService by KoinJavaComponent.inject<PreauthCancelService>(PreauthCancelService::class.java)
    val preauthService by KoinJavaComponent.inject<PreauthService>(PreauthService::class.java)
    val preauthCompleteService by KoinJavaComponent.inject<PreauthCompleteService>(PreauthCompleteService::class.java)
    val log: Logger = LoggerFactory.getLogger(javaClass)

    routing {
        route("/v1/qc") {
            //This route first validates the request and internally calls QC PreAuth API to block the card amount and provides response.
            post("/preauth") {
                val request: OriginalPreauthRequest = call.receive()
                val validateRequest = validatePreauthRequest.validate(request)
                validateRequestBody(validateRequest)
                val preAuthResponse = preauthService.preAuth(request)
                call.respond(preauthService.validateResponse(preAuthResponse), preAuthResponse)
            }
            //This route first validates the request and internally calls QC PreAuthComplete API to complete the blocked amount of the card and provides response.
            post("/preauth-complete") {
                val request: OriginalPreauthCompleteRequest = call.receive()
                log.debug("request body of preauth complete is {}", request)
                val validateRequest = validatePreauthCompleteRequest.validate(request)
                validateRequestBody(validateRequest)
                val preAuthCompleteResponse = preauthCompleteService.preAuthComplete(request)
                call.respond(preauthService.validateResponse(preAuthCompleteResponse), preAuthCompleteResponse)
            }

            //This route first validates the request and internally calls QC PreAuthCancel API and provides response.
            post("/preauth-cancel") {
                val request: OriginalPreauthCancelRequest = call.receive()
                val validateRequest = validatePreauthCancelRequest.validate(request)
                validateRequestBody(validateRequest)
                val preAuthCancelResponse = preauthCancelService.preAuthCancel(request)
                call.respond(preauthService.validateResponse(preAuthCancelResponse), preAuthCancelResponse)
            }

            //This route first validates the request and internally calls QC PreAuthComplete reversal API and provides response.
            post("/preauth-complete-reversal") {
                val request: OriginalPreauthReversalRequest = call.receive()
                val validateRequest = validatePreauthReversalRequest.validate(request)
                validateRequestBody(validateRequest)
                val response = preauthCompleteReversalService.preAuthCompleteReversal(request)
                if (response.ResponseCode?.toInt() == Constants.QC.REQUEST_RESPONSECODE) {
                    call.respond(response)
                } else  {
                    call.respond(HttpStatusCode.BadRequest, response)
                }
            }

            //This route first validates the request and internally calls QC PreAuth reversal API and provides response.
            post("/preauth-reversal") {
                val request: OriginalPreauthReversalRequest = call.receive()
                val validateRequest = validatePreauthReversalRequest.validate(request)
                validateRequestBody(validateRequest)
                val response = preAuthReversalService.preAuthReversal(request)
                if (response.ResponseCode?.toInt() == Constants.QC.REQUEST_RESPONSECODE) {
                    call.respond(response)
                } else   {
                    call.respond(HttpStatusCode.BadRequest, response)
                }
            }

            //This route first validates the request and internally calls QC PreAuthComplete cancel API and provides response.
            post("/preauth-complete-cancel") {
                val request: OriginalPreauthCompleteCancel = call.receive()
                val validateRequest = validatePreauthCompleteCancel.validate(request)
                validateRequestBody(validateRequest)
                val preAuthCompleteCancelResponse = preAuthCompleteCancelService.preAuthCompleteCancel(request)
                call.respond(
                    preauthService.validateResponse(preAuthCompleteCancelResponse),
                    preAuthCompleteCancelResponse
                )

            }

        }
    }
}