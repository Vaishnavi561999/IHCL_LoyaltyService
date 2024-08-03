package com.ihcl.qwikcilver.dto.deactivate

import kotlinx.serialization.Serializable

@Serializable
data class ResCard(
    val responseMessage:String?,
    val responseCode:Int?,
    val cardStatus:String?,
    val cardType:String?
)