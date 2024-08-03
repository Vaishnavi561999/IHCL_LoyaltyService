package com.ihcl.qwikcilver.service

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.neucoins.request.GetNeuCoinsRequestDTO
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants.CLIENT_ID
import com.ihcl.qwikcilver.util.Constants.PARTNER_ID
import com.ihcl.qwikcilver.util.Constants.STORE_ID
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.slf4j.LoggerFactory
import java.nio.charset.StandardCharsets
import java.util.*

class LoyaltyTransactionHistory {

    private val log: org.slf4j.Logger? = LoggerFactory.getLogger(javaClass)
    val prop = Configuration.env
    val gson = Gson()
    suspend fun getTransactionHistory(request: GetNeuCoinsRequestDTO): HttpResponse? {
        log?.debug("request received to get transaction history is {}", request)
        val accessToken = decodeJwtToken(request.authorization!!)
         val getTransactionHistory ="${prop.loyaltyTransactionHistoryURL}?$PARTNER_ID=${prop.partnerId}"
        try {
            val response: HttpResponse = ConfigureClient.client.get(getTransactionHistory) {
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
            log?.debug("get loyalty transaction history response received  :  {}", response)
            return response
        } catch (e: Exception) {
            log?.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
        }
        return null
    }
     private fun decodeJwtToken(jwtToken:String):String{
         try {
             // Split the JWT into its three parts: header, payload, and signature
             val parts = jwtToken.split(".")
             val headerEncoded = parts.get(0)
             val payloadEncoded = parts.get(1)

             // Decode the header and payload from base64
             val header = String(Base64.getUrlDecoder().decode(headerEncoded), StandardCharsets.UTF_8)
             val payload = String(Base64.getUrlDecoder().decode(payloadEncoded), StandardCharsets.UTF_8)
             val jsonObject = gson.fromJson(payload, JsonObject::class.java)
             return jsonObject.get("accessToken").asString
         } catch (e: Exception) {
             throw QCInternalServerException("Incorrect Authorization token.....")
         }
    }
}