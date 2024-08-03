package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.deactivate.FEDeactivateRes
import com.ihcl.qwikcilver.dto.deactivate.OriginalDeactivateRequest
import com.ihcl.qwikcilver.dto.deactivate.ResCard
import com.ihcl.qwikcilver.dto.deactivate.request.Card
import com.ihcl.qwikcilver.dto.deactivate.request.DeactivateRequest
import com.ihcl.qwikcilver.dto.deactivate.response.DeactivateResponse
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
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DeactivateService {
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val databaseRepository by KoinJavaComponent.inject<DatabaseRepository>(DatabaseRepository::class.java)
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    //This function makes a call to deactivate API of QC and provides response and throws exception if any occurred.
    suspend fun deactivate(request: OriginalDeactivateRequest): FEDeactivateRes? {
        val reloadUrl = prop.qcBaseUrl + Constants.QC.Path.TRANSACTIONS_URL
        val accessToken = authService.getToken()
        val transactionId = databaseRepository.incrementCounter()
        val dateAtClient: String = balanceEnquiryService.getDateAtClient()
        val body = DeactivateRequest(
            listOf(Card(request.CardNumber, request.CardPin)),
            Constants.QC.RequestParams.INPUTTYPE,
            Constants.QC.TransactionType.DEACTIVATE
        )
        log.debug("Deactivate QC request prepared as ${Json.encodeToString(body)}")
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
            val res: DeactivateResponse = response.body()
            log.debug("Deactivate card response received from QC is {}", res)
            if (res.ResponseCode.toString() == Constants.QC.TOKEN_EXPIRED || res.ResponseCode.toString() == Constants.QC.INVALID_TOKEN) {
                authService.generateAndUpdateToken()
                return deactivate(request)
            }
            return FEDeactivateRes(
                res.TransactionId,
                res.ResponseCode,
                listOf(
                    ResCard(
                        res.Cards?.get(0)?.ResponseMessage,
                        res.Cards?.get(0)?.ResponseCode,
                        res.Cards?.get(0)?.CardStatus,
                        res.Cards?.get(0)?.CardType
                    )
                )
            )
        } catch (e: Exception) {
            log.error("Exception occurred while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }

    }
}