package com.ihcl.qwikcilver.dto.orderstatus

import kotlinx.serialization.Serializable

@Serializable
data class FEOrderStatusRequest(
    val orderNumber: String?,
    val email: String?,
)

@Serializable
data class OrderStatusResponse(
    val status: String?,
    val statusLabel: String?,
    val orderId: String?,
    val refno: String?,
    val cancel: Cancel?,
)

@Serializable
data class Cancel(
    val allowed: String?,
    val allowedWithIn: String?,
)

@Serializable
data class OrderStatusAvailabilityResponse(
    val code:String?,
    var message:String?,
    val messages:List<String?>?
)