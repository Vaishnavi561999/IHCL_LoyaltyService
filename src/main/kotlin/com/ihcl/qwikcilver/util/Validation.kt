package com.ihcl.qwikcilver.util

import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.loyaltygravty.request.PinDisableVoucherRedemption
import com.ihcl.qwikcilver.dto.loyaltygravty.request.PinEnabled
import com.ihcl.qwikcilver.dto.loyaltygravty.request.PinEnabledVoucherRedemption
import com.ihcl.qwikcilver.dto.loyaltygravty.request.VoucherRedemptionAvailPrivileges
import com.ihcl.qwikcilver.dto.loyaltygravty.response.*
import com.ihcl.qwikcilver.exception.QCBadRequestException
import com.ihcl.qwikcilver.service.AuthorizationService
import com.ihcl.qwikcilver.service.MemberLookupValidationService
import io.konform.validation.Invalid
import io.konform.validation.ValidationResult
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.koin.java.KoinJavaComponent

private val props = Configuration.env
val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)

fun <T> validateRequestBody(validateResult: ValidationResult<T>){
    if (validateResult is Invalid) {
        throw QCBadRequestException(
            HttpStatusCode.BadRequest.value,
            message = ResponseMessages.VALIDATION_ERROR_OCCURRED + "${validateResult.errors}",
            errorMessage = generateErrors(validateResult)
        )
    }

}
suspend fun validateLoginResponse(call: ApplicationCall,response: HttpResponse){
    when (response.status) {
        HttpStatusCode.Created -> {
            call.respond(HttpStatusCode.Created, response.body() as MemberEnrollmentCreateDTO)
        }

        HttpStatusCode.BadRequest -> {
            call.respond(HttpStatusCode.BadRequest, response.body() as ErrorsDTO)
        }

        HttpStatusCode.Forbidden -> {
            call.respond(HttpStatusCode.Forbidden, response.body() as Forbidden)
        }
    }
}

suspend fun validateMemberResponse(call: ApplicationCall,response: HttpResponse){
    when (response.status) {
        HttpStatusCode.Created -> {
            call.respond(HttpStatusCode.Created, response.body() as MemberEnrollmentCreateDTO)
        }

        HttpStatusCode.Accepted->{
            call.respond(HttpStatusCode.Created, response.body() as MemberEnrollmentAcceptDto)
        }


        HttpStatusCode.BadRequest -> {
            call.respond(HttpStatusCode.BadRequest, response.body() as ErrorsDTO)
        }

        HttpStatusCode.Forbidden -> {
            call.respond(HttpStatusCode.Forbidden, response.body() as Forbidden)
        }
    }
}
suspend fun validateMemberLookUpResponse(call: ApplicationCall,response: HttpResponse){
    when (response.status) {
        HttpStatusCode.OK -> {
            call.respond(HttpStatusCode.OK, response.body() as MemberLookupDTO)
        }

        HttpStatusCode.BadRequest -> {
            call.respond(HttpStatusCode.BadRequest, response.body() as MembersErrorDTO)
        }

        HttpStatusCode.Forbidden -> {
            call.respond(HttpStatusCode.Forbidden, response.body() as Forbidden)
        }
    }
}
suspend fun validateMembershipPlanResponse(call: ApplicationCall,response: HttpResponse){
    when (response.status) {
        HttpStatusCode.BadRequest -> {
            call.respond(HttpStatusCode.BadRequest, response.body() as MemberShipBadRequestError)
        }

        HttpStatusCode.Forbidden -> {
            call.respond(HttpStatusCode.Forbidden, response.body() as MembersErrorDTO)
        }

        HttpStatusCode.Created -> {
            call.respond(HttpStatusCode.Created, response.body() as MemberShipPlanDTO)
        }

    }
}
suspend fun validateMemberShipResponse(call: ApplicationCall,email:String?,mobile:String?,memberId:String?){
    if (email != null || mobile != null || memberId != null) {
        val response = MemberLookupValidationService().getMemberDetails(mobile.toString(), "mobile")
        val memberShipId = response.body<List<MemberLookupDTORes>>()
        if (memberShipId.isEmpty()) {
            val response = MemberLookupValidationService().getMemberDetails(email.toString(), "email")
            call.respond(HttpStatusCode.OK, response.body() as List<MemberLookupValidationDTO>)
        } else if (memberId != null) {
            val response = MemberLookupValidationService().getMemberDetails(memberId, "member_id")
            call.respond(HttpStatusCode.OK, response.body() as List<MemberLookupValidationDTO>)
        } else {
            call.respond(HttpStatusCode.OK, response.body() as List<MemberLookupValidationDTO>)
        }
    }
}
suspend fun validateAddOnCardResponse(call: ApplicationCall,response: HttpResponse){
    when (response.status) {
        HttpStatusCode.BadRequest -> {
            call.respond(HttpStatusCode.BadRequest, response.body() as ErrorsDTO)
        }

        HttpStatusCode.Forbidden -> {
            call.respond(HttpStatusCode.Forbidden, response.body() as Forbidden)
        }

        HttpStatusCode.Created -> {
            call.respond(HttpStatusCode.Created, response.body() as AddOnCardResponse)
        }

        HttpStatusCode.NotAcceptable -> {
            call.respond(HttpStatusCode.NotAcceptable, response.body() as VoucherRedemptionErrorDTO)
        }
    }
}
suspend fun validateMemberCardsResponse(call: ApplicationCall,response: HttpResponse){
    when (response.status) {
        HttpStatusCode.Created -> {
            call.respond(HttpStatusCode.Created, response.body() as EpicurePrimaryCardResponse)
        }

        HttpStatusCode.BadRequest -> {
            call.respond(HttpStatusCode.BadRequest, response.body() as ErrorsDTO)
        }

        HttpStatusCode.Forbidden -> {
            call.respond(HttpStatusCode.Forbidden, response.body() as Forbidden)
        }
    }
}
suspend fun validateFetchMembershipResponse(call: ApplicationCall,response: HttpResponse){
    when(response.status){
        HttpStatusCode.OK -> {
            val memberCards= response.body() as GravtyMemberCards
            val cardList=memberCards.data
            call.respond(HttpStatusCode.OK, cardList.sortedByDescending { it.cardStatus == Constants.GRAVTY_CARD_STATUS })
        }

        HttpStatusCode.Forbidden -> {
            call.respond(HttpStatusCode.Forbidden, response.body() as Forbidden)
        }
    }
}
fun validatePrivilegePin(body: VoucherRedemptionAvailPrivileges):Any{
   val pinEnabledBody= if (body.pin.isNullOrEmpty()) {
        PinDisableVoucherRedemption(
            body.hotelSponsorId.toInt(),
            body.hBitDate,
            if (body.type == Constants.EPICURE) props.epicureProgramId.toInt() else props.chamberProgramId.toInt(),
            body.hMemberId,
            Constants.AVAILMENT,
            listOf(
                body.hPrivileges
            ),
        )
    } else {
        PinEnabledVoucherRedemption(
            body.hotelSponsorId.toInt(),
            body.hBitDate,
            if (body.type == Constants.EPICURE) props.epicureProgramId.toInt() else props.chamberProgramId.toInt(),
            body.hMemberId,
            Constants.AVAILMENT,
            listOf(
                PinEnabled(body.hPrivileges, body.pin)
            )
        )
    }
    return pinEnabledBody
}
