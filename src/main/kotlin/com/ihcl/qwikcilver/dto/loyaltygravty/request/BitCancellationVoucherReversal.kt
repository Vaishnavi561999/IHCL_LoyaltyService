package com.ihcl.qwikcilver.dto.loyaltygravty.request

import com.google.gson.annotations.SerializedName

data class BitCancellationVoucherReversal(
    @SerializedName("h_bit_date")
    var hBitDate: String,
    @SerializedName("cancel_bit_id")
    var cancelBitId: String,
    @SerializedName("h_member_id")
    var hMemberId: String,
    var type:String,
    @SerializedName("hotelSponsorId")
    var hotelSponsorId:String
)
data class BitCancellationRequest(
    @SerializedName("h_bit_date")
    var hBitDate: String,
    @SerializedName("h_sponsor_id")
    var hSponsorId :Int,
    @SerializedName("h_bit_category")
    var hBitCategory: String,
    @SerializedName("h_bit_type")
    var hBitType: String,
    @SerializedName("cancel_bit_id")
    var cancelBitId: String,
    @SerializedName("h_program_id")
    var hProgramId :Int,
    @SerializedName("h_member_id")
    var hMemberId: String
)