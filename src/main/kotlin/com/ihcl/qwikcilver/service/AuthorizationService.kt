package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.config.RedisConfig
import com.ihcl.qwikcilver.dto.auth.AuthRequest
import com.ihcl.qwikcilver.dto.auth.AuthResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.model.AuthToken
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.Constants.GRAVTY_TOKEN
import com.ihcl.qwikcilver.util.Constants.JWT_TOKEN
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.HttpStatusCode
import org.koin.java.KoinJavaComponent

class AuthorizationService {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    //This function is responsible for calling QC authorize API which will return auth token in response and throws exception if any occurred.
    private suspend fun getAuthToken(): AuthResponse? {
        val requestBody = AuthRequest(
            terminalId = prop.qcTerminalId,
            userName = prop.qcUserName,
            password = prop.qcPassword
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
        RedisConfig.setKey(Constants.QC.TOKENkEY,accessToken)
        return accessToken
    }

    //This function is used to fetch the auth Token from Redis if token does not exist it calls the generateAndUpdateToken function.
    suspend fun getToken(): String {
        val token = RedisConfig.getKey(Constants.QC.TOKENkEY)
        log.debug("token received from redis is $token")
        if (token.isNullOrBlank()) {
            return generateAndUpdateToken()
        }
        return token
    }

    data class GravtyAuthToken(
        val username:String,
        val password:String
    )

     private suspend fun getGravtyToken(): AuthToken {
        val requestBody = GravtyAuthToken(
            username = prop.gravtyUserName,
            password =prop.gravtyEpicurePassWord
        )

        val response: HttpResponse
        try {
            response = ConfigureClient.client.post(prop.gravtyTokenURL) {
                headers{
                    append(prop.gravityHeaderKey,prop.epicureHeaderCode)
                }
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            log.info("response body of gravty token is${response.bodyAsText()}")
            if (response.status == HttpStatusCode.OK) {
                return response.body() as AuthToken
            } else {
                throw QCInternalServerException(response.body())
            }
        } catch (e: Exception) {
            log.error("Authorization error occurred while calling api is ${e.message}")
            throw QCInternalServerException(e.message)
        }
     }
    private suspend fun generateUpdateGravtyToken(): String {
        val token = getGravtyToken()
        val accessToken =JWT_TOKEN+" "+ token.token
        RedisConfig.setKeyAndTTL(GRAVTY_TOKEN,accessToken,token.expires_in!!.toLong())
        return accessToken
    }
    suspend fun getGravtyAuthToken(): String {
        val token = RedisConfig.getKey(GRAVTY_TOKEN)
        log.debug("token received from redis for gravty is $token")
        if (token.isNullOrBlank()) {
            return generateUpdateGravtyToken()
        }
        return token
    }

}
