package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.plugins.ConfigureClient
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.plugins.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MemberLookupValidationService {
    private val props = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)

    suspend fun getMemberDetails(param:String,type:String):HttpResponse{
        val response:HttpResponse
        try {
            val token= authService.getGravtyAuthToken()
            response= ConfigureClient.client.get ("${props.memberLookUpValidationURL}?$type=$param"){
                    headers{
                        append(props.gravityHeaderKey,props.epicureHeaderCode)
                        append(props.authorizationKey,token)
                    }
                timeout {
                    requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                }
                    contentType(ContentType.Application.Json)
            }
            if (response.status== HttpStatusCode.Unauthorized){
                authService.getGravtyAuthToken()
                return getMemberDetails(param,type)
            }

        }catch (e:Exception){
            log.error("MemberLookupValidation API Response while getting  : Exception raised : ${e.message}")
            throw e.message?.let { BadRequestException(it) }!!
        }
        log.debug("MemberLookup Epicure API response.  : $response" )
        return response
    }
}