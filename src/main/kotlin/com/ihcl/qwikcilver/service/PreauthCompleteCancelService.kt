package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.preauth.FEPreauthRes
import com.ihcl.qwikcilver.dto.preauth.ResCard
import com.ihcl.qwikcilver.dto.preauthcompletecancel.OriginalPreauthCompleteCancel
import com.ihcl.qwikcilver.dto.preauthcompletecancel.request.Card
import com.ihcl.qwikcilver.dto.preauthcompletecancel.request.OriginalRequest
import com.ihcl.qwikcilver.dto.preauthcompletecancel.request.PreauthCompleteCancelRequest
import com.ihcl.qwikcilver.dto.preauthcompletecancel.response.PreauthCompleteCancelResponse
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
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PreauthCompleteCancelService {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val databaseRepository by KoinJavaComponent.inject<DatabaseRepository>(DatabaseRepository::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    //This function makes a call to Preauth complete cancel API of QC, provides the response and throws exception if any occurred.
    suspend fun preAuthCompleteCancel(req: OriginalPreauthCompleteCancel): FEPreauthRes {
        val transactionId = databaseRepository.incrementCounter()
        val transactionsPreAuthURL = prop.qcBaseUrl + Constants.QC.Path.CANCEl_URL
        val accessToken = authService.getToken()
        val dateAtClient: String = balanceEnquiryService.getDateAtClient()
        val body = PreauthCompleteCancelRequest(
            listOf(
                Card(
                    req.CardNumber,
                    OriginalRequest(
                        req.OriginalRequest?.OriginalAmount,
                        req.OriginalRequest?.OriginalApprovalCode,
                        req.OriginalRequest?.OriginalBatchNumber,
                        req.OriginalRequest?.OriginalTransactionId
                    )
                )
            ), Constants.QC.RequestParams.INPUTTYPE,
            Constants.QC.PREAUTH_CANCEL_NOTE,
            Constants.QC.RequestParams.TRANSACTIONMODEID
        )
        log.debug("Preauth Complete Cancel QC request prepared as ${Json.encodeToString(body)}")
        try {
            val response: HttpResponse = ConfigureClient.client.post(transactionsPreAuthURL) {
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(HttpHeaders.Authorization, accessToken)
                    append(Constants.QC.HEADER_TRANSACTIONID, transactionId.toString())
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                }
                setBody(body)
                contentType(ContentType.Application.Json)
            }
            val qcRes: PreauthCompleteCancelResponse = response.body()
            log.debug("Preauth Complete Cancel response received from QC is {}", qcRes)
            if (qcRes.ResponseCode == Constants.QC.TOKEN_EXPIRED || qcRes.ResponseCode == Constants.QC.INVALID_TOKEN) {
                authService.generateAndUpdateToken()
                return preAuthCompleteCancel(req)
            }
            return FEPreauthRes(
                qcRes.TransactionId,
                qcRes.ResponseMessage,
                qcRes.ResponseCode?.toInt(),
                listOf(
                    ResCard(
                        qcRes.Cards?.get(0)?.Balance,
                        qcRes.Cards?.get(0)?.PreAuthCode,
                        qcRes.Cards?.get(0)?.ApprovalCode,
                        qcRes.Cards?.get(0)?.ResponseMessage,
                        qcRes.Cards?.get(0)?.ResponseCode,
                        qcRes.Cards?.get(0)?.TransactionDateTime,
                        qcRes.Cards?.get(0)?.TransactionAmount.toString()
                    )
                ),
                qcRes.CurrentBatchNumber
            )
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
     }
}