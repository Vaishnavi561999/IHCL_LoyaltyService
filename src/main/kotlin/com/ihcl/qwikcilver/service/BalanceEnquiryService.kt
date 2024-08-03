package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.balanceenquiry.FEBalanceEnquiryResponse
import com.ihcl.qwikcilver.dto.balanceenquiry.OriginalBalanceEnquiryRequest
import com.ihcl.qwikcilver.dto.balanceenquiry.ResCard
import com.ihcl.qwikcilver.dto.balanceenquiry.request.BalanceEnquiryRequest
import com.ihcl.qwikcilver.dto.balanceenquiry.request.Card
import com.ihcl.qwikcilver.dto.balanceenquiry.response.BalanceEnquiryResponse
import com.ihcl.qwikcilver.exception.BadRequestException
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.repository.DatabaseRepository
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.Constants.BOOKINGS
import com.ihcl.qwikcilver.util.Constants.CARDNUMBER_OR_CARDPIN_EXPIRED
import com.ihcl.qwikcilver.util.Constants.CARD_NUMBER_DE_ACTIVE_CODE
import com.ihcl.qwikcilver.util.Constants.CARD_NUMBER_DE_ACTIVE_ERROR_MESSAGE
import com.ihcl.qwikcilver.util.Constants.CARD_NUMBER_INCORRECT
import com.ihcl.qwikcilver.util.Constants.CARD_NUMBER_OR_CARDPIN_INCORRECT_ERROR_MESSAGE
import com.ihcl.qwikcilver.util.Constants.CARD_PIN_INCORRECT
import com.ihcl.qwikcilver.util.Constants.CPG_ID
import com.ihcl.qwikcilver.util.Constants.ERROR_RESPONSE_MESSAGE_OF_GIFT_CARD
import com.ihcl.qwikcilver.util.Constants.HOTEL_BOOKING
import com.ihcl.qwikcilver.util.Constants.QC.TIME_ZONE_ID
import com.ihcl.qwikcilver.util.Constants.REDEEM_GIFT_CARD_ERROR_MESSAGE
import io.ktor.client.plugins.timeout
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.call.body
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import org.litote.kmongo.json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class BalanceEnquiryService {
    private val databaseRepository by KoinJavaComponent.inject<DatabaseRepository>(DatabaseRepository::class.java)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val prop = Configuration.env
    var value = 0

    //This function makes a call to balance enquiry API of QC and provides response and throws exception if any occurred.
    suspend fun giftCardBalanceEnquiry(req: OriginalBalanceEnquiryRequest): FEBalanceEnquiryResponse {
        val transactionId = databaseRepository.incrementCounter()
        val giftCardBalanceEnquiryURL = prop.qcBaseUrl + Constants.QC.Path.TRANSACTIONS_URL
        var cardType:String?
        val accessToken = authService.getToken()
        val dateAtClient: String = this.getDateAtClient()
        val body = BalanceEnquiryRequest(
            Constants.QC.TransactionType.BALANCE_ENQUIRY,
            Constants.QC.RequestParams.INPUTTYPE,
            req.balanceEnquiry.map {
                Card(
                    it.CardNumber,
                    it.CardPin
                )
            }
        )

        log.info("Balance Enquiry QC request prepared as ${Json.encodeToString(body)}")

        try {
            val response: HttpResponse = ConfigureClient.client.post(giftCardBalanceEnquiryURL) {
                timeout {
                    requestTimeoutMillis = prop.requestTimeoutMillis.toLong()
                }
                headers {
                    append(Constants.QC.HEADER_TRANSACTIONID, transactionId.toString())
                    append(HttpHeaders.Authorization, accessToken)
                    append(Constants.QC.HEADER_DATEATCLIENT, dateAtClient)
                }
                setBody(body)
                contentType(ContentType.Application.Json)
            }

            val qcRes: BalanceEnquiryResponse = response.body()
            log.debug("Balance enquiry response received from QC is {}", qcRes)

            if (qcRes.ResponseCode == Constants.QC.TOKEN_EXPIRED || qcRes.ResponseCode == Constants.QC.INVALID_TOKEN) {
                authService.generateAndUpdateToken()
                return giftCardBalanceEnquiry(req)
            }

            val feResponse = FEBalanceEnquiryResponse(
                qcRes.ResponseMessage,
                qcRes.ResponseCode,
                qcRes.Cards?.map {
                    ResCard(
                        it.CardNumber,
                        it.CardType,
                        it.TotalReloadedAmount,
                        it.TotalRedeemedAmount,
                        it.Balance?.toInt(),
                        it.ResponseCode.toString(),
                        it.ResponseMessage,
                        it.ExpiryDate
                    )
                }
            )
            when(feResponse.Cards?.first()?.ResponseCode){
                CARDNUMBER_OR_CARDPIN_EXPIRED.toString()->{feResponse.Cards.first()?.ResponseMessage=ERROR_RESPONSE_MESSAGE_OF_GIFT_CARD}
                CARD_NUMBER_DE_ACTIVE_CODE.toString()->{feResponse.Cards.first()?.ResponseMessage=CARD_NUMBER_DE_ACTIVE_ERROR_MESSAGE}
                CARD_NUMBER_INCORRECT.toString() ->{feResponse.Cards.first()?.ResponseMessage=CARD_NUMBER_OR_CARDPIN_INCORRECT_ERROR_MESSAGE}
                CARD_PIN_INCORRECT.toString() ->{ feResponse.Cards.first()?.ResponseMessage=CARD_NUMBER_OR_CARDPIN_INCORRECT_ERROR_MESSAGE }
            }
            if (feResponse.Cards?.first()?.ResponseCode!="0"){
                return feResponse
            }
            req.balanceEnquiry.forEach {
                if (it.type.equals(HOTEL_BOOKING, ignoreCase = true)) {
                    val giftCardDetails = databaseRepository.getGiftCardDetails(CPG_ID)
                    log.info("cpg restriction details is${giftCardDetails.json}")
                    cardType=qcRes.Cards?.first()?.CardType
                        if(giftCardDetails.first().giftCardValues?.any { ct -> ct.cardType.equals(cardType, ignoreCase = true) &&  ct.booking.equals(BOOKINGS, ignoreCase = true)} == true){
                            return feResponse
                        }else{
                            throw BadRequestException(REDEEM_GIFT_CARD_ERROR_MESSAGE)
                        }
                }
            }
            return feResponse
        } catch (e: BadRequestException) {
            log.error("Bad Request Exception occurred while calling API ${e.message} due to ${e.cause}")
            throw e
        }catch (e: Exception) {
            // Handle other exceptions
            log.error("Exception occurred while calling API ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
    }
    suspend fun validateBalanceEnquiry(call: ApplicationCall, response:FEBalanceEnquiryResponse){
        if (response.ResponseCode== Constants.QC.REQUEST_RESPONSECODE.toString()){
            call.respond(HttpStatusCode.OK,response)
        }else{
            call.respond(HttpStatusCode.BadRequest, response)
        }
    }

    //generates the current date and time in "yyyy-MM-dd'T'HH:mm:ss" format
    fun getDateAtClient(): String {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat(Constants.QC.DATE_FORMAT)
        return dateFormat.format(currentDate).toString()
    }
    fun getCurrentDateTime(): String? {
        val current = LocalDateTime.now( ZoneId.of(TIME_ZONE_ID))
        val offsetDate = OffsetDateTime.of(current, ZoneOffset.UTC)
        return offsetDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH))
    }
}