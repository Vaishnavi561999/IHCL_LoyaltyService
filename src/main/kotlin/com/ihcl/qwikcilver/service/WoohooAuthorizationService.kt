package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.config.RedisConfig
import com.ihcl.qwikcilver.dto.woohooauth.request.WoohooAuthCodeRequest
import com.ihcl.qwikcilver.dto.woohooauth.request.WoohooAuthTokenRequest
import com.ihcl.qwikcilver.dto.woohooauth.response.WoohooAuthCodeResponse
import com.ihcl.qwikcilver.dto.woohooauth.response.WoohooAuthTokenResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.util.Constants
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.*

class WoohooAuthorizationService {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    private suspend fun getAuthToken():WoohooAuthTokenResponse {
        val authCodeRes = this.getAuthCode()
         val authCode = authCodeRes?.authorizationCode
         val tokenRequestBody = WoohooAuthTokenRequest(
            clientId = prop.woohooClientId,
            clientSecret = prop.woohooClientSecret,
            authorizationCode =  authCode
        )
        val authTokenUrl = prop.woohooBaseUrl + Constants.QC.Path.AUTHTOKENURL

        try {
            val response: HttpResponse = ConfigureClient.client.post(authTokenUrl) {
                contentType(ContentType.Application.Json)
                setBody(tokenRequestBody)
            }
            return if (response.status == HttpStatusCode.OK) {
                 response.body() as WoohooAuthTokenResponse
             } else {
                response.body()
            }
        } catch (e: Exception) {
            log.error("Authorization error occurred while calling api is ${e.message}")
            throw QCInternalServerException(e.message)
        }
    }

    private suspend fun getAuthCode():  WoohooAuthCodeResponse? {
        val requestBody = WoohooAuthCodeRequest(
            clientId = prop.woohooClientId,
            password = prop.woohooPassword,
            username = prop.woohooUserName
        )
        val authCodeUrl = prop.woohooBaseUrl + Constants.QC.Path.AUTHCODEURL
        val response: HttpResponse
        try {
            response = ConfigureClient.client.post(authCodeUrl) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            return if (response.status == HttpStatusCode.OK) {
                response.body() as WoohooAuthCodeResponse
            } else {
                response.body()
            }
        } catch (e: Exception) {
            log.error("Authorization error occurred while calling api is ${e.message}")
            throw QCInternalServerException(e.message)
        }
     }
    suspend fun generateAndUpdateToken(): String {
        val token = getAuthToken()
         val accessToken = Constants.AUTH_TOKEN+ token.token as String
         RedisConfig.setKey(Constants.WOOHOO.TOKENKEY,accessToken)
        return accessToken
    }
    suspend fun getToken(): String {
        val token: String? = RedisConfig.getKey(Constants.WOOHOO.TOKENKEY)
          log.info("token received from redis is $token")
        if (token.isNullOrBlank()) {
            return generateAndUpdateToken()
          }
        return token
    }
   fun getDateAtClient(): String {
        val dateFormat = SimpleDateFormat(Constants.WOOHOO.DATE_FORMAT)
        dateFormat.timeZone = TimeZone.getTimeZone(Constants.WOOHOO.TIMEZONE_ID)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

}