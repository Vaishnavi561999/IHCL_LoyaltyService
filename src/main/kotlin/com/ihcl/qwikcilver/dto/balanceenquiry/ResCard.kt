package com.ihcl.qwikcilver.dto.balanceenquiry

import kotlinx.serialization.Serializable

@Serializable
data class ResCard(
    val CardNumber:String?,
    val CardType:String?,
    val TotalReloadedAmount:Double?,
    val TotalRedeemedAmount:Double?,
    val Balance:Int?,
    val ResponseCode:String?,
    var ResponseMessage:String?,
    val ExpiryDate:String?
)