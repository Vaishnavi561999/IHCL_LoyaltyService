package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.plugins.ConfigureClient.client
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.loyaltygravty.request.EpicurePrimaryCard
import com.ihcl.qwikcilver.exception.PrimaryCardCreationException
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EpicurePrimaryCardService {
    private val props = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)

    suspend fun createPrimaryCard(body: EpicurePrimaryCard): HttpResponse {
        val response: HttpResponse
        try {
           // val token = DatabseRepository().gettToken().token
            val token=authService.getGravtyAuthToken()                  // Epicure Token
            response = client.post(props.epicurePrimaryCard) {
                headers {
                    append(props.gravityHeaderKey, props.epicureHeaderCode)
                    append(props.authorizationKey,token)
                }
                timeout {
                    requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            if (response.status == HttpStatusCode.Unauthorized) {
                authService.getGravtyAuthToken()
                return createPrimaryCard(body)
            }
        } catch (e: Exception) {
            val errorMessage = "Error while creating Epicure Primary Card. Exception: ${e.message}"
            log.error(errorMessage)
            throw PrimaryCardCreationException(errorMessage)
        }
        log.debug("Primary card API response: {}", response)

        return response
    }
}
