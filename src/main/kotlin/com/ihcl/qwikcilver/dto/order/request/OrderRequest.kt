package com.ihcl.qwikcilver.dto.order.request

import kotlinx.serialization.Serializable

@Serializable
data class OrderRequest(
    val address: Address?,
    val billing: Billing?,
    val deliveryMode: String?,
    val remarks: String?,
    val payments: List<Payments?>?,
    val products: List<Product?>?,
    val refno: String?,
    val syncOnly: Boolean?

)
@Serializable
data class Payments(
    val amount: String?,
    val code: String?,
    val poNumber:String?
)
@Serializable
data class PhysicalGCOrderRequest(
    val address: Address?,
    val billing: Billing?,
    val payments: List<Payments?>?,
    val products: List<Product?>?,
    val refno: String?,
    val syncOnly: Boolean?,
    val remarks:String?

)