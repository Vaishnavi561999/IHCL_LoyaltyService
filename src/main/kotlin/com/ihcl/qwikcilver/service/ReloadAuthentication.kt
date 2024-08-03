package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.config.RedisConfig
import com.ihcl.qwikcilver.dto.auth.AuthRequest
import com.ihcl.qwikcilver.dto.auth.AuthResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.model.AuthToken
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ReloadAuthentication {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    //This function is responsible for calling QC authorize API which will return auth token in response and throws exception if any occurred.
    private suspend fun getAuthToken(): AuthResponse? {
        val requestBody = AuthRequest(
            terminalId = prop.reloadTerminalId,
            userName = prop.reloadUserName,
            password = prop.reloadPassword
        )
        val authorizationURL = prop.qcBaseUrl + Constants.QC.Path.AUTHORIZATION
        val dateAtClient: String = balanceEnquiryService.getDateAtClient()
        val response: HttpResponse
        try {
            response = ConfigureClient.client.post(authorizationURL) {
                headers {
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                }
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            log.debug("QC auth token response ${response.bodyAsText()}")
            return if (response.status == HttpStatusCode.OK) {
                response.body() as AuthResponse
            } else {
                response.body()
            }
        } catch (e: Exception) {
            log.error("Authorization error occurred while calling api is ${e.message}")
            throw QCInternalServerException(e.message)
        }
    }
    //This function first generates the autToken by calling getAuthToken function and updates the token in Redis with the new token generated.
    suspend fun generateAndUpdateToken(): String {
        val token = getAuthToken()
        val accessToken =  Constants.AUTH_TOKEN + token?.authToken as String
        RedisConfig.setKey(Constants.QC.RELOADTOKENKEY,accessToken)
        return accessToken
    }

    //This function is used to fetch the auth Token from Redis if token does not exist it calls the generateAndUpdateToken function.
    suspend fun getToken(): String {
        val token = RedisConfig.getKey(Constants.QC.RELOADTOKENKEY)
        log.debug("Reload token received from redis is $token")
        if (token.isNullOrBlank()) {
            return generateAndUpdateToken()
        }
        return token
    }
}