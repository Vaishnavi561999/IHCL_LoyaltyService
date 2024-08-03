package com.ihcl.qwikcilver.dto.activategc

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ActivateGCResponse(
    val cards: List<Card?>?,
    val currency: Currency?,
    val delivery: Delivery?,
    val deliveryMode: String?,
    val products: Products?,
    @SerializedName("total_cards")
    val totalCards: Int?
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
    val cardView: CardView?,
    val formats: List<Formats?>?,
    val issuanceDate: String?,
    val labels: Labels?,
    val productName: String?,
    val recipientDetails: RecipientDetails?,
    val sku: String?,
    val theme: String?,
    val validity: String?
)
@Serializable
data class Formats(
    val key:String?,
    val value:String?
)
@Serializable
data class Currency(
    val code: String?,
    val numericCode: String?,
    val symbol: String?
)
@Serializable
data class Delivery(
    val summary: Summary?
)
@Serializable
data class CardView(
    val identifier: String?
)
@Serializable
data class RecipientDeliveryDetails(
    val mode: String?,
    val status: Status?
)
@Serializable
data class StatusEmail(
    val reason: String?,
    val status: String?
)
@Serializable
data class Email(
    val delivered: Int?,
    val failed: Int?,
    val inProgress: Int?,
    val totalCount: Int?
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
@Serializable
data class Labels(
    val activationCode: String?,
    val cardNumber: String?,
    val cardPin: String?,
    val validity: String?
)
@Serializable
data class Products(
    val productName: ProductName?
)
@Serializable
data class RecipientDetails(
    val delivery: RecipientDeliveryDetails?,
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
data class StatusSms(
    val reason: String?,
    val status: String?
)
@Serializable
data class Sms(
    val delivered: Int?,
    val failed: Int?,
    val inProgress: Int?,
    val totalCount: Int?
)
@Serializable
data class Status(
    val email: StatusEmail?,
    val sms: StatusSms?
)
@Serializable
data class Summary(
    val delivered: Int?,
    val email: Email?,
    val failed: Int?,
    val inProgress: Int?,
    val sent: Int?,
    val sms: Sms?,
    val totalCardsCount: Int?
)