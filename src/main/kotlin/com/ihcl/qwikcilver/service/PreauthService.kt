package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.preauth.FEPreauthRes
import com.ihcl.qwikcilver.dto.preauth.OriginalPreauthRequest
import com.ihcl.qwikcilver.dto.preauth.ResCard
import com.ihcl.qwikcilver.dto.preauth.request.Card
import com.ihcl.qwikcilver.dto.preauth.request.PreauthRequest
import com.ihcl.qwikcilver.dto.preauth.response.PreAuthResponseDto
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PreauthService {

    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val databaseRepository by KoinJavaComponent.inject<DatabaseRepository>(DatabaseRepository::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    //This function makes a call to Preauth API of QC, provides the response and throws exception if any occurred.
    suspend fun preAuth(request: OriginalPreauthRequest): FEPreauthRes {
        val transactionId = databaseRepository.incrementCounter()
        val transactionsPreAuthURL = prop.qcBaseUrl + Constants.QC.Path.TRANSACTIONS_URL
        val accessToken = authService.getToken()
        val dateAtClient: String = balanceEnquiryService.getDateAtClient()
        val body = PreauthRequest(
            listOf(Card(request.amount, request.cardNumber, request.cardPin)),
            Constants.QC.RequestParams.INPUTTYPE,
            Constants.QC.RequestParams.PREAUTHTYPE,
            Constants.QC.TransactionType.REDEEM
        )
        log.debug("Preauth QC request prepared as ${Json.encodeToString(body)}")
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
            val res = response.body<PreAuthResponseDto>()
            log.debug("Pre-auth response received from QC is {}", res)
            if (res.ResponseCode == Constants.QC.TOKEN_EXPIRED || res.ResponseCode == Constants.QC.INVALID_TOKEN) {
                authService.generateAndUpdateToken()
                return preAuth(request)
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
    fun validateResponse(response:FEPreauthRes): HttpStatusCode {
        return if (response.responseCode == Constants.QC.REQUEST_RESPONSECODE) {
            HttpStatusCode.OK
        } else  {
            HttpStatusCode.BadRequest
        }
    }
}