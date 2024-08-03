package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.gcOrderStatus.GcOrderErrorStatus
import com.ihcl.qwikcilver.dto.gcOrderStatus.OrderStatusError
import com.ihcl.qwikcilver.dto.gcOrderStatus.PhysicalGCOrderStatusResponse
import com.ihcl.qwikcilver.dto.gcOrderStatus.PhysicalGiftCardOrderStatus
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.ihcl.qwikcilver.util.Signature
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class GCOrderStatusService {
    private val prop = Configuration.env
    private val woohooAuthorizationService by KoinJavaComponent.inject<WoohooAuthorizationService>(WoohooAuthorizationService::class.java)
    val log: Logger = LoggerFactory.getLogger(javaClass)
    private val signature by KoinJavaComponent.inject<Signature>(Signature::class.java)

    suspend fun getOrderStatus(orderId:String): HttpResponse {
        log.debug("Request received to check Physical giftCard order status with $orderId")
        val statusURL = prop.woohooBaseUrl + Constants.QC.Path.WOHOO_ENDPOINT_GC +"/${orderId.replace("\\s+".toRegex(), "")}"
        log.info("Status URL..$statusURL")
        val authToken = woohooAuthorizationService.getToken()
        val dateAtClient = woohooAuthorizationService.getDateAtClient()
        //This function is responsible for generating signature to authenticate woohoo api based on request, url and method.
        val signature = signature.signatureGenerator(null, Constants.QC.Path.GET_METHOD, statusURL)
        log.info("get order status URL $statusURL date at client $dateAtClient Authorization $authToken signature $signature")
        try {
            val response: HttpResponse = ConfigureClient.client.get(statusURL) {
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                    append(HttpHeaders.Authorization , authToken)
                    append(Constants.QC.HEADER_SIGNATURE, signature)
                }
                contentType(ContentType.Application.Json)
            }
            if (  response.status.value ==  HttpStatusCode.Unauthorized.value) {
                log.info("Unauthorized coming...")
                woohooAuthorizationService.generateAndUpdateToken()
                getOrderStatus(orderId)
            }
            log.info("Getting response status value from Physical gift card Order status API ${response.status.value}  Response body ${response.bodyAsText()}")
            return response

        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
    }
    suspend fun validateGetOrderStatus(call: ApplicationCall,response:HttpResponse,orderId: String){
        val orderStatusResponse = response.body<PhysicalGCOrderStatusResponse>()
        when{
            (response.status == HttpStatusCode.Unauthorized)->{
                call.respond(HttpStatusCode.Unauthorized, response.body() as OrderStatusError)
            }
            (response.status == HttpStatusCode.InternalServerError)->{
                call.respond(HttpStatusCode.InternalServerError, response.body() as List<OrderStatusError>)
            }
            (response.status == HttpStatusCode.BadRequest)->{
                call.respond(HttpStatusCode.BadRequest, response.body() as GcOrderErrorStatus)
            }
            (orderStatusResponse.statusLabel== Constants.GC_ORDER_STATUS_IN_PROCESSING)->{
                val trackingNumber:String
                val trackingURL :String?
                if (orderStatusResponse.shipments?.isNotEmpty() == true && orderStatusResponse.shipments?.get(0)?.tracks?.isNotEmpty() == true ){
                    trackingNumber=orderStatusResponse.shipments?.get(0)?.tracks?.get(0)?.awb.toString()
                    trackingURL= orderStatusResponse.shipments?.get(0)?.tracks?.get(0)?.url.toString()
                } else{
                    trackingNumber=Constants.GC_ORDER_STATUS_IN_PROCESSING
                    trackingURL= null
                }
                call.respond(HttpStatusCode.OK,
                    PhysicalGiftCardOrderStatus(orderId, trackingNumber, Constants.GC_ORDER_STATUS_IN_VOICED, "2", trackingURL,"${orderStatusResponse.date}", orderStatusResponse.cardTypes[0])
                )
            }
            (orderStatusResponse.statusLabel== Constants.GC_ORDER_STATUS_IN_SHIPPED)->{
                val trackingNumber:String
                val trackingURL :String?
                if (orderStatusResponse.shipments?.isNotEmpty() == true && orderStatusResponse.shipments?.get(0)?.tracks?.isNotEmpty() == true ){
                    trackingNumber=orderStatusResponse.shipments?.get(0)?.tracks?.get(0)?.awb.toString()
                    trackingURL= orderStatusResponse.shipments?.get(0)?.tracks?.get(0)?.url.toString()
                } else{
                    trackingNumber=Constants.GC_ORDER_STATUS_IN_SHIPPED
                    trackingURL= null
                }
                call.respond(HttpStatusCode.OK,
                    PhysicalGiftCardOrderStatus(orderId, trackingNumber, Constants.GC_ORDER_STATUS_IN_SHIPPED, "3", trackingURL,"${orderStatusResponse.date}", orderStatusResponse.cardTypes[0])
                )
            }
            (orderStatusResponse.statusLabel== Constants.GC_ORDER_STATUS_COMPLETE)->{
                val trackingNumber:String
                val trackingURL :String?
                if (orderStatusResponse.shipments?.isNotEmpty() == true && orderStatusResponse.shipments?.get(0)?.tracks?.isNotEmpty() == true ){
                    trackingNumber=orderStatusResponse.shipments?.get(0)?.tracks?.get(0)?.awb.toString()
                     trackingURL= orderStatusResponse.shipments?.get(0)?.tracks?.get(0)?.url.toString()
                } else{
                    trackingNumber=Constants.GC_ORDER_STATUS_COMPLETE
                     trackingURL= null
                }
                if (orderStatusResponse.orderType == Constants.PHYSICAL_GIFT_CARD_LABEL) {
                    call.respond(
                        HttpStatusCode.OK,
                        PhysicalGiftCardOrderStatus(
                            orderId,
                            trackingNumber,
                            Constants.GC_ORDER_STATUS_COMPLETE,
                            "4",
                            trackingURL,
                            "${orderStatusResponse.date}",
                            orderStatusResponse.cardTypes[0]
                        )
                    )
                }
                else{
                    call.respond(
                        HttpStatusCode.OK,
                        PhysicalGiftCardOrderStatus(
                            orderId,
                            "",
                            Constants.GC_ORDER_STATUS_COMPLETE,
                            "4",
                            "",
                            "${orderStatusResponse.date}",
                            Constants.E_GIFT_CARD_LABEL
                        )
                    )
                }
            }
            (orderStatusResponse.statusLabel== Constants.GC_ORDER_STATUS_BUSINESS_APPROVED)->{
                call.respond(HttpStatusCode.OK,
                    PhysicalGiftCardOrderStatus(orderId, "", Constants.GC_ORDER_STATUS_BUSINESS_APPROVED, "1", "","${orderStatusResponse.date}",orderStatusResponse.cardTypes[0])
                )
            }
        }
    }
}