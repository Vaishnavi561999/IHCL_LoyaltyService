package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.preauth.FEPreauthRes
import com.ihcl.qwikcilver.dto.preauth.ResCard
import com.ihcl.qwikcilver.dto.preauthcancel.OriginalPreauthCancelRequest
import com.ihcl.qwikcilver.dto.preauthcancel.request.Card
import com.ihcl.qwikcilver.dto.preauthcancel.request.OriginalRequest
import com.ihcl.qwikcilver.dto.preauthcancel.request.PreauthCancelRequest
import com.ihcl.qwikcilver.dto.preauthcancel.response.PreauthCancelResponse
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

class PreauthCancelService {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val databaseRepository by KoinJavaComponent.inject<DatabaseRepository>(DatabaseRepository::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)

    //This function makes a call to Preauth cancel API of QC to cancel the blocked amount of GC then provides the response and throws exception if any occurred.
    suspend fun preAuthCancel(body: OriginalPreauthCancelRequest): FEPreauthRes {
        val transactionId = databaseRepository.incrementCounter()
        val transactionsPreAuthURL = prop.qcBaseUrl + Constants.QC.Path.CANCEl_URL
        val accessToken = authService.getToken()
        val dateAtClient: String = balanceEnquiryService.getDateAtClient()
        val req = PreauthCancelRequest(
            listOf(
                Card(
                    body.CardNumber,
                    OriginalRequest(
                        body.OriginalRequest?.OriginalAmount, body.OriginalRequest?.OriginalApprovalCode,
                        body.OriginalRequest?.OriginalBatchNumber, body.OriginalRequest?.OriginalTransactionId
                    )
                )
            ), Constants.QC.RequestParams.INPUTTYPE,
            Constants.QC.PREAUTH_CANCEL_NOTE,
            Constants.QC.RequestParams.TRANSACTIONMODEID
        )
        log.debug("PreauthCancel QC request prepared as ${Json.encodeToString(req)}")
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
                setBody(req)
                contentType(ContentType.Application.Json)
            }
            val res: PreauthCancelResponse = response.body()
            log.debug("Preauth Cancel card response received from QC is {}", res)
            if (res.ResponseCode == Constants.QC.TOKEN_EXPIRED || res.ResponseCode == Constants.QC.INVALID_TOKEN) {
                authService.generateAndUpdateToken()
                return preAuthCancel(body)
            }
            return FEPreauthRes(
                res.TransactionId,
                res.ResponseMessage,
                res.ResponseCode?.toInt(),
                listOf(
                    ResCard(
                        res.Cards?.get(0)?.Balance,
                        res.Cards?.get(0)?.PreAuthCode,
                        res.Cards?.get(0)?.ApprovalCode,
                        res.Cards?.get(0)?.ResponseMessage,
                        res.Cards?.get(0)?.ResponseCode,
                        res.Cards?.get(0)?.TransactionDateTime,
                        res.Cards?.get(0)?.TransactionAmount.toString()
                    )
                ),
                res.CurrentBatchNumber
            )
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
     }
}