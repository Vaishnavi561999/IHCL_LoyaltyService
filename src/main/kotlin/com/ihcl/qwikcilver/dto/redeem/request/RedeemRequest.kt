package com.ihcl.qwikcilver.dto.redeem.request

import kotlinx.serialization.Serializable

@Serializable
data class RedeemRequest(
    val Cards: List<Card?>?,
    val InputType: String?,
    val TransactionTypeId: Int?,
    val BillAmount:Double,
    val IdempotencyKey:String?,
    val Notes:String?
)