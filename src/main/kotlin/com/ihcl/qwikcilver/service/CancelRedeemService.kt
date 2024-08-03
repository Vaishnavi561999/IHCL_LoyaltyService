package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.cancelRedeem.FECancelRedeemRequest
import com.ihcl.qwikcilver.dto.cancelRedeem.request.CancelRedeemRequest
import com.ihcl.qwikcilver.dto.cancelRedeem.request.OriginalRequest
import com.ihcl.qwikcilver.dto.cancelRedeem.request.RequestCard
import com.ihcl.qwikcilver.dto.redeem.FERedeemResponse
import com.ihcl.qwikcilver.dto.redeem.ResCard
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
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CancelRedeemService {
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val databaseRepository by KoinJavaComponent.inject<DatabaseRepository>(DatabaseRepository::class.java)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    suspend fun cancelRedeem(request: FECancelRedeemRequest): FERedeemResponse{
        log.debug("Response received from Front end for cancel redeem is {}", request)
        val cancelRedeemUrl = prop.qcBaseUrl + Constants.QC.Path.CANCEl_URL
        val accessToken = authService.getToken()
        val transactionId = databaseRepository.incrementCounter()
        val dateAtClient: String = balanceEnquiryService.getDateAtClient()
        val body = CancelRedeemRequest(
            listOf(
                RequestCard(
                    request.Amount,
                    request.CardNumber,
                    OriginalRequest(
                        request.Amount,
                        request.OriginalApprovalCode,
                        request.OriginalBatchNumber,
                        request.OriginalTransactionId
                    )
                )
            ),
            Constants.QC.RequestParams.INPUTTYPE.toInt(),
            request.Note,
            Constants.QC.RequestParams.TRANSACTIONMODEID.toString()
        )
        log.debug("Cancel redeem QC request prepared as ${Json.encodeToString(body)}")
        try {
            val response: HttpResponse = ConfigureClient.client.post(cancelRedeemUrl) {
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
            val qcRes: RedeemResponse = response.body()
            log.debug("Response received from cancel redeem api is ${response.bodyAsText()}")
            if (qcRes.ResponseCode == Constants.QC.INVALID_TOKEN || qcRes.ResponseCode == Constants.QC.TOKEN_EXPIRED) {
                authService.generateAndUpdateToken()
                return cancelRedeem(request)
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

}

