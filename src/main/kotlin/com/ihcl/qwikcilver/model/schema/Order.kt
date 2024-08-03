package com.ihcl.qwikcilver.model.schema


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.serialization.Serializable
import java.util.Date

@JsonIgnoreProperties(ignoreUnknown = true)
data class Order(
    val orderId: String,
    val customerHash: String,
    val customerEmail : String,
    val customerId: String,
    val customerMobile: Long,
    val channel: String,
    val currencyCode: String,
    val discountAmount: Double,
    val basePrice: Double?,
    val taxAmount: Double?,
    val gradTotal: Double,
    var payableAmount: Double,
    val isRefundable: Boolean,
    val orderType: OrderType,
    val transactionId: String?,
    val billingAddress: BillingAddress,
    val offers: List<Offers>,
    val orderLineItems: MutableList<OrderLineItem>,
    var modifyBookingCount: Int,
    var paymentDetails: TransactionInfo,
    val paymentMethod: String,
    var paymentStatus: String,
    var orderStatus: String,
    var transactionType: String?,
    val refundAmount: Double,
    val createdTimestamp: Date = Date(),
    var modifiedTimestamp: Date = Date(),
    val agreedTnc: Boolean? = null,
    val agreedPrivacyPolicy: Boolean? = null,
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderLineItem(
    val hotel: Hotel?,
    val giftCard: GiftCard?,
    val loyalty:Loyalty?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Hotel(
    val addOnDetails: List<AddOnDetail>,
    val address: Address,
    var bookingNumber: String,
    val category: String,
    val hotelId: String?,
    val invoiceNumber: String,
    val invoiceUrl: String,
    val name: String?,
    val reservationId: String,
    val roomCount:Int,
    val adultCount:Int,
    val childrens:Int,
    val rooms: List<Room>?,
    var status: String,
    val voucherRedemption: VoucherRedemptionAvailPrivileges?,
    val promoCode:String?,
    val checkIn: String,
    val checkOut: String,
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class PaymentDetail(
    var paymentType: String?,
    var paymentMethod: String?,
    var paymentMethodType: String?,
    var txnGateway: Int?,
    var txnId: String?,
    var txnNetAmount: Double?,
    var txnStatus: String?,
    var txnUUID: String?,
    var cardNo: String?,
    var nameOnCard: String?,
    var userId: String?,
    var redemptionId: String?,
    var pointsRedemptionsSummaryId: String?,
    var externalId: String?,
    var cardNumber: String?,
    val cardPin: String?,
    var preAuthCode: String?,
    var batchNumber: String?,
    var approvalCode: String?,
    var transactionId: Int?,
    var transactionDateAndTime: String?,
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class Offers(
    val offerAmount: Double,
    val offerName: String,
    val offerType: String
)
@Serializable
data class Loyalty(
    val memberCardDetails: MemberCardDetails,
    val membershipDetails: MembershipDetails
)
@Serializable
data class MemberCardDetails(
    val enrolling_location: String,
    val enrolling_sponsor: Int,
    val enrollment_channel: String,
    val enrollment_touchpoint: Int,
    val extra_data: ExtraData,
    val epicure_price:Double,
    val taxAmount: Double
)
@Serializable
data class ExtraData(
    val country_code: String,
    val domicile: String,
    val epicure_type: String,
    val state: String,
    val country:String,
    val gstNumber:String
)
@Serializable
data class MembershipDetails(
    val memberId: String,
    val mobile: String,
    val user: User
)
@Serializable
data class User(
    val email: String,
    val first_name: String,
    val last_name: String,
    val gender:String?,
    val salutation:String,
    val date_of_birth:String,
    val address: String,
    val pincode:String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class TransactionInfo(
    var transaction_1: MutableList<PaymentDetail>?,
    var transaction_2: MutableList<PaymentDetail>?,
    var transaction_3: MutableList<PaymentDetail>?,
    var transaction_4: MutableList<PaymentDetail>?
)

enum class OrderType {
    HOTEL_BOOKING, GIFT_CARD_PURCHASE, RESTAURANTS, SPA, RELOAD_BALANCE, MEMBERSHIP_PURCHASE
}

@Serializable
data class AddOnDetail(
    val addOnCode: String,
    val addOnDesc: String,
    val addOnName: String,
    val addOnPrice: Double,
    val addOnType: String
)
@Serializable
data class Address(
    val city: String,
    val contactNumber: String,
    val directions: String,
    val landmark: String,
    val lat: String,
    val long: String,
    val mapLink: String,
    val pinCode: String,
    val state: String,
    val street: String
)
@Serializable
data class BillingAddress(
    val address1: String,
    val address2: String,
    val address3: String,
    val city: String,
    val country: String,
    val firstName: String,
    val lastName: String,
    val pinCode: String,
    val state: String,
    val phoneNumber: String,
    val countyCodeISO: String
)
@Serializable
data class GiftCard(
    val deliveryMethods: DeliveryMethodsDto,
    val quantity: Int,
    val isMySelf: Boolean?,
    val giftCardDetails: List<GiftCardDetailsDto>?,
    val promoCode: String?,
    val receiverAddress: ReceiverAddressDto?,
    val receiverDetails: ReceiverDetailsDto?,
    val senderDetails: SenderDetailsDto?
)
@Serializable
data class GiftCardDetailsDto(
    var amount: Double?,
    val sku: String?,
    val type: String?,
    val theme: String?,
    var cardNumber: String?,
    var cardPin: String?,
    var cardId: String?,
    var validity: String?,
    var orderId: String?
)
@Serializable
data class ReceiverAddressDto(
    val addressLine1: String,
    val addressLine2: String,
    val city: String,
    val country: String,
    val pinCode: String,
    val state: String
)
@Serializable
data class ReceiverDetailsDto(
    val email: String,
    val firstName: String,
    val lastName: String,
    val message: String,
    val phone: String,
    val rememberMe: Boolean,
    val scheduleOn: String
)
@Serializable
data class SenderDetailsDto(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val registerAsNeuPass: Boolean
)
@Serializable
data class DeliveryMethodsDto(
    val phone: String,
    val sms: Boolean,
    val whatsApp: Boolean
)
@Serializable
data class Tax(
    var amount: Double?,
    val breakDown: List<BreakDown>?
)

@Serializable
data class BreakDown(
    val amount: Double?,
    val code: String?
)
@Serializable
data class Room(
    val isPackage: Boolean?,
    var confirmationId: String,
    var cancellationId: String?,
    var status : String?,
    val addOnDetails: List<AddOnDetail>,
    val checkIn: String,
    val checkOut: String,
    val tax: Tax?,
    val discountAmount: Double,
    val discountCode: String,
    val isModified: Boolean,
    val isRefundedItem: Boolean,
    val modifiedWith: String,
    val price: Double,
    val rateDescription: String,
    val refundedAmount: String,
    val roomDescription: String,
    val roomId: String,
    val roomName: String,
    val roomNumber: Int,
    val roomType: String,
    val rateCode: String?,
    val packageCode:String?,
    val adult:Int?,
    val children:Int?,
    val packageName: String?,
    val travellerDetails: List<TravellerDetail>?,
    val roomImgUrl: String?,
    val changePrice:Double?,
    val changeTax:Double?,
    val modifyBooking: ModifyBooking?,
    val bookingPolicyDescription: String?,
    val cancelPolicyDescription: String?,
    var penaltyAmount: Double?,
    var penaltyDeadLine: String?,
    var cancellationTime: String?,
    var penaltyApplicable: Boolean?,
)
@JsonIgnoreProperties(ignoreUnknown = true)
@Serializable
data class ModifyBooking(
    val isPackage: Boolean?,
    var confirmationId: String?,
    var cancellationId: String?,
    var status : String?,
    val addOnDetails: List<AddOnDetail>,
    val checkIn: String,
    val checkOut: String,
    val tax: Tax?,
    val discountAmount: Double,
    val discountCode: String,
    val isModified: Boolean,
    val isRefundedItem: Boolean,
    val modifiedWith: String,
    val price: Double,
    val rateDescription: String,
    val refundedAmount: String,
    val roomDescription: String,
    val roomId: String,
    val roomName: String,
    val roomNumber: Int,
    val roomType: String,
    val rateCode: String?,
    val packageCode:String?,
    val adult:Int?,
    val children:Int?,
    val packageName: String?,
    val travellerDetails: List<TravellerDetail>?,
    val roomImgUrl: String?,
    val bookingPolicyDescription: String?,
    val cancelPolicyDescription: String?
)
@Serializable
data class VoucherRedemptionAvailPrivileges(
    var bitDate: String,
    var memberId: String,
    var privileges: String,
    var pin:String? = null,
    var availBitId: String? = null,
    var status: String? = null,
    var bitCategory: String? = null
)

@Serializable
data class TravellerDetail(
    val dateOfBirth: String,
    val address: String,
    val city: String,
    val countryCode: String,
    val customerId: String,
    val customerType: String,
    val email: String,
    val firstName: String,
    val gender: String,
    val gstNumber: String,
    val lastName: String,
    val membershipNumber: String,
    val mobile: String,
    val name: String,
    val secondaryContact: String,
    val state: String
)
