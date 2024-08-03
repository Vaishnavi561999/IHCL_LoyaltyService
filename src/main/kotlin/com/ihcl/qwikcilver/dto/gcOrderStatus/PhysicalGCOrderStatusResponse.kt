package com.ihcl.qwikcilver.dto.gcOrderStatus

import com.google.gson.annotations.SerializedName

data class PhysicalGCOrderStatusResponse(
    @SerializedName("orderId"                  ) var orderId: String?                   = null,
    @SerializedName("refno"                    ) var refno: String?                   = null,
    @SerializedName("status"                   ) var status: String?                   = null,
    @SerializedName("statusLabel"              ) var statusLabel: String?                   = null,
    @SerializedName("createdBy"                ) var createdBy: String?                   = null,
    @SerializedName("date"                     ) var date: String?                   = null,
    @SerializedName("scheduledDate"            ) var scheduledDate: String?                   = null,
    @SerializedName("extCustomerId"            ) var extCustomerId: String?                   = null,
    @SerializedName("grandTotal"               ) var grandTotal: String?                   = null,
    @SerializedName("subTotal"                 ) var subTotal: String?                   = null,
    @SerializedName("discount"                 ) var discount: Int?                      = null,
    @SerializedName("conversionRate"           ) var conversionRate: String?                   = null,
    @SerializedName("baseGrandTotal"           ) var baseGrandTotal: String?                   = null,
    @SerializedName("baseSubTotal"             ) var baseSubTotal: String?                   = null,
    @SerializedName("baseCurrency"             ) var baseCurrency: BaseCurrency?,
    @SerializedName("currencyConversionCharge" ) var currencyConversionCharge: CurrencyConversionCharge?,
    @SerializedName("packaging"                ) var packaging: Packaging?,
    @SerializedName("corporateDiscount"        ) var corporateDiscount: CorporateDiscount?,
    @SerializedName("totalQty"                 ) var totalQty: Int?                      = null,
    @SerializedName("handlingCharges"          ) var handlingCharges: HandlingCharges?,
    @SerializedName("convenienceCharge"        ) var convenienceCharge: Int?                      = null,
    @SerializedName("tax"                      ) var tax: Tax?,
    @SerializedName("orderTypeCode"            ) var orderTypeCode: String?                   = null,
    @SerializedName("products"                 ) var products: ArrayList<Products>,
    @SerializedName("currency"                 ) var currency: Currency?,
    @SerializedName("address"                  ) var address: Address?,
    @SerializedName("billing"                  ) var billing: Billing?,
    @SerializedName("etaMessage"               ) var etaMessage: String?                   = null,
    @SerializedName("shipments"                ) var shipments: ArrayList<Shipments?>? =null,
    @SerializedName("shipping"                 ) var shipping: Shipping?,
    @SerializedName("payments"                 ) var payments: ArrayList<Payments>,
    @SerializedName("orderType"                ) var orderType: String?                   = null,
    @SerializedName("fullFilledBySeller"       ) var fullFilledBySeller: Boolean?                  = null,
    @SerializedName("consolidatedEmailStatus"  ) var consolidatedEmailStatus: String?                   = null,
    @SerializedName("cardTypes"                ) var cardTypes: ArrayList<String>         = arrayListOf(),
    @SerializedName("isMreOrder"               ) var isMreOrder: Boolean?                  = null,
    @SerializedName("cancel"                   ) var cancel: Cancel?,
    @SerializedName("bizApprove"               ) var bizApprove: BizApprove?,
    @SerializedName("additionalTxnFields"      ) var additionalTxnFields: AdditionalTxnFields?,
    @SerializedName("delivery"                 ) var delivery: Delivery?,
    @SerializedName("cards"                    ) var cards: Cards?,
    @SerializedName("orderHistory"             ) var orderHistory: ArrayList<OrderHistory>,
    @SerializedName("extensionParams"          ) var extensionParams: ArrayList<String>,
    @SerializedName("orderReceipt"             ) var orderReceipt: String?                   = null
)
data class Shipments (

    @SerializedName("tracks" ) var tracks : ArrayList<Tracks?>? =null

)
data class Tracks (

    @SerializedName("label" ) var label : String? = null,
    @SerializedName("awb"   ) var awb   : String? = null,
    @SerializedName("url"   ) var url   : String? = null

)
data class OrderHistory (

    @SerializedName("eventGroup"  ) var eventGroup  : String? = null,
    @SerializedName("eventStatus" ) var eventStatus : String? = null,
    @SerializedName("label"       ) var label       : String? = null

)
data class Cards (

    @SerializedName("summary" ) var summary : CardsSummary?

)
data class CardsSummary (

    @SerializedName("success"         ) var success         : Int? = null,
    @SerializedName("inProgress"      ) var inProgress      : Int? = null,
    @SerializedName("failed"          ) var failed          : Int? = null,
    @SerializedName("totalCardsCount" ) var totalCardsCount : Int? = null

)
data class Delivery (

    @SerializedName("summary" ) var summary : Summary?

)
data class Summary (

    @SerializedName("email"           ) var email           : Email? ,
    @SerializedName("sms"             ) var sms             : Sms? ,
    @SerializedName("totalCardsCount" ) var totalCardsCount : Int?   = null

)

data class Sms (

    @SerializedName("totalCount" ) var totalCount : Int? = null,
    @SerializedName("delivered"  ) var delivered  : Int? = null,
    @SerializedName("failed"     ) var failed     : Int? = null,
    @SerializedName("inProgress" ) var inProgress : Int? = null

)

data class BaseCurrency (

    @SerializedName("code"        ) var code        : String? = null,
    @SerializedName("numericCode" ) var numericCode : String? = null,
    @SerializedName("symbol"      ) var symbol      : String? = null

)


data class CurrencyConversionCharge (

    @SerializedName("amount" ) var amount : Int?    = null,
    @SerializedName("label"  ) var label  : String? = null

)
 class Packaging

data class CorporateDiscount (

    @SerializedName("label"      ) var label      : String? = null,
    @SerializedName("amount"     ) var amount     : String? = null,
    @SerializedName("percentage" ) var percentage : String? = null

)

data class HandlingCharges (

    @SerializedName("label"  ) var label  : String? = null,
    @SerializedName("amount" ) var amount : String? = null

)
data class Tax (

    @SerializedName("amount" ) var amount : String? = null,
    @SerializedName("label"  ) var label  : String? = null

)
data class Image (

    @SerializedName("thumbnail" ) var thumbnail : String? = null,
    @SerializedName("mobile"    ) var mobile    : String? = null,
    @SerializedName("base"      ) var base      : String? = null,
    @SerializedName("small"     ) var small     : String? = null

)
data class Currency (

    @SerializedName("code"        ) var code        : String? = null,
    @SerializedName("numericCode" ) var numericCode : String? = null,
    @SerializedName("symbol"      ) var symbol      : String? = null

)

data class Products (

    @SerializedName("name"              ) var name              : String?            = null,
    @SerializedName("type"              ) var type              : String?            = null,
    @SerializedName("qty"               ) var qty               : Int?               = null,
    @SerializedName("price"             ) var price             : String?            = null,
    @SerializedName("total"             ) var total             : String?            = null,
    @SerializedName("discount"          ) var discount          : String?            = null,
    @SerializedName("corporateDiscount" ) var corporateDiscount : CorporateDiscount? ,
    @SerializedName("image"             ) var image             : Image?             = Image(),
    @SerializedName("currency"          ) var currency          : Currency?          = Currency(),
    @SerializedName("mergedQty"         ) var mergedQty         : Int?               = null

)
data class Address (

    @SerializedName("salutation" ) var salutation : String? = null,
    @SerializedName("name"       ) var name       : String? = null,
    @SerializedName("line1"      ) var line1      : String? = null,
    @SerializedName("line2"      ) var line2      : String? = null,
    @SerializedName("line3"      ) var line3      : String? = null,
    @SerializedName("line4"      ) var line4      : String? = null,
    @SerializedName("city"       ) var city       : String? = null,
    @SerializedName("region"     ) var region     : String? = null,
    @SerializedName("postcode"   ) var postcode   : String? = null,
    @SerializedName("country"    ) var country    : String? = null,
    @SerializedName("telephone"  ) var telephone  : String? = null,
    @SerializedName("email"      ) var email      : String? = null,
    @SerializedName("gstn"       ) var gstn       : String? = null,
    @SerializedName("company"    ) var company    : String? = null

)
data class Billing (

    @SerializedName("salutation" ) var salutation : String? = null,
    @SerializedName("name"       ) var name       : String? = null,
    @SerializedName("line1"      ) var line1      : String? = null,
    @SerializedName("line2"      ) var line2      : String? = null,
    @SerializedName("line3"      ) var line3      : String? = null,
    @SerializedName("line4"      ) var line4      : String? = null,
    @SerializedName("city"       ) var city       : String? = null,
    @SerializedName("region"     ) var region     : String? = null,
    @SerializedName("postcode"   ) var postcode   : String? = null,
    @SerializedName("country"    ) var country    : String? = null,
    @SerializedName("telephone"  ) var telephone  : String? = null,
    @SerializedName("email"      ) var email      : String? = null,
    @SerializedName("gstn"       ) var gstn       : String? = null,
    @SerializedName("company"    ) var company    : String? = null

)

data class Method (

    @SerializedName("code"   ) var code   : String? = null,
    @SerializedName("label"  ) var label  : String? = null,
    @SerializedName("amount" ) var amount : Int?    = null,
    @SerializedName("eta"    ) var eta    : String? = null

)
data class Shipping (

    @SerializedName("method" ) var method : Method?

)

data class Payments (

    @SerializedName("code"     ) var code     : String? = null,
    @SerializedName("name"     ) var name     : String? = null,
    @SerializedName("amount"   ) var amount   : String? = null,
    @SerializedName("tds"      ) var tds      : String? = null,
    @SerializedName("poNumber" ) var poNumber : String? = null

)
data class Cancel (

    @SerializedName("allowed" ) var allowed : Boolean? = null

)
data class BizApprove (

    @SerializedName("status"     ) var status     : Int?    = null,
    @SerializedName("actionDate" ) var actionDate : String? = null,
    @SerializedName("by"         ) var by         : String? = null,
    @SerializedName("comment"    ) var comment    : String? = null

)
data class AdditionalTxnFields (

    @SerializedName("remarks" ) var remarks : String? = null

)
data class Email (

    @SerializedName("totalCount" ) var totalCount : Int? = null,
    @SerializedName("delivered"  ) var delivered  : Int? = null,
    @SerializedName("failed"     ) var failed     : Int? = null,
    @SerializedName("inProgress" ) var inProgress : Int? = null

)



data class PhysicalGiftCardOrderStatus(
    val orderId:String,
    val trackingNumber:String,
    val orderStatus:String,
    val orderStatusIdentifier:String,
    val orderTrackingURL:String?,
    val dueDate:String,
    val cardType:String,
)
data class OrderStatusError(
    val code:String,
    val message:String,
)
data class GcOrderErrorStatus(
    val code:String,
    val message:String,
    val messages:List<Any>,
)