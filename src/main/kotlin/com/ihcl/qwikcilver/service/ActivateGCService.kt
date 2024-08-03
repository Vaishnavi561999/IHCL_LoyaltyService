package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.activategc.ActivateGCResponse
import com.ihcl.qwikcilver.dto.gcProducts.ActivateGCRequest
import com.ihcl.qwikcilver.dto.order.response.BadRequestResponse
import com.ihcl.qwikcilver.dto.orderstatus.OrderStatusAvailabilityResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.Signature
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.netty.handler.codec.http.HttpResponseStatus
import org.koin.java.KoinJavaComponent
import org.litote.kmongo.json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ActivateGCService {
    private val woohooAuthorizationService by KoinJavaComponent.inject<WoohooAuthorizationService>(WoohooAuthorizationService::class.java)
    private val signature by KoinJavaComponent.inject<Signature>(Signature::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    //This method generate signature to authorize woohoo API and calls the activate gift card api of woohoo and returns response.
    suspend fun activateGC(request:ActivateGCRequest): HttpResponse {
        log.debug("Request received to activate gc is {}", request.json)
        val activateURL = prop.woohooBaseUrl + Constants.QC.Path.WOHOO_ENDPOINT + "/${request.orderId}/${Constants.WOOHOO.ACTIVATE_URL}"
        val authToken = woohooAuthorizationService.getToken()
        val dateAtClient = woohooAuthorizationService.getDateAtClient()
        //This function is responsible for generating signature to authenticate woohoo api based on request, url and method.
        val signature = signature.signatureGenerator(null, Constants.QC.Path.GET_METHOD, activateURL)
        log.debug("Activate API url $activateURL Authorization $authToken date at client $dateAtClient signature $signature")
        try {
            val response: HttpResponse = ConfigureClient.client.get(activateURL) {
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                    append(HttpHeaders.Authorization, authToken)
                    append(Constants.QC.HEADER_SIGNATURE, signature)
                }
                contentType(ContentType.Application.Json)
            }
            log.debug("Response received from activate gc api is ${response.bodyAsText()}")
            val res: BadRequestResponse = response.body()
            return when (response.status) {
                HttpStatusCode.OK, HttpStatusCode.BadRequest -> {
                    response
                }
                HttpStatusCode.Unauthorized -> {
                    woohooAuthorizationService.generateAndUpdateToken()
                    activateGC(request)
                }
                else -> {
                    log.debug("activate gc is ${response.bodyAsText()} ")
                    throw QCInternalServerException(res.message)
                }
            }
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
    }
    suspend fun validateActiveGC(call: ApplicationCall,response:HttpResponse){
        if (response.status.value == HttpResponseStatus.OK.code()) {
            call.respond(response.body() as ActivateGCResponse)
        } else {
            call.respond(HttpStatusCode.BadRequest,response.body() as OrderStatusAvailabilityResponse)
        }
        call.respond(response.bodyAsText())
    }
}