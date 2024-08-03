package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.config.MongoConfig
import com.ihcl.qwikcilver.dto.order.request.FEOrderGCdto
import com.ihcl.qwikcilver.dto.order.request.OrderRequest
import com.ihcl.qwikcilver.dto.order.request.PhysicalGCOrderRequest
import com.ihcl.qwikcilver.dto.order.request.Product
import com.ihcl.qwikcilver.dto.order.request.Payments
import com.ihcl.qwikcilver.dto.order.request.Address
import com.ihcl.qwikcilver.dto.order.request.Billing
import com.ihcl.qwikcilver.dto.order.response.BadRequestResponse
import com.ihcl.qwikcilver.dto.order.response.OrderGCAsyncResponse
import com.ihcl.qwikcilver.dto.order.response.OrderGCSyncResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.model.schema.Order
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.Signature
import io.ktor.client.call.*
import io.ktor.client.plugins.timeout
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.netty.handler.codec.http.HttpResponseStatus
import org.koin.java.KoinJavaComponent
import org.litote.kmongo.eq
import org.litote.kmongo.json
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class OrderGiftCardService {
    private val woohooAuthorizationService by KoinJavaComponent.inject<WoohooAuthorizationService>(WoohooAuthorizationService::class.java)
    private val signature by KoinJavaComponent.inject<Signature>(Signature::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val orderCollection = MongoConfig().getDatabase().getCollection<Order>()

    //This function makes a call to order API of Woohoo to order a GC and provides response and throws exception if any occurred.
    suspend fun orderCard(request: FEOrderGCdto): HttpResponse {
        log.debug("Request received from FE to {}", request)
//        val order=orderCollection.findOne(Order::orderId eq request.refno )
//        val message=order?.orderLineItems?.mapNotNull { orderRef -> orderRef.giftCard?.receiverDetails?.message }?.joinToString(",")
//        //preparing request as per woohoo API.
        val body: Any =
            if (request.deliveryMode!!.isEmpty()) {
//                if (request.products?.get(0)?.qty!! <= 4) {
//                    preparePhysicalGCRequest(request, Constants.QC.SYNC_ONLY_TRUE)
//                } else {
                    preparePhysicalGCRequest(request, Constants.QC.SYNC_ONLY_FALSE)
//                }
            } else {
//                if (request.products?.get(0)?.qty!! <= 4) {
//                    prepareElectronicGCRequest(request, Constants.QC.SYNC_ONLY_TRUE)
//                } else {
                    prepareElectronicGCRequest(request, Constants.QC.SYNC_ONLY_FALSE)
//                }
            }
        return createWoohooOrder(body)
    }
    suspend fun validateBuyGCResponse(call: ApplicationCall,response: HttpResponse){
        when (response.status.value) {
            HttpResponseStatus.BAD_REQUEST.code() -> {
                call.respond(HttpStatusCode.BadRequest, response.body() as BadRequestResponse)
            }
            HttpResponseStatus.ACCEPTED.code() -> {
                call.respond(HttpStatusCode.Accepted, response.body() as OrderGCAsyncResponse)
            }
            HttpResponseStatus.CREATED.code() -> {
                call.respond(HttpStatusCode.OK, response.body() as OrderGCSyncResponse)
            }
            else -> {
                call.respond(response.body() as BadRequestResponse)
            }
        }
    }

    private suspend fun OrderGiftCardService.createWoohooOrder(
        body: Any
    ): HttpResponse {
        val orderUrl = prop.woohooBaseUrl + Constants.QC.Path.WOHOO_ENDPOINT
        val authToken = woohooAuthorizationService.getToken()
        val dateAtClient = woohooAuthorizationService.getDateAtClient()
        //This function is responsible for generating signature to authenticate woohoo api based on request, url and method.
        val signature = signature.signatureGenerator(body, Constants.QC.Path.POST_METHOD, orderUrl)
        log.debug("Request prepared for woohoo is {}", body.json)
        try {
            val response: HttpResponse = ConfigureClient.client.post(orderUrl) {
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                    append(HttpHeaders.Authorization, authToken)
                    append(Constants.QC.HEADER_SIGNATURE, signature)
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            log.debug("Response received from Woohoo for buy gift card is ${response.bodyAsText()}")
            val res: BadRequestResponse = response.body()
            return if (response.status == HttpStatusCode.Accepted || response.status == HttpStatusCode.Created || response.status == HttpStatusCode.BadRequest) {
                response
            } else if (response.status.value == HttpStatusCode.Unauthorized.value) {
                woohooAuthorizationService.generateAndUpdateToken()
                createWoohooOrder(body)
            } else {
                log.debug("response received from woohoo is ${response.bodyAsText()}")
                throw QCInternalServerException(res.message)
            }
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
    }

    private fun preparePhysicalGCRequest(request: FEOrderGCdto, sync: Boolean): Any {
        val physicalGCOrderRequest:PhysicalGCOrderRequest
        if(request.billing?.city.isNullOrEmpty() || request.billing?.line1.isNullOrEmpty() || request.billing?.postcode.isNullOrEmpty()){
            physicalGCOrderRequest = PhysicalGCOrderRequest(
                Address(
                    request.address?.billToThis,
                    request.address?.city,
                    request.address?.company,
                    Constants.WOOHOO.COUNTRY,
                    request.address?.email,
                    request.address?.firstname,
                    request.address?.lastname,
                    request.address?.line1,
                    request.address?.line2,
                    request.address?.postcode,
                    request.address?.region,
                    request.address?.telephone
                ),
                Billing(
                    request.address?.city,
                    request.billing?.company,
                    request.billing?.country,
                    request.billing?.email,
                    request.billing?.firstname,
                    request.billing?.lastname,
                    request.address?.line1,
                    request.billing?.line2,
                    request.address?.postcode,
                    request.billing?.region,
                    request.billing?.telephone
                ),
                listOf(Payments(request.payments?.get(0)?.amount, request.payments?.get(0)?.code,request.refno)),
                listOf(
                    Product(
                        request.products?.get(0)?.currency,
                        request.products?.get(0)?.price,
                        request.products?.get(0)?.qty,
                        request.products?.get(0)?.sku,
                        request.products?.get(0)?.theme,
                        request.products?.get(0)?.payout,
                        request.products?.get(0)?.giftMessage
                    )
                ),
                request.refno,
                sync,
                request.remarks
            )
        }else{
            physicalGCOrderRequest = PhysicalGCOrderRequest(
                Address(
                    request.address?.billToThis,
                    request.address?.city,
                    request.address?.company,
                    Constants.WOOHOO.COUNTRY,
                    request.address?.email,
                    request.address?.firstname,
                    request.address?.lastname,
                    request.address?.line1,
                    request.address?.line2,
                    request.address?.postcode,
                    request.address?.region,
                    request.address?.telephone
                ),
                Billing(
                    request.billing?.city,
                    request.billing?.company,
                    request.billing?.country,
                    request.billing?.email,
                    request.billing?.firstname,
                    request.billing?.lastname,
                    request.billing?.line1,
                    request.billing?.line2,
                    request.billing?.postcode,
                    request.billing?.region,
                    request.billing?.telephone
                ),
                listOf(Payments(request.payments?.get(0)?.amount, request.payments?.get(0)?.code,request.refno)),
                listOf(
                    Product(
                        request.products?.get(0)?.currency,
                        request.products?.get(0)?.price,
                        request.products?.get(0)?.qty,
                        request.products?.get(0)?.sku,
                        request.products?.get(0)?.theme,
                        request.products?.get(0)?.payout,
                        request.products?.get(0)?.giftMessage
                    )
                ),
                request.refno,
                sync,
                request.remarks
            )
        }
        return physicalGCOrderRequest
    }

    private fun prepareElectronicGCRequest(request: FEOrderGCdto, sync: Boolean): Any {
        return OrderRequest(
            Address(
                request.address?.billToThis,
                request.address?.city,
                request.address?.company,
                Constants.WOOHOO.COUNTRY,
                request.address?.email,
                request.address?.firstname,
                request.address?.lastname,
                request.address?.line1,
                request.address?.line2,
                request.address?.postcode,
                request.address?.region,
                request.address?.telephone
            ),
            Billing(
                request.billing?.city,
                request.billing?.company,
                request.billing?.country,
                request.billing?.email,
                request.billing?.firstname,
                request.billing?.lastname,
                request.billing?.line1,
                request.billing?.line2,
                request.billing?.postcode,
                request.billing?.region,
                request.billing?.telephone
            ),
            request.deliveryMode,
            request.remarks,
            listOf(Payments(request.payments?.get(0)?.amount, request.payments?.get(0)?.code,request.refno)),
            listOf(
                Product(
                    request.products?.get(0)?.currency,
                    request.products?.get(0)?.price,
                    request.products?.get(0)?.qty,
                    request.products?.get(0)?.sku,
                    request.products?.get(0)?.theme,
                    request.products?.get(0)?.payout,
                    request.products?.get(0)?.giftMessage
                )
            ),
            request.refno,
            sync
        )
    }
}