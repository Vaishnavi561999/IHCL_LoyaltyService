package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName
import com.ihcl.qwikcilver.dto.loyaltygravty.request.OriginalBitVoucherDetails

data class VoucherRedemptionAvailPrivilegeDTO(
    @SerializedName("original_bit")
    var originalBit: OriginalBit,
    @SerializedName("processing_date")
    var processingDate: String,
    @SerializedName("bit_id")
    var bitId: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("error")
    var error: Errorss,
    @SerializedName("loyalty_balances")
    var loyaltyBalances: String,
    @SerializedName("points_rewarded")
    var pointsRewarded: Boolean,
    @SerializedName("points_redeemed")
    var pointsRedeemed: Boolean,
    @SerializedName("offer_actions")
    var offerActions: ArrayList<String>,
    @SerializedName("assigned_privileges")
    var assignedPrivileges: ArrayList<String>,
    @SerializedName("availed_privileges")
    var availedPrivileges: ArrayList<String>,
    @SerializedName("billing_payment_rates")
    var billingPaymentRates: String,
    @SerializedName("points_reset")
    var pointsReset: Boolean,
    @SerializedName("sponsor_balance")
    var sponsorBalance: String,
    @SerializedName("member_id")
    var memberId: String,
)
data class OriginalBit(
    @SerializedName("header")
    var header: Header,
    @SerializedName("lines")
    var lines: ArrayList<String>,
    @SerializedName("payment_details")
    var paymentDetails: ArrayList<String>
)

data class Header(

    @SerializedName("h_sponsor_id")
    var hSponsorId: String,
    @SerializedName("h_rep_id")
    var hRepId: Int,
    @SerializedName("h_member_id")
    var hMemberId: String,
    @SerializedName("h_end_date")
    var hEndDate: String,
    @SerializedName("h_bit_source")
    var hBitSource: String,
    @SerializedName("h_comment")
    var hComment: String,
    @SerializedName("h_bit_amount")
    var hBitAmount: String,
    @SerializedName("rate_code")
    var rateCode: String,
    @SerializedName("h_bit_date")
    var hBitDate: String,
    @SerializedName("h_bit_category")
    var hBitCategory: String,
    @SerializedName("h_rep_email")
    var hRepEmail: String,
    @SerializedName("bill_number")
    var billNumber: String,
    @SerializedName("h_privileges")
    var hPrivileges: ArrayList<String>,
    @SerializedName("membership_card_number")
    var membershipCardNumber: String,
    @SerializedName("h_program_id")
    var hProgramId: Int,
    @SerializedName("folio_number")
    var folioNumber: String,
    @SerializedName("h_date_of_booking")
    var hDateOfBooking: String,
    @SerializedName("h_bit_source_generated_id")
    var hBitSourceGeneratedId: String,
    @SerializedName("h_start_date")
    var hStartDate: String,

    )

data class VoucherAvailResponse(
    val original_bit:OriginalBitChamberVoucherDetails,
    val availed_privileges:List<VoucherRedeemDetails>
)
data class VoucherRedeemDetails(
    val member_id:String,
    val privilege_code:String,
    val availment_bit_id:String,
    val start_date:String,
    val privilege_type:String,
    val usage_date:String,
    val status:String,
    val cdc:String,
)
data class OriginalBitChamberVoucherDetails(
    val header:ChamberHeader
)
data class ChamberHeader(
    val h_bit_date:String
)
data class ChamberVoucherAvailResponse(
    val type:String,
    val data:VoucherAvailResponse
)
data class ChamberVoucherReversalResponse(
    val type:String,
    val data:BitCancellationVoucherReversalResponse
)