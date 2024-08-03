package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName

data class VoucherRedemptionErrorDTO(
    @SerializedName("error") var error: Error?
)

data class AvailHeader(

    @SerializedName("h_sponsor_id") var hSponsorId: String? = null,
    @SerializedName("h_member_id") var hMemberId: String? = null,
    @SerializedName("h_end_date") var hEndDate: String? = null,
    @SerializedName("h_bit_source") var hBitSource: String? = null,
    @SerializedName("h_location") var hLocation: String? = null,
    @SerializedName("h_comment") var hComment: String? = null,
    @SerializedName("h_bit_amount") var hBitAmount: String? = null,
    @SerializedName("rate_code") var rateCode: String? = null,
    @SerializedName("h_bit_date") var hBitDate: String? = null,
    @SerializedName("h_bit_category") var hBitCategory: String? = null,
    @SerializedName("bill_number") var billNumber: String? = null,
    @SerializedName("h_privileges") var hPrivileges: ArrayList<String>,
    @SerializedName("membership_card_number") var membershipCardNumber: String? = null,
    @SerializedName("h_program_id") var hProgramId: Int? = null,
    @SerializedName("folio_number") var folioNumber: String? = null,
    @SerializedName("h_date_of_booking") var hDateOfBooking: String? = null,
    @SerializedName("h_bit_source_generated_id") var hBitSourceGeneratedId: String? = null,
    @SerializedName("h_start_date") var hStartDate: String? = null

)


data class AvailOriginalBit(

    @SerializedName("header") var header: AvailHeader?,
    @SerializedName("lines") var lines: ArrayList<String>,
    @SerializedName("payment_details") var paymentDetails: ArrayList<String>

)

data class Error(

    @SerializedName("code") var code: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("scope") var scope: String? = null

)