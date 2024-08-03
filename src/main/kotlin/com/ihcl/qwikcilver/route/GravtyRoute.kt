package com.ihcl.qwikcilver.route


import com.ihcl.qwikcilver.dto.loyaltygravty.request.*
import com.ihcl.qwikcilver.service.*
import com.ihcl.qwikcilver.util.*
import com.ihcl.qwikcilver.util.Constants.DEFAULT_LIMIT
import com.ihcl.qwikcilver.util.Constants.GRAVTY_EMAIL
import com.ihcl.qwikcilver.util.Constants.GRAVTY_MEMBER_ID
import com.ihcl.qwikcilver.util.Constants.GRAVTY_MOBILE
import com.ihcl.qwikcilver.util.Constants.VOUCHER_AUTHORIZATION
import com.ihcl.qwikcilver.util.Constants.VOUCHER_LIMIT
import com.ihcl.qwikcilver.util.Constants.VOUCHER_PRODUCT_CATEGORY
import com.ihcl.qwikcilver.util.Constants.VOUCHER_TYPE
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import org.koin.java.KoinJavaComponent
import org.slf4j.LoggerFactory

fun Application.configureGravtyRouting() {
    val log: org.slf4j.Logger = LoggerFactory.getLogger(javaClass)
    val loyaltyGravty by KoinJavaComponent.inject<MemberEnrollmentService>(MemberEnrollmentService::class.java)
    val memberLookup by KoinJavaComponent.inject<MemberLookupService>(MemberLookupService::class.java)
    val addOnCard by KoinJavaComponent.inject<AddOnCardService>(AddOnCardService::class.java)
    val bitCancellation by KoinJavaComponent.inject<BITCancellationVoucherReversalService>(
        BITCancellationVoucherReversalService::class.java
    )
    val membershipPlan by KoinJavaComponent.inject<MemberShipPlanService>(MemberShipPlanService::class.java)
    val voucherRedemption by KoinJavaComponent.inject<VoucherRedemptionService>(VoucherRedemptionService::class.java)
    val primaryCard by KoinJavaComponent.inject<EpicurePrimaryCardService>(EpicurePrimaryCardService::class.java)
    val memberCards by KoinJavaComponent.inject<GravtyMemberCardsService>(GravtyMemberCardsService::class.java)
    val fetchMemberShipService by KoinJavaComponent.inject<FetchMemberShipsService>(FetchMemberShipsService::class.java)
    routing {
        get("/") {
            call.respond("loyalty....")
        }
        route("/v1/gravty") {
            post("/members/LoggedIn") {
                val body = call.receive<MemberEnrollment>()
                log.info("MemberEnrollment request body :  $body")
                val response = loyaltyGravty.fetchMemberEnrollmentLogedInList(body)
                validateLoginResponse(call,response)
            }
            post("/members") {
                val body = call.receive<MemberEnrollment>()
                log.debug("MemberEnrollment request body :  {}", body)
                val response = loyaltyGravty.fetchMemberEnrollmentList(body)
                validateMemberResponse(call,response)
            }

            get("/members/data/{member_id}") {
                val memberId = call.parameters["member_id"]
                log.info("Member Id : $memberId")
                val response = memberLookup.getMemberDetails(memberId!!)
                validateMemberLookUpResponse(call,response)
            }

            post("/entity-data/members/membership_plan/") {
                val body = call.receive<MemberShipPlan>()
                log.debug("MemberShip plan request body : {}", body)
                val response = membershipPlan.createMemberShipPlan(body)
                validateMembershipPlanResponse(call,response)
            }
            post("/cancel/bits") {
                val body = call.receive<BitCancellationVoucherReversal>()
                log.debug("BITCancellation Voucher reversal request body :  {}", body)
                call.respond(bitCancellation.cancellationVoucherReversal(body))
            }

            post("/members/avail/bits") {
                val body = call.receive<VoucherRedemptionAvailPrivileges>()
                log.debug("AvailPrivileges Voucher redemption request body :  {}", body)
                call.respond(voucherRedemption.getAvailPrivileges(body))
            }

            get("/members/list/") {
                val email = call.request.queryParameters[GRAVTY_EMAIL]
                val mobile = call.request.queryParameters[GRAVTY_MOBILE]
                val memberId = call.request.queryParameters[GRAVTY_MEMBER_ID]

                validateMemberShipResponse(call,email,mobile,memberId)

            }
            post("/entity-data/members/members_cards/") {
                val body = call.receive<AddOnCard>()
                log.debug("Add on card request body :  {}", body)
                val response = addOnCard.getAddOnCard(body)
                validateAddOnCardResponse(call,response)
            }
            get("/fetch-vouchers") {
               val productCategory=call.request.queryParameters[VOUCHER_PRODUCT_CATEGORY]
                val type = call.request.queryParameters[VOUCHER_TYPE]?.toUpperCasePreservingASCIIRules()
                val token = call.request.headers[VOUCHER_AUTHORIZATION]
                val limit=call.request.queryParameters[VOUCHER_LIMIT]?:DEFAULT_LIMIT
                voucherRedemption.getPrivileges(call, productCategory, type, token, limit)
            }

            post("/entity-data/members/members_cards/primary") {
                val primaryCardRequest = call.receive<EpicurePrimaryCard>()
                log.info("Primary card request received: $primaryCardRequest")

                val response=primaryCard.createPrimaryCard(primaryCardRequest)
                validateMemberCardsResponse(call,response)
            }

            get ("/members/members_cards") {
                val authorizationToken=call.request.headers["Authorization"]
                log.info("Authorization Token :  $authorizationToken")
                val fetchMemberShips =
                    authorizationToken?.let { it1 -> fetchMemberShipService.getMemberShipDetails(it1)}?.message
                log.info("Fetch membership Response size ..${fetchMemberShips?.size}")
                val memberId=fetchMemberShipService.getMemberId(fetchMemberShips)
                log.info("Member id received : $memberId")
                val response= memberId.let { it1 -> memberCards.getMemberCards(it1) }
                validateFetchMembershipResponse(call,response)
            }
        }
    }
}
