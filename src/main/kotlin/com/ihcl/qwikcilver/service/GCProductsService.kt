package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.config.RedisConfig
import com.ihcl.qwikcilver.dto.gcProducts.GCProductRequest
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.Signature
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GCProductsService {
    private val woohooAuthorizationService by KoinJavaComponent.inject<WoohooAuthorizationService>(WoohooAuthorizationService::class.java)
    private val signature by KoinJavaComponent.inject<Signature>(Signature::class.java)
    private val prop = Configuration.env
    val log: Logger = LoggerFactory.getLogger(javaClass)
    suspend fun getProducts(request: GCProductRequest): HttpResponse {
        val productsUrl = prop.woohooBaseUrl + Constants.QC.Path.CATALOGUE_URL + "/${request.categoryId}/${Constants.WOOHOO.PRODUCT_URL}"
        val authToken = woohooAuthorizationService.getToken()
        val dateAtClient = woohooAuthorizationService.getDateAtClient()
        val signature = signature.signatureGenerator(null, Constants.QC.Path.GET_METHOD, productsUrl)
        log.info("Get products API URL $productsUrl Authorization $authToken date at client $dateAtClient signature $signature")
        try {
            val response: HttpResponse = ConfigureClient.client.get(productsUrl) {
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
            log.debug("products received from qwikcilver are {}", response.bodyAsText())

            return when{
                (response.status == HttpStatusCode.OK)->{
                    response
                }
                (response.status == HttpStatusCode.Unauthorized)->{
                    woohooAuthorizationService.generateAndUpdateToken()
                    getProducts(request)
                }
                else->{
                    log.debug("response body of products is {}", response)
                    response
                }
            }
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
    }
    suspend fun validateFetchProduct(call: ApplicationCall, request: GCProductRequest, userName:String?, password:String?){
        if (userName == prop.woohooCatalogue_username && password == prop.woohooCatalogue_password) {
            log.info("Successfully validated username and password..")
            val response = getProducts(request)
            when (response.status) {
                HttpStatusCode.OK -> call.respond(response.bodyAsText())
                HttpStatusCode.BadRequest -> call.respond(HttpStatusCode.BadRequest,response.body())
            }
        } else {
            val gcIncorrectAttempts= RedisConfig.getKey(Constants.QC.GIFT_CARD_CATEGORY_RETRIEVE_COUNT)
            if (gcIncorrectAttempts.isNullOrEmpty()){
                RedisConfig.setKeyAndTTL(Constants.QC.GIFT_CARD_CATEGORY_RETRIEVE_COUNT,
                    Constants.QC.INITIAL_GC_CATALOGUE_VALUE,prop.gcCatalogueTTL.toLong())
                throw QCInternalServerException(Constants.QC.INVALID_GC_CATALOGUE_CREDENTIALS)
            }
            else{
                var count=gcIncorrectAttempts.toInt()
                if (gcIncorrectAttempts.toInt()>=3){
                    RedisConfig.setKeyAndTTL(Constants.QC.GIFT_CARD_CATEGORY_RETRIEVE_COUNT,count.toString(),prop.gcCatalogueTTL.toLong())
                    throw QCInternalServerException(Constants.QC.INVALID_GC_CATALOGUE_CREDENTIALS_ACCOUNT_RESTRICT)
                }else{
                    count++
                    RedisConfig.setKey(Constants.QC.GIFT_CARD_CATEGORY_RETRIEVE_COUNT,count.toString())
                    throw QCInternalServerException(Constants.QC.INVALID_GC_CATALOGUE_CREDENTIALS)
                }
            }
        }
    }
}