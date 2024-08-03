package com.ihcl.qwikcilver.route

import com.ihcl.qwikcilver.dto.gcProducts.ActivateGCRequest
import com.ihcl.qwikcilver.dto.gcProducts.GCProductRequest
import com.ihcl.qwikcilver.dto.order.request.FEOrderGCdto
import com.ihcl.qwikcilver.dto.orderstatus.FEOrderStatusRequest
import com.ihcl.qwikcilver.service.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import com.ihcl.qwikcilver.util.Constants
import io.ktor.server.routing.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Application.buyingCardsRoute() {
    val orderService by KoinJavaComponent.inject<OrderGiftCardService>(OrderGiftCardService::class.java)
    val orderStatusService by KoinJavaComponent.inject<OrderStatusService>(OrderStatusService::class.java)
    val categoriesService by KoinJavaComponent.inject<GCCategoriesService>(GCCategoriesService::class.java)
    val productsService by KoinJavaComponent.inject<GCProductsService>(GCProductsService::class.java)
    val activateGCService by KoinJavaComponent.inject<ActivateGCService>(ActivateGCService::class.java)
    val gcOrderStatusService by KoinJavaComponent.inject<GCOrderStatusService>(GCOrderStatusService::class.java)
    val log: Logger = LoggerFactory.getLogger(javaClass)
    routing {
        route("/v1/qc") {

            //This route receives the request from order service and internally calls the Woohoo create order API to buy a gift card from woohoo and provide response.
            post("/order-gc") {
                val request = call.receive<FEOrderGCdto>()
                val response = orderService.orderCard(request)
                log.debug("response body of order is {}", response)
                orderService.validateBuyGCResponse(call,response)
            }
            //This route receives the request from front end and internally calls the woohoo orderStatus api to check the status of the purchased GC and returns the response
            post("/order-status") {
                val request = call.receive<FEOrderStatusRequest>()
                log.debug("request body of FE order status is {}", request)
                 val response = orderStatusService.cardStatus(request)
                 orderStatusService.validateOrderStatus(call,response)
            }
            //This end point will fetch and send the response of category details from woohoo
            get("/fetch-categories") {
                val userName = call.request.headers[Constants.QC.USERNAME]
                val password = call.request.headers[Constants.QC.PASSWORD]
                categoriesService.validateFetchCatogiries(call,userName,password)
            }
            //This end point will fetch and send the response of list of products configured.
            post("/fetch-products") {
                val request = call.receive<GCProductRequest>()
                val userName = call.request.headers[Constants.QC.USERNAME]
                val password = call.request.headers[Constants.QC.PASSWORD]
                productsService.validateFetchProduct(call,request,userName,password)
            }
            //This end point internally call the activate api of woohoo which activates and gived the card details of the gift cards purchased from woohoo
            post("/activate-gc") {
                val request = call.receive<ActivateGCRequest>()
                val response = activateGCService.activateGC(request)
                activateGCService.validateActiveGC(call,response)
            }

            get ("/order-details"){
                val orderId = call.request.queryParameters["orderId"]
                val response = gcOrderStatusService.getOrderStatus(orderId!!)
                gcOrderStatusService.validateGetOrderStatus(call,response,orderId)

            }
        }
    }
}