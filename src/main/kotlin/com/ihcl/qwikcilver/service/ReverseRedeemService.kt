package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.redeem.FERedeemResponse
import com.ihcl.qwikcilver.dto.redeem.ResCard
import com.ihcl.qwikcilver.dto.redeem.response.RedeemResponse
import com.ihcl.qwikcilver.dto.reverseRedeem.FEReverseRedeemRequest
import com.ihcl.qwikcilver.dto.reverseRedeem.request.Card
import com.ihcl.qwikcilver.dto.reverseRedeem.request.ReverseRedeemRequest
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ReverseRedeemService {
    private val prop = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    val balanceEnquiryService by KoinJavaComponent.inject<BalanceEnquiryService>(BalanceEnquiryService::class.java)
    suspend fun reverseRedeem(request: FEReverseRedeemRequest):FERedeemResponse{
        log.debug("Response received from Front end for reverse the redeemed gc is {}", request)
        val reverseRedeemUrl = prop.qcBaseUrl + Constants.QC.Path.REVERSE_URL
        val accessToken = authService.getToken()
        val dateAtClient: String = balanceEnquiryService.getDateAtClient()
        val body = ReverseRedeemRequest(listOf(Card(request.Amount,request.CardNumber)),Constants.QC.RequestParams.INPUTTYPE)
        log.debug("Reverse redeem request is prepared as {}", body)
        try {
            val response: HttpResponse = ConfigureClient.client.post(reverseRedeemUrl){
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(Constants.QC.HEADER_TRANSACTIONID, request.TransactionId!!)
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                    append(HttpHeaders.Authorization, accessToken)
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            val qcRes: RedeemResponse = response.body()
            log.debug("Response received from reverse redeem api is ${response.bodyAsText()}")
            if (qcRes.ResponseCode == Constants.QC.TOKEN_EXPIRED || qcRes.ResponseCode == Constants.QC.INVALID_TOKEN) {
                authService.generateAndUpdateToken()
               return  reverseRedeem(request)
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
        }catch (e:Exception){
            log.error("Exception occurred while calling reverse redeem api is ${e.message}")
            throw Exception(e.message)
        }
    }
}