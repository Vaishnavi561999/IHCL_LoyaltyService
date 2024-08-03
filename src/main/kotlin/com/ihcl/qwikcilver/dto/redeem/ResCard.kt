package com.ihcl.qwikcilver.dto.redeem

import kotlinx.serialization.Serializable
@Serializable
data class ResCard(
    val responseMessage:String?,
    val responseCode:Int?,
    val balance:Double?
)