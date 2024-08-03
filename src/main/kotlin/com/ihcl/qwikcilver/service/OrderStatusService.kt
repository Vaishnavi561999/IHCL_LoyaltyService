package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.order.response.BadRequestResponse
import com.ihcl.qwikcilver.dto.orderstatus.FEOrderStatusRequest
import com.ihcl.qwikcilver.dto.orderstatus.OrderStatusAvailabilityResponse
import com.ihcl.qwikcilver.dto.orderstatus.OrderStatusResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.Signature
import io.ktor.client.call.*
import io.ktor.client.plugins.timeout
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.netty.handler.codec.http.HttpResponseStatus
import org.koin.java.KoinJavaComponent
import org.litote.kmongo.json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OrderStatusService {
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val signature by KoinJavaComponent.inject<Signature>(Signature::class.java)
    private val woohooAuthorizationService by KoinJavaComponent.inject<WoohooAuthorizationService>(WoohooAuthorizationService::class.java)

    //This function makes a call to order status API of Woohoo to Check status of a purchased GC and provides response and throws exception if any occurred.
    suspend fun cardStatus(request: FEOrderStatusRequest): HttpResponse {
        log.debug("Request received to check the order status is {}", request.json)
        val statusURL = prop.woohooBaseUrl + Constants.QC.Path.WOHOO_ENDPOINT + "/${request.orderNumber}/${Constants.WOOHOO.STATUS_URL}"
        val authToken = woohooAuthorizationService.getToken()
        val dateAtClient = woohooAuthorizationService.getDateAtClient()
        //This function is responsible for generating signature to authenticate woohoo api based on request, url and method.
        val signature = signature.signatureGenerator(null, Constants.QC.Path.GET_METHOD, statusURL)
        try {
            val response: HttpResponse = ConfigureClient.client.get(statusURL) {
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
            log.debug("Response received from card stats API is ${response.bodyAsText()}")
            val res: BadRequestResponse = response.body()
            return if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.BadRequest){
                response
            }else if (response.status.value ==  HttpStatusCode.Unauthorized.value) {
                woohooAuthorizationService.generateAndUpdateToken()
                cardStatus(request)
            }else{
                log.debug("response body of order status is ${response.bodyAsText()} ")
                throw QCInternalServerException(res.message)
            }

        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
    }
    suspend fun validateOrderStatus(call: ApplicationCall,response:HttpResponse){
        if (response.status.value == HttpResponseStatus.OK.code()) {
            call.respond(response.body() as OrderStatusResponse)
        } else if(response.status == HttpStatusCode.BadRequest){
            val res = response.body<OrderStatusAvailabilityResponse>()
            if(res.code?.toInt() == Constants.GC_ORDER_NOT_FOUND_ERROR_CODE){
                call.respond(response.status,res.apply {
                    this.message = Constants.INVALID_ORDER
                })
            }
        }else {
            call.respond(response.status, response.bodyAsText())
        }
    }
}