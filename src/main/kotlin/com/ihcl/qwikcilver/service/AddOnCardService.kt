package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.plugins.ConfigureClient.client
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.loyaltygravty.request.AddOnCard
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class AddOnCardService {
    private val props = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)

    /* Method is used to call Add On card gravty Api and
    used for getting the created card */

    suspend fun getAddOnCard(body: AddOnCard): HttpResponse {
        val response:HttpResponse
        try {
            val token = authService.getGravtyAuthToken()
            log.info("TOKEN..............$token")
            response = client.post(props.gravtyMemberCardsURL) {
                headers {
                    append(props.gravityHeaderKey, props.epicureHeaderCode)
                    append(props.authorizationKey, token)
                }
                timeout {
                    requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            if (response.status== HttpStatusCode.Unauthorized){
                authService.getGravtyAuthToken()
                return getAddOnCard(body)
            }
        } catch (e: Exception) {
            log.error("Add-On card API Response while getting  : Exception raised : ${e.message}")
            throw e.message?.let { BadRequestException(it) }!!
        }
        log.info("Add-On card API response.  : $response")
        return response
    }
}