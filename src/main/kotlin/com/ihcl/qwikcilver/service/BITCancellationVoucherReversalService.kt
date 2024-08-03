package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.loyaltygravty.request.BitCancellationRequest
import com.ihcl.qwikcilver.dto.loyaltygravty.request.BitCancellationVoucherReversal
import com.ihcl.qwikcilver.dto.loyaltygravty.response.*
import com.ihcl.qwikcilver.exception.HttpResponseException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants.BIT_CANCELLATION_NOT_DONE
import com.ihcl.qwikcilver.util.Constants.CANCELLATION
import com.ihcl.qwikcilver.util.Constants.CHAMBERS
import com.ihcl.qwikcilver.util.Constants.EPICURE
import com.ihcl.qwikcilver.util.Constants.REVERSAL
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BITCancellationVoucherReversalService {
    private val props = Configuration.env
    private lateinit var cancellationRequest: HttpResponse
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val fetchMemberShipService by KoinJavaComponent.inject<FetchMemberShipsService>(FetchMemberShipsService::class.java)

    /* Method is used to call BITCancellation voucher reversal gravty Api and
    used for reversal of already processed Transactions */

    suspend fun cancellationVoucherReversal(body: BitCancellationVoucherReversal): Any {
        lateinit var response: HttpResponse
        if (body.type == EPICURE) {
            try {
                val request = BitCancellationRequest(
                    body.hBitDate,
                    body.hotelSponsorId.toInt(),
                    CANCELLATION,
                    REVERSAL,
                    body.cancelBitId,
                    props.epicureProgramId.toInt(),
                    body.hMemberId
                )
                val token = authService.getGravtyAuthToken()
                response = ConfigureClient.client.post(props.bitCancellationVoucher) {
                    headers {
                        append(props.gravityHeaderKey, props.epicureHeaderCode)
                        append(props.authorizationKey, token)
                    }
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                if (response.status == HttpStatusCode.Unauthorized) {
                    authService.getGravtyAuthToken()
                    return cancellationVoucherReversal(body)
                }
                log.info("BIT Cancellation Voucher reversal  API response.  : $response")
                when (response.status) {
                    HttpStatusCode.OK -> {
                        val epicureReversalResponse=response.body() as BitCancellationVoucherReversalResponse
                        return ChamberVoucherReversalResponse(EPICURE,epicureReversalResponse)
                    }

                    HttpStatusCode.BadRequest -> {
                        return response.body() as ErrorsDTO
                    }

                    HttpStatusCode.Forbidden -> {
                        return response.body() as Forbidden
                    }
                    HttpStatusCode.Created->{
                        return response.body() as VoucherReversalResponse
                    }
                    HttpStatusCode.NotAcceptable->{
                        return response.body() as VoucherRedemptionErrorDTO
                    }
                }
            } catch (e: Exception) {
                log.error(
                    "BIT Cancellation Voucher reversal  API Response while getting  " +
                            ": Exception raised : ${e.message}"
                )
                log.debug("Creating MemberShip Plan Epicure API response.  : {}", response)
                throw e.message?.let {
                    HttpResponseException(
                        BIT_CANCELLATION_NOT_DONE,
                        HttpStatusCode.BadRequest
                    )
                }!!
            }
        } else {
            try {
                val request = BitCancellationRequest(
                    body.hBitDate,
                    body.hotelSponsorId.toInt(),
                    CANCELLATION,
                    REVERSAL,
                    body.cancelBitId,
                   props.chamberProgramId.toInt(),
                    body.hMemberId
                )
                val token = fetchMemberShipService.getChambersAuthToken()
                response = ConfigureClient.client.post(props.bitCancellationVoucher) {
                    headers {
                        append(props.gravityHeaderKey, props.chambersHeaderKey)
                        append(props.authorizationKey, token)
                    }
                    timeout {
                        requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                    }
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }
                if (response.status== HttpStatusCode.Unauthorized){
                    fetchMemberShipService.getChambersAuthToken()
                    return cancellationVoucherReversal(body)
                }
                log.info("BIT Cancellation Voucher reversal for chambers API response.  : $response")
                when (response.status) {
                    HttpStatusCode.OK -> {
                        val epicureReversalResponse=response.body() as BitCancellationVoucherReversalResponse
                        return ChamberVoucherReversalResponse(CHAMBERS,epicureReversalResponse)
                    }

                    HttpStatusCode.BadRequest -> {
                        return response.body() as ErrorsDTO
                    }

                    HttpStatusCode.Forbidden -> {
                        return response.body() as Forbidden
                    }
                    HttpStatusCode.Created->{
                        return response.body() as VoucherReversalResponse
                    }
                    HttpStatusCode.NotAcceptable->{
                        return response.body() as VoucherRedemptionErrorDTO
                    }
                }
            } catch (e: Exception) {
                log.error(
                    "BIT Cancellation Voucher reversal  API for chambers Response while getting  " +
                            ": Exception raised : ${e.message}"
                )
                log.debug("Creating MemberShip Plan Epicure API response.  : {}", response)
                throw e.message?.let {
                    HttpResponseException(
                        BIT_CANCELLATION_NOT_DONE,
                        HttpStatusCode.BadRequest
                    )
                }!!
            }
        }
            return response
        }
    }