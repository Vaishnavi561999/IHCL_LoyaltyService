package com.ihcl.qwikcilver.dto.preauth

import kotlinx.serialization.Serializable

@Serializable
data class FEPreauthRes(
    val transactionId:Int?,
    val responseMessage:String?,
    val responseCode:Int?,
    val Card:List<ResCard>?,
    val currentBatchNumber:Int?,

)