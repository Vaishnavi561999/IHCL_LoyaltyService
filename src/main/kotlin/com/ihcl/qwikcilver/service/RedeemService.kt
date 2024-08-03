package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.redeem.FERedeemResponse
import com.ihcl.qwikcilver.dto.redeem.OriginalRedeemRequest
import com.ihcl.qwikcilver.dto.redeem.ResCard
import com.ihcl.qwikcilver.dto.redeem.request.Card
import com.ihcl.qwikcilver.dto.redeem.request.RedeemRequest
import com.ihcl.qwikcilver.dto.redeem.response.RedeemResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
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

class RedeemService {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val databaseRepository by KoinJavaComponent.inject<DatabaseRepository>(DatabaseRepository::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    //This function makes a call to redeem API of QC to redeem the GC balance then provides response and throws exception if any occurred.
    suspend fun redeem(request: OriginalRedeemRequest): FERedeemResponse {
        val reloadUrl = prop.qcBaseUrl + Constants.QC.Path.TRANSACTIONS_URL
        val accessToken = authService.getToken()
        val transactionId = databaseRepository.incrementCounter()
        val dateAtClient: String? = balanceEnquiryService.getCurrentDateTime()
        val body = RedeemRequest(
            listOf(Card(request.amount, request.cardNumber, request.cardPin,request.invoiceNumber)),
            Constants.QC.RequestParams.INPUTTYPE,
            Constants.QC.TransactionType.REDEEM,
            request.billAmount,
            request.idempotencyKey,
            request.propertyName
        )
        log.debug("Redeem QC request prepared as ${Json.encodeToString(body)}")
        try {
            val response: HttpResponse = ConfigureClient.client.post(reloadUrl) {
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(Constants.QC.HEADER_TRANSACTIONID, transactionId.toString())
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient!!)
                    append(HttpHeaders.Authorization, accessToken)
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            val qcRes: RedeemResponse = response.body()
            log.debug("Redeem response received from QC is {}", qcRes)
            if (qcRes.ResponseCode == Constants.QC.TOKEN_EXPIRED || qcRes.ResponseCode == Constants.QC.INVALID_TOKEN) {
                authService.generateAndUpdateToken()
                return redeem(request)
            }
            return FERedeemResponse(
                            qcRes.TransactionId, qcRes.ResponseMessage, qcRes.ResponseCode?.toInt(),
                            listOf(
                                ResCard(
                                    qcRes.Cards?.get(0)?.ResponseMessage,
                                    qcRes.Cards?.get(0)?.ResponseCode,
                                    qcRes.Cards?.get(0)?.Balance
                                )
                            ),
                            qcRes.CurrentBatchNumber.toString(),
                            qcRes.Cards?.get(0)?.ApprovalCode.toString()
                        )


        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
       }
    suspend fun  validateRedeem(call: ApplicationCall,response:FERedeemResponse){
        if (response.responseCode == Constants.QC.REQUEST_RESPONSECODE) {
            call.respond(response)
        } else {
            call.respond(HttpStatusCode.BadRequest, response)
        }
    }
}