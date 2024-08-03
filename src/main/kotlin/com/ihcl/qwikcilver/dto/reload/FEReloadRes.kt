package com.ihcl.qwikcilver.dto.reload

import kotlinx.serialization.Serializable

@Serializable
data class FEReloadRes(
    val transactionId:Int?,
    val responseMessage:String?,
    val responseCode:Int?,
    val Cards:List<ResCard?>?
)