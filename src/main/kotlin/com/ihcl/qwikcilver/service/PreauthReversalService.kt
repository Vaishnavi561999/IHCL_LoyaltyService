package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.preauthreversal.OriginalPreauthReversalRequest
import com.ihcl.qwikcilver.dto.preauthreversal.request.Card
import com.ihcl.qwikcilver.dto.preauthreversal.request.PreauthReversalRequest
import com.ihcl.qwikcilver.dto.preauthreversal.response.PreauthReversalResponse
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import io.ktor.client.call.body
import io.ktor.client.plugins.timeout
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PreauthReversalService {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    //This function makes a call to Preauth reversal API of QC, provides the response and throws exception if any occurred.
    suspend fun preAuthReversal(req: OriginalPreauthReversalRequest): PreauthReversalResponse {

        val transactionsPreAuthURL = prop.qcBaseUrl + Constants.QC.Path.REVERSE_URL
        val accessToken = authService.getToken()
        val transactionId: String = req.TransactionId.toString()
        val dateAtClient = req.TransactionDateTime.toString()
        val body = PreauthReversalRequest(
            listOf(Card(req.Amount, req.CardNumber)),
            Constants.QC.RequestParams.INPUTTYPE,
             Constants.QC.REVERSAL_PREAUTH_NOTE,
            req.InvoiceNumber,
            req.InvoiceDate,
            req.BillAmount
        )
        log.debug("Request prepared for QC is {}", body)
        log.info("Request prepared for QC is ${req.TransactionId}")
        log.info("Request prepared for QC is ${req.TransactionDateTime}")
        log.debug("Preauth Reversal QC request prepared as ${Json.encodeToString(body)}")
        try {
            val response: HttpResponse = ConfigureClient.client.post(transactionsPreAuthURL) {
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(HttpHeaders.Authorization, accessToken)
                    append(Constants.QC.HEADER_TRANSACTIONID, transactionId)
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                }
                setBody(body)
                contentType(ContentType.Application.Json)
            }
            val qcRes: PreauthReversalResponse = response.body()
            log.debug("Preauth reversal response received from QC is {}", qcRes)
            if (qcRes.ResponseCode == Constants.QC.TOKEN_EXPIRED || qcRes.ResponseCode == Constants.QC.INVALID_TOKEN) {
                authService.generateAndUpdateToken()
                 return preAuthReversal(req)
            }
            return if (response.status == HttpStatusCode.OK) {
                response.body() as PreauthReversalResponse
            } else {
                log.info("Preauth reversal response received from QC is ${response.status}")
                response.body()
            }
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
     }
}