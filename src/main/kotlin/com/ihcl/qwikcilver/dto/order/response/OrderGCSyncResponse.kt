package com.ihcl.qwikcilver.dto.order.response

import kotlinx.serialization.Serializable

@Serializable
data class OrderGCSyncResponse(
    val additionalTxnFields: List<String?>?,
    val cancel: Cancel?,
    val cards: List<Card?>?,
    val currency: Currency?,
    val orderId: String?,
    val payments: List<Payment?>?,
    val products: Products?,
    val refno: String?,
    val status: String?
)

@Serializable
data class Card(
    val activationCode: String?,
    val activationUrl: String?,
    val amount: String?,
    val barcode: String?,
    val cardId: Int?,
    val cardNumber: String?,
    val cardPin: String?,
    val formats: List<Format?>?,
    val issuanceDate: String?,
    val labels: Labels?,
    val productName: String?,
    val recipientDetails: RecipientDetails?,
    val sku: String?,
    val theme: String?,
    val validity: String?
)

@Serializable
data class Format(
    val key: String?,
    val value: String?
)
@Serializable
data class Labels(
    val activationCode: String?,
    val cardNumber: String?,
    val cardPin: String?,
    val validity: String?
)
@Serializable
data class RecipientDetails(
    val delivery: Delivery?,
    val email: String?,
    val failureReason: String?,
    val firstname: String?,
    val lastname: String?,
    val mobileNumber: String?,
    val name: String?,
    val salutation: String?,
    val status: String?
)
@Serializable
data class Delivery(
    val mode: String?,
    val status: Status?
)
@Serializable
data class Status(
    val email: Email?,
    val sms: Sms?
)
@Serializable
data class Email(
    val reason: String?,
    val status: String?
)
@Serializable
data class Sms(
    val reason: String?,
    val status: String?
)
@Serializable
data class Products(
    val productName: ProductName?
)

@Serializable
data class ProductName(
    val balanceEnquiryInstruction: String?,
    val cardBehaviour: String?,
    val images: Images?,
    val name: String?,
    val sku: String?,
    val specialInstruction: String?
)
@Serializable
data class Images(
    val base: String?,
    val mobile: String?,
    val small: String?,
    val thumbnail: String?
)
