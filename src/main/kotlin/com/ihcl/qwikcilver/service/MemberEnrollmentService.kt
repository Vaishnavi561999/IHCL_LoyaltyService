package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.plugins.ConfigureClient.client
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.loyaltygravty.request.MemberEnrollment
import io.ktor.client.plugins.*
import io.ktor.client.request.post
import io.ktor.client.request.headers
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.http.contentType
import io.ktor.http.ContentType
import io.ktor.server.plugins.BadRequestException
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MemberEnrollmentService {
    private val props = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)

    /*  Method is used to call MemberLookUp gravty API for logged-In user  and fetch memberEnrollment member list with member ID */
    suspend fun fetchMemberEnrollmentLogedInList(body: MemberEnrollment):HttpResponse{
        val response:HttpResponse
        try {
            val token=authService.getGravtyAuthToken()
            log.info("TOKEN..............$token")
             response=client.post  (props.memberEnrollmentURL){
                 timeout {
                     requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                 }
                 headers{
                     append(props.gravityHeaderKey,props.epicureHeaderCode)
                     append(props.authorizationKey,token)
                 }
                 contentType(ContentType.Application.Json)
                 setBody(body)
            }
            if (response.status== HttpStatusCode.Unauthorized){
                authService.getGravtyAuthToken()
                return fetchMemberEnrollmentLogedInList(body)
            }
        }catch (e:Exception){
            log.error("MemberEnrollmentEpicure API for logged-In user Response while getting  : Exception raised : ${e.message}")
            throw e.message?.let { BadRequestException(it) }!!
        }
        log.info("MemberEnrollment Epicure API response.  : $response" )
        return response
    }

    /*  Method is used to call MemberLookUp gravty API for non-logged-In user  and fetch memberEnrollment member list with member ID */
    suspend fun fetchMemberEnrollmentList(body: MemberEnrollment):HttpResponse{
        val response:HttpResponse
        try {
            response=client.post  (props.memberEnrollmentURL){
                headers{
                    append(props.gravityHeaderKey,props.epicureHeaderCode)
                }
                timeout {
                    requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }catch (e:Exception){
            log.error("MemberEnrollmentEpicure API Response while getting  : Exception raised : ${e.message}")
            throw e.message?.let { BadRequestException(it) }!!
        }
        log.info("MemberEnrollment Epicure API response.  : $response" )
        return response
    }
}