package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.plugins.ConfigureClient.client
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.exception.GravtyCardFetchingException
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GravtyMemberCardsService {
    private val props = Configuration.env
    private lateinit var response: HttpResponse
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)

    suspend fun getMemberCards(memberId:String): HttpResponse {
        try {
            val token= authService.getGravtyAuthToken()
            val memberCardsUrl="${props.gravtyMemberCardsURL}?filter%7Bmember_id%7D=$memberId"
            response = client.get(memberCardsUrl) {
                headers {
                    append(props.gravityHeaderKey, props.epicureHeaderCode)
                    append(props.authorizationKey, token)
                }
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.Unauthorized) {
                authService.getGravtyAuthToken()
                getMemberCards(memberId)
            }
        } catch (e: Exception) {
            val errorMessage = "Error while getting member cards: ${e.message}"
            log.error(errorMessage)
            throw GravtyCardFetchingException(errorMessage)
        }
        log.info("Gravty primary and Add-on cards for the member ID $memberId are : {}", response)

        return response
    }
}