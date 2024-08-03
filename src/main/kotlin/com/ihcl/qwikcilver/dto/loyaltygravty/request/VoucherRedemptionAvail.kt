package com.ihcl.qwikcilver.dto.loyaltygravty.request

import com.google.gson.annotations.SerializedName

data class VoucherRedemptionAvailPrivileges(
    @SerializedName("h_bit_date")
    var hBitDate: String,
    @SerializedName("h_member_id")
    var hMemberId: String,
    @SerializedName("h_privileges")
    var hPrivileges: String,
    @SerializedName("pin")
    var pin:String,
    var type:String,
    @SerializedName("hotelSponsorId")
    var hotelSponsorId:String
)

data class PinEnabledVoucherRedemption(
    @SerializedName("h_sponsor_id")
    var hSponsorId: Int,
    @SerializedName("h_bit_date")
    var hBitDate: String,
    @SerializedName("h_program_id")
    var hProgramId:Int,
    @SerializedName("h_member_id")
    var hMemberId: String,
    @SerializedName("h_bit_category")
    var hBitCategory: String,
    @SerializedName("h_privileges_with_pin")
    var hPrivilegesWithPin: List<PinEnabled>
)
data class PinEnabled(
    @SerializedName("h_privilege")
    var hPrivileges: String,
    @SerializedName("pin")
    var pin:String,
)
data class PinDisableVoucherRedemption(
    @SerializedName("h_sponsor_id")
    var hSponsorId: Int,
    @SerializedName("h_bit_date")
    var hBitDate: String,
    @SerializedName("h_program_id")
    var hProgramId:Int,
    @SerializedName("h_member_id")
    var hMemberId: String,
    @SerializedName("h_bit_category")
    var hBitCategory: String,
    @SerializedName("h_privileges")
    var hPrivileges: List<String>
)
data class ChamberVoucherAvailResponse(
    val original_bit:OriginalBitVoucherDetails,
    val availed_privileges:List<VoucherRedeemDetails>
)
data class EpicureVoucherAvailResponse(
    val original_bit:OriginalBitVoucherDetails,
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
data class Response(
    val type:String,
    val data:EpicureVoucherAvailResponse
)
data class OriginalBitVoucherDetails(
    val header:Header
)
data class Header(
    val h_bit_date:String
)




