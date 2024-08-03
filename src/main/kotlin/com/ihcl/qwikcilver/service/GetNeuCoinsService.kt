package com.ihcl.qwikcilver.service


import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.util.Base64
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.neucoins.response.GetLoyaltyPointsErrorResponseDTO
import com.ihcl.qwikcilver.dto.neucoins.response.GetNeuCoinsResponseDto
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants.CLIENT_ID
import com.ihcl.qwikcilver.util.Constants.STORE_ID
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class GetNeuCoinsService {
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val prop = Configuration.env
    val gson = Gson()
    suspend fun getPoints(authorization: String): HttpResponse? {
        log.debug("request received to get neu coins is $authorization")
        val token = authorization.split(" ").getOrNull(1) ?: throw QCInternalServerException("Invalid Token")
        val accessToken = decodeJwtToken(token)
        log.info("access token is $accessToken")
        val getLoyaltyPointsUrl = prop.getLoyaltyPointsUrl
        try {
            val response: HttpResponse = ConfigureClient.client.get(getLoyaltyPointsUrl) {
                headers {
                    append(CLIENT_ID, prop.tataNeuClient_Id)
                    append(STORE_ID, prop.tataNeuStore_Id)
                    append(HttpHeaders.Authorization, "Bearer $accessToken")
                }
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                contentType(ContentType.Application.Json)
            }
            log.debug("get loyalty points response received is {}", response)
            return response
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
        }
        return null
    }
    suspend fun validateNeucoins(call: ApplicationCall, response: HttpResponse?){
        if (response?.status == HttpStatusCode.OK) {
            call.respond(response.body() as GetNeuCoinsResponseDto)
        } else {
            call.respond(response!!.status, response.body() as GetLoyaltyPointsErrorResponseDTO)
        }
    }
    fun decodeJwtToken(jwtToken: String): String {

        // Split the JWT into its three parts: header, payload, and signature
        val parts = jwtToken.split(".")
        val headerEncoded = parts.get(0)
        val payloadEncoded = parts.get(1)

        // Decode the header and payload from base64
        val payload = String(Base64.getUrlDecoder().decode(payloadEncoded), StandardCharsets.UTF_8)
        val jsonObject = gson.fromJson(payload, JsonObject::class.java)

        return jsonObject.get("accessToken").asString
    }
}