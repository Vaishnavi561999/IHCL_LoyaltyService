package com.ihcl.qwikcilver.service

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.plugins.ConfigureClient
import io.ktor.client.plugins.*
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.server.plugins.BadRequestException
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MemberLookupService {
    private val props = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)

    /*  Method is used to call MemberLookUp gravty API and get the list of Members from GRAVTY®  */
    suspend fun getMemberDetails(memberId:String):HttpResponse{
        val response:HttpResponse
        try {

            val token=authService.getGravtyAuthToken()
            log.info("TOKEN..............$token")

            response= ConfigureClient.client.get ("${props.memberLookUp}/$memberId"){
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
                return getMemberDetails(memberId)
            }

        }catch (e:Exception){
            log.error("MemberLookupEpicure API Response while getting  : Exception raised : ${e.message}")
            throw e.message?.let { BadRequestException(it) }!!
        }
        log.debug("MemberLookup Epicure API response.  : $response" )
        return response
    }
}