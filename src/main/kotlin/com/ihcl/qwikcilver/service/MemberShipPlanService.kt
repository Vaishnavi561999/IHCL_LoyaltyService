package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.loyaltygravty.request.MemberShipPlan
import com.ihcl.qwikcilver.exception.HttpResponseException
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants
import io.ktor.client.plugins.*
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.contentType
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MemberShipPlanService {
    private val props = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)

    /*    Method is used to call gravty membership plan APi and creates membership plan   */

    suspend fun createMemberShipPlan(body: MemberShipPlan):HttpResponse{
        val response:HttpResponse
        try {
            val token= authService.getGravtyAuthToken()

            log.info("Membership Plan request: ${Json.encodeToString(body)}")
            response= ConfigureClient.client.post  (props.createMembershipPlan){
                headers{
                    append(props.gravityHeaderKey,props.epicureHeaderCode)
                    append(props.authorizationKey,token)
                }
                timeout {
                    requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
            if (response.status== HttpStatusCode.Unauthorized){
                authService.getGravtyAuthToken()
                return createMemberShipPlan(body)
            }

            log.info("Membership Plan response: ${response.bodyAsText()}")
        }catch (e:Exception){
            log.error("Creating MemberShip Plan API Response while getting  : Exception raised : ${e.message}")
            throw e.message?.
            let {HttpResponseException(Constants.MEMBERSHIP_PLAN_NOT_CREATED,HttpStatusCode.BadRequest) }!!
        }
        log.debug("Creating MemberShip Plan Epicure API response.  : {}", response)
        return response
    }
}