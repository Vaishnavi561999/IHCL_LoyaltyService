package com.ihcl.qwikcilver.dto.order.request

import kotlinx.serialization.Serializable

@Serializable
data class FEOrderGCdto(
    val address: Address?,
    val billing: Billing?,
    val deliveryMode: String?,
    val remarks:String?,
    val payments: List<Payment?>?,
    val products: List<Product?>?,
    val refno: String?
)

@Serializable
data class Address(
    val billToThis: Boolean?,
    val city: String?,
    val company: String?,
    val country: String?,
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val line1: String?,
    val line2: String?,
    val postcode: String?,
    val region: String?,
    val telephone: String?
)
@Serializable
data class Billing(
    val city: String?,
    val company: String?,
    val country: String?,
    val email: String?,
    val firstname: String?,
    val lastname: String?,
    val line1: String?,
    val line2: String?,
    val postcode: String?,
    val region: String?,
    val telephone: String?
)
@Serializable
data class Payment(
    val amount: String?,
    val code: String?
)
@Serializable
data class Product(
    val currency: String?,
    val price: Int?,
    val qty: Int?,
    val sku: String?,
    val theme: String?,
    val payout:Payout?,
    val giftMessage:String?
)
@Serializable
data class Payout(
    val id:String?
)