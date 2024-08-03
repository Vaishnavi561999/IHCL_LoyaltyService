package com.ihcl.qwikcilver.route

import com.ihcl.qwikcilver.dto.neucoins.request.*
import com.ihcl.qwikcilver.dto.neucoins.response.*
import com.ihcl.qwikcilver.service.GetNeuCoinsService
import com.ihcl.qwikcilver.service.LoyaltyTransactionHistory
import com.ihcl.qwikcilver.service.RedeemNeuCoinsService
import com.ihcl.qwikcilver.service.ReverseNeuCoinsService
import com.ihcl.qwikcilver.util.Constants.AUTHORIZATION
import com.ihcl.qwikcilver.util.Constants.FETCH_NEUCOINS_HEADERKEY
import com.ihcl.qwikcilver.util.validateRequestBody
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.configureNeuCoinsRoute() {
    val log: Logger = LoggerFactory.getLogger(javaClass)
    val reverseNeuCoinsService by KoinJavaComponent.inject<ReverseNeuCoinsService>(ReverseNeuCoinsService::class.java)
    val redeemNeuCoinsService by KoinJavaComponent.inject<RedeemNeuCoinsService>(RedeemNeuCoinsService::class.java)
    val getNeuCoinsService by KoinJavaComponent.inject<GetNeuCoinsService>(GetNeuCoinsService::class.java)

    routing {
        route("/v1/neucoins") {
            post("/fetch-neucoins") {
                val authorization=call.request.headers[FETCH_NEUCOINS_HEADERKEY]
                log.info("Bearer token received to fetch neucoins is $authorization")
                val response = authorization?.let { it1 -> getNeuCoinsService.getPoints(it1) }
                getNeuCoinsService.validateNeucoins(call,response)
            }
            post("/redeem-neucoins") {
                val request = call.receive<RedeemNeuCoinsRequestDTO>()
                val validateRequest = validateRedeemNeuCoinsRequestDTO.validate(request)
                validateRequestBody(validateRequest)
                val authorization = call.request.headers[AUTHORIZATION]
                val storeId = call.request.headers["storeId"]
                val response = redeemNeuCoinsService.redeemNeuCoins(request, authorization, storeId.toString())
                if (response?.status == HttpStatusCode.OK) {
                    call.respond(response.body() as RedeemNeuCoinsResponseDTO)
                } else {
                    call.respond(response?.status!!, response.body() as GetLoyaltyPointsErrorResponseDTO)
                }
            }
            post("/reverse-neucoins") {
                val request = call.receive<ReverseNeuCoinRequestDTO>()
                val validateRequest = validateReverseNeuCoinRequestDTO.validate(request)
                validateRequestBody(validateRequest)
                val authorization = call.request.headers[AUTHORIZATION]
                log.info("JWTToken received to reverse neucoins is $authorization")
                val response = reverseNeuCoinsService.reverseCoins(request, authorization)
                when{
                    (response?.status == HttpStatusCode.OK)->{
                        call.respond(response.body() as ReverseNeuCoinsResponseDTO)
                    }
                    (response?.status == HttpStatusCode.Unauthorized)->{
                        call.respond(response.status, response.body() as GetLoyaltyPointsErrorResponseDTO)
                    }
                    (response?.status == HttpStatusCode.ServiceUnavailable)->{
                        call.respond(response.status, response.body() as GetLoyaltyPointsErrorResponseDTO)
                    }
                    else->{
                        call.respond(response?.status!!, response.body<ReverseResponseErrorDTO>())
                    }
                }
            }

            post("/transaction-fetch-neucoins") {
                val request = call.receive<GetNeuCoinsRequestDTO>()
                val response: HttpResponse? = LoyaltyTransactionHistory().getTransactionHistory(request)
                log.debug("Response  ${response?.bodyAsText()}")
                when (response?.status) {
                    HttpStatusCode.OK -> {
                        call.respond(response.body() as LoyaltyTransactionHistoryDTO)
                    }

                    HttpStatusCode.ServiceUnavailable -> {
                        call.respond(response.body() as LoyaltyTransactionHistoryError)
                    }

                    HttpStatusCode.Unauthorized -> {
                        call.respond(response.body() as LoyaltyTransactionHistoryUnauthorized)
                    }
                }
            }
        }
    }
}
