package com.ihcl.qwikcilver.route

import com.ihcl.qwikcilver.dto.balanceenquiry.OriginalBalanceEnquiryRequest
import com.ihcl.qwikcilver.dto.balanceenquiry.validateOriginalBalanceEnquiryRequest
import com.ihcl.qwikcilver.dto.cancelRedeem.FECancelRedeemRequest
import com.ihcl.qwikcilver.dto.cancelRedeem.validateFECancelRedeemRequest
import com.ihcl.qwikcilver.dto.deactivate.FEDeactivateRes
import com.ihcl.qwikcilver.dto.deactivate.OriginalDeactivateRequest
import com.ihcl.qwikcilver.dto.deactivate.validateDeactivateRequest
import com.ihcl.qwikcilver.dto.redeem.OriginalRedeemRequest
import com.ihcl.qwikcilver.dto.redeem.validateRedeemRequest
import com.ihcl.qwikcilver.dto.reload.OriginalReloadRequest
import com.ihcl.qwikcilver.dto.reload.validateReloadRequest
import com.ihcl.qwikcilver.dto.reverseRedeem.FEReverseRedeemRequest
import com.ihcl.qwikcilver.dto.reverseRedeem.validateFEReverseRedeemRequest
import com.ihcl.qwikcilver.service.BalanceEnquiryService
import com.ihcl.qwikcilver.service.DeactivateService
import com.ihcl.qwikcilver.service.RedeemService
import com.ihcl.qwikcilver.service.ReloadService
import com.ihcl.qwikcilver.service.CancelRedeemService
import com.ihcl.qwikcilver.service.ReverseRedeemService
import com.ihcl.qwikcilver.util.Constants
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.ktor.server.routing.route
import io.ktor.server.routing.post
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.ktor.server.application.call
import io.ktor.server.request.receive
import kotlinx.serialization.encodeToString
import com.ihcl.qwikcilver.util.validateRequestBody
import io.ktor.http.*
import io.ktor.server.response.respond

fun Application.manageCardsRoute() {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    val deactivateService by KoinJavaComponent.inject<DeactivateService>(DeactivateService::class.java)
    val redeemService by KoinJavaComponent.inject<RedeemService>(RedeemService::class.java)
    val cancelRedeemService by KoinJavaComponent.inject<CancelRedeemService>(CancelRedeemService::class.java)
    val reverseRedeemService by KoinJavaComponent.inject<ReverseRedeemService>(ReverseRedeemService::class.java)
    val log: Logger = LoggerFactory.getLogger(javaClass)
    val reloadService by KoinJavaComponent.inject<ReloadService>(ReloadService::class.java)
    routing {
        route("/v1/qc") {

            //This route first validates the request from client and internally calls QC balance enquiry API to check balance and provides response.
            post("/balance-enquiry") {
                val request: OriginalBalanceEnquiryRequest = call.receive()
                log.debug("request received from FE is ${Json.encodeToString(request)}")
                val validateRequest = validateOriginalBalanceEnquiryRequest.validate(request)
                validateRequestBody(validateRequest)
                log.info("successfully validated request")
                val response = balanceEnquiryService.giftCardBalanceEnquiry(request)
                 balanceEnquiryService.validateBalanceEnquiry(call,response)
            }

            //This route first validates the request from client and internally calls QC reload API to reload the card balance and provides response.
            post("/reload-card") {
                val request: OriginalReloadRequest = call.receive()
                val validateRequest = validateReloadRequest.validate(request)
                validateRequestBody(validateRequest)
                val response = reloadService.reloadCard(request)
                reloadService.validateReload(call,response)
            }

            //This route first validates the request from client and internally calls QC redeem API to redeem GC and provides response.
            post("/redeem") {
                val request: OriginalRedeemRequest = call.receive()
                val validateRequest = validateRedeemRequest.validate(request)
                validateRequestBody(validateRequest)
                val response = redeemService.redeem(request)
                 redeemService.validateRedeem(call,response)
            }

            //This API is used to cancel the transaction of a gift card. This can be used for original transactions of Redeem.
            post("/cancel-redeem") {
                val request: FECancelRedeemRequest = call.receive()
                val validateRequest = validateFECancelRedeemRequest.validate(request)
                validateRequestBody(validateRequest)
                val response = cancelRedeemService.cancelRedeem(request)
                if (response.responseCode == Constants.QC.REQUEST_RESPONSECODE) {
                    call.respond(response)
                } else {
                    call.respond(HttpStatusCode.BadRequest, response)
                }
            }

            post("/reverse-redeem") {
                val request: FEReverseRedeemRequest = call.receive()
                val validateRequest = validateFEReverseRedeemRequest.validate(request)
                validateRequestBody(validateRequest)
                val response = reverseRedeemService.reverseRedeem(request)
                if (response.responseCode == Constants.QC.REQUEST_RESPONSECODE) {
                    call.respond(response)
                } else {
                    call.respond(HttpStatusCode.BadRequest, response)
                }
            }

            //This route first validates the request from client and internally calls QC deactivate API to deactivate the GC and provides response.
            post("/deactivate") {
                val request: OriginalDeactivateRequest = call.receive()
                val validateRequest = validateDeactivateRequest.validate(request)
                validateRequestBody(validateRequest)
                val response = deactivateService.deactivate(request)
                if (response?.ResponseCode == Constants.QC.REQUEST_RESPONSECODE) {
                    call.respond(response)
                } else {
                    call.respond(HttpStatusCode.BadRequest, response as FEDeactivateRes)
                }
            }
        }
    }
}