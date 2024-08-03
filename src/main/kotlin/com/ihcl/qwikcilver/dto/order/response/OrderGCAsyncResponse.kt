package com.ihcl.qwikcilver.dto.order.response

import kotlinx.serialization.Serializable

@Serializable
data class OrderGCAsyncResponse(
    val cancel: Cancel?,
    val currency: Currency?,
    val orderId: String?,
    val payments: List<Payment?>?,
    val refno: String?,
    val status: String?
)
@Serializable
data class Cancel(
    val allowed: Boolean?,
    val allowedWithIn: Int?
)
@Serializable
data class Currency(
    val code: String?,
    val numericCode: String?,
    val symbol: String?
)
@Serializable
data class Payment(
    val balance: String?,
    val code: String?
)
@Serializable
data class BadRequestResponse(
    val status: String?,
    val code:Int,
    val message:String?,
    val messages:List<String?>,
    val additionalTxnFields: List<String?>?,
)