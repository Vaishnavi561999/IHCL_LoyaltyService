package com.ihcl.qwikcilver.dto.redeem

import kotlinx.serialization.Serializable

@Serializable
data class FERedeemResponse (
    val transactionId:Int?,
    val responseMessage:String?,
    val responseCode:Int?,
    val Cards:List<ResCard?>?,
    val CurrentBatchNumber:String,
    val ApprovalCode:String
)