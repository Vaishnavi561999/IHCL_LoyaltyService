package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.config.RedisConfig
import com.ihcl.qwikcilver.dto.gcCategories.GCCategoriesBadRequest
import com.ihcl.qwikcilver.dto.gcCategories.GCCategoriesResponse
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

class GCCategoriesService {
    private val woohooAuthorizationService by KoinJavaComponent.inject<WoohooAuthorizationService>(
        WoohooAuthorizationService::class.java
    )
    private val signature by KoinJavaComponent.inject<Signature>(Signature::class.java)
    private val prop = Configuration.env
    val log: Logger = LoggerFactory.getLogger(javaClass)
    suspend fun getCategories(): HttpResponse {
        val categoriesUrl = prop.woohooBaseUrl + Constants.QC.Path.CATALOGUE_URL
        val authToken = woohooAuthorizationService.getToken()
        val dateAtClient = woohooAuthorizationService.getDateAtClient()
        val signature = signature.signatureGenerator(null, Constants.QC.Path.GET_METHOD, categoriesUrl)
        log.debug("get categories url $categoriesUrl date at client $dateAtClient Authorization $authToken")
        try {
            val response: HttpResponse = ConfigureClient.client.get(categoriesUrl) {
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
            log.debug("response body of fetch categories is {}", response.bodyAsText())

            return when{
                (response.status == HttpStatusCode.OK)->{
                    response
                }
                (response.status  == HttpStatusCode.Unauthorized)->{
                    woohooAuthorizationService.generateAndUpdateToken()
                    getCategories()
                }
                else->{
                    log.debug("response body of fetch categories is  {}", response)
                    response
                }
            }
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
    }
    suspend fun validateFetchCatogiries(call: ApplicationCall, userName:String?, password:String?){
        if (userName == prop.woohooCatalogue_username && password == prop.woohooCatalogue_password) {
            log.info("Successfully validated username and password..")
            val response = getCategories()
            if (response.status == HttpStatusCode.OK) {
                call.respond(response.body() as GCCategoriesResponse)
            } else {
                call.respond(response.body() as GCCategoriesBadRequest)
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