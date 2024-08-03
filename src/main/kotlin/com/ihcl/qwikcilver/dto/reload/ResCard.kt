package com.ihcl.qwikcilver.dto.reload

import kotlinx.serialization.Serializable

@Serializable
data class ResCard(
    val responseCode:Int?,
    val balance:Double?,
    val cardNumber:String?,
    val expiryDate:String?,
    var responseMessage:String?

)