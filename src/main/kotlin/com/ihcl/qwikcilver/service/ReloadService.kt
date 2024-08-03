package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.reload.FEReloadRes
import com.ihcl.qwikcilver.dto.reload.OriginalReloadRequest
import com.ihcl.qwikcilver.dto.reload.ResCard
import com.ihcl.qwikcilver.dto.reload.request.Card
import com.ihcl.qwikcilver.dto.reload.request.ReloadRequest
import com.ihcl.qwikcilver.dto.reload.response.ReloadResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.repository.DatabaseRepository
import com.ihcl.qwikcilver.util.Constants
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ReloadService {

    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    val reloadAuthService by KoinJavaComponent.inject<ReloadAuthentication>(ReloadAuthentication::class.java)
    private val databaseRepository by KoinJavaComponent.inject<DatabaseRepository>(DatabaseRepository::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    //This function makes a call to reload API of QC to reload the GC balance then provides response and throws exception if any occurred.
    suspend fun reloadCard(request: OriginalReloadRequest): FEReloadRes? {
        val transactionId = databaseRepository.incrementCounter()
        val reloadUrl = prop.qcBaseUrl + Constants.QC.Path.TRANSACTIONS_URL
        val accessToken = reloadAuthService.getToken()
        val dateAtClient: String = balanceEnquiryService.getDateAtClient()
        val body =
            ReloadRequest(
                listOf(Card(request.amount, request.cardNumber)),
                Constants.QC.RequestParams.INPUTTYPE,
                Constants.QC.RELOAD_NUMBER_OF_CARDS,
                Constants.QC.TransactionType.RELOAD,
                request.invoiceNumber,
                request.idempotencyKey
            )
        log.debug("Reload QC request prepared as ${Json.encodeToString(body)}")
        try {
            val response: HttpResponse = ConfigureClient.client.post(reloadUrl) {
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(Constants.QC.HEADER_TRANSACTIONID, transactionId.toString())
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                    append(HttpHeaders.Authorization, accessToken)
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            val res = response.body<ReloadResponse>()
            log.debug("Reload response received from QC is {}", res)
            //10019 transaction id check failed
            if (res.ResponseCode == Constants.QC.TOKEN_EXPIRED || res.ResponseCode == Constants.QC.INVALID_TOKEN) {
                reloadAuthService.generateAndUpdateToken()
                return reloadCard(request)
            } else if (res.ResponseCode == Constants.QC.CHECK_FAIL_TRANSACTIONID) {
                throw QCInternalServerException("duplicate orderID")
            }
            val feRes = FEReloadRes(
                res.TransactionId,
                res.ResponseMessage,
                res.ResponseCode?.toInt(),
                listOf(
                    ResCard(
                        res.Cards?.get(0)?.ResponseCode,
                        res.Cards?.get(0)?.Balance,
                        res.Cards?.get(0)?.CardNumber,
                        res.Cards?.get(0)?.ExpiryDate,
                        res.Cards?.get(0)?.ResponseMessage
                    )
                )
            )
            feRes.Cards?.forEach {
                 if (it?.responseCode==Constants.CARDNUMBER_OR_CARDPIN_EXPIRED){
                     it.responseMessage=Constants.ERROR_RESPONSE_MESSAGE_OF_GIFT_CARD
                 }
            }
            return feRes
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
    }
    suspend fun validateReload(call: ApplicationCall, response:FEReloadRes?){
        if (response?.responseCode == Constants.QC.REQUEST_RESPONSECODE) {
            call.respond(response)
        }else if(response?.Cards?.any {it?.responseCode== Constants.CARD_NUMBER_DE_ACTIVE_CODE }==true){
            response.Cards.forEach { it?.responseMessage = Constants.CARD_NUMBER_DE_ACTIVE_ERROR_MESSAGE }
            call.respond(HttpStatusCode.BadRequest, response)
        }
        else {
            call.respond(HttpStatusCode.BadRequest, response as FEReloadRes)
        }
    }
}