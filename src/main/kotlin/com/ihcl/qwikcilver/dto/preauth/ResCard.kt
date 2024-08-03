package com.ihcl.qwikcilver.dto.preauth

import kotlinx.serialization.Serializable

@Serializable
data class ResCard (
    val balance:Double?,
    val preAuthCode:String?,
    val approvalCode:String?,
    val responseMessage:String?,
    val responseCode:Int?,
    val transactionDateAndTime:String?,
    val transactionAmount: String?
)