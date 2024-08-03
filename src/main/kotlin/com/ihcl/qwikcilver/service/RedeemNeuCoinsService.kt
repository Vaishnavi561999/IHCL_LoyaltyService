package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.neucoins.request.RedeemNeuCoinsRequestDTO
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants.CLIENT_ID
import com.ihcl.qwikcilver.util.Constants.STORE_ID
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RedeemNeuCoinsService {
    private val getNeuCoinsService by KoinJavaComponent.inject<GetNeuCoinsService>(GetNeuCoinsService::class.java)
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val prop = Configuration.env
    suspend fun redeemNeuCoins(request: RedeemNeuCoinsRequestDTO, authorization: String?, storeId: String): HttpResponse? {
        log.debug("Request received to redeem neu Coins is {} and auth token received as {}", request, authorization)
        val redeemNeuCoinsUrl = prop.redeemNeuCoinsUrl
        val accessToken = getNeuCoinsService.decodeJwtToken(authorization!!)
        try {
            val response: HttpResponse = ConfigureClient.client.post(redeemNeuCoinsUrl) {
                headers {
                    append(CLIENT_ID, prop.tataNeuClient_Id)
                    append(STORE_ID, storeId.lowercase())
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            log.debug("redeem neuCoins response received is ${response.bodyAsText()}")
            return response
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
        }
        return null
    }
}