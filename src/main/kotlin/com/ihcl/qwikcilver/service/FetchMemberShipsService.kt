package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.config.RedisConfig
import com.ihcl.qwikcilver.dto.loyaltygravty.response.*
import com.ihcl.qwikcilver.exception.QCInternalServerException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import com.ihcl.qwikcilver.util.Constants.CHAMBERS_TOKEN
import com.ihcl.qwikcilver.util.Constants.GRAVTY_EXTERNAL_ID
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class FetchMemberShipsService {
    private val props = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    private val voucherRedemption by KoinJavaComponent.inject<VoucherRedemptionService>(VoucherRedemptionService::class.java)

    private suspend fun getChambersToken(): ChambersToken {
        val response: HttpResponse
        val requestBody = ChambersTokenCredentials(props.gravtyUserName, props.gravtyChamberPassWord)
        response = ConfigureClient.client.post(props.gravtyTokenURL) {
            headers {
                append(props.gravityHeaderKey, props.chambersHeaderKey)
            }
            timeout {
                requestTimeoutMillis = props.requestTimeoutMillis.toLong()
            }
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }
        log.info("response body of chambers token is${response.bodyAsText()}")
        return response.body() as ChambersToken
    }
    private suspend fun generateUpdateChambersToken(): String {
        val token =getChambersToken()
        val accessToken = Constants.JWT_TOKEN +" "+ token.token
        RedisConfig.setKeyAndTTL(CHAMBERS_TOKEN,accessToken,token.expires_in!!.toLong())
        return accessToken
    }
    suspend fun getChambersAuthToken(): String {
        val token = RedisConfig.getKey(CHAMBERS_TOKEN)
        log.debug("token received from redis for chambers is $token")
        if (token.isNullOrBlank()) {
             return generateUpdateChambersToken()
        }
        return token
    }
    suspend fun getMemberShipDetails(token: String) : FetchEpicureMemberShip {
        val fetchMemberShipResponse: HttpResponse
        try {
            fetchMemberShipResponse = ConfigureClient.client.get(props.gravtyFetchMemberShip) {
                headers {
                    append(props.authorizationKey, "Bearer $token")
                }
                timeout {
                    requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                }
            }
        }catch (e: Exception) {
            log.error("Exception occured while calling api ${e.message} due to ${e.cause}")
            throw QCInternalServerException(e.message)
        }
        return fetchMemberShipResponse.body() as FetchEpicureMemberShip
    }
    suspend fun  getChambersResponse(externalId: String): HttpResponse {
        val chambersResponse: HttpResponse
        val token = getChambersAuthToken()                      // Chambers Token
        var memberId=""
        chambersResponse =
            ConfigureClient.client.get("${props.memberLookUpValidationURL}?$GRAVTY_EXTERNAL_ID=$externalId") {
                headers {
                    append(props.gravityHeaderKey, props.chambersHeaderKey)
                    append(props.authorizationKey, token)
                }
                timeout {
                    requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                }
            }
        if (chambersResponse.status== HttpStatusCode.Unauthorized){
            getChambersAuthToken()
            return getChambersResponse(externalId)
        }
        val userDetails = chambersResponse.body() as List<ChambersLookUpValidation>
        if (userDetails.isNotEmpty()){
            memberId=userDetails[0].member_id
        }
        return voucherRedemption.getChamberPrivileges(memberId,token)
    }
     fun getMemberId(memberShipList: ArrayList<Message>?): String{
        var memberId=""
        for (memberShip in memberShipList!!) {
            if (memberShip.brandMembershipId!=null) {
                if (memberShip.brandMembershipId?.length!! > 4) {
                    memberId = memberShip.brandMembershipId!!
                }
            }
        }
        return memberId
    }
}