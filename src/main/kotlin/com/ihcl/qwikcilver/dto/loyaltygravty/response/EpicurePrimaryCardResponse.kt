package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName
import io.ktor.http.*

data class EpicurePrimaryCardResponse (
    @SerializedName("data" ) var data : PrimaryCardData?
)
data class PrimaryCardData (

    @SerializedName("id"                   ) var id                 : Int?    = null,
    @SerializedName("card_status"          ) var cardStatus         : String? = null,
    @SerializedName("name_on_card"         ) var nameOnCard         : String? = null,
    @SerializedName("relationship_type"    ) var relationshipType   : String? = null,
    @SerializedName("receipt_number"       ) var receiptNumber      : String? = null,
    @SerializedName("air_way_bill_number"  ) var airWayBillNumber   : String? = null,
    @SerializedName("dispatched_date"      ) var dispatchedDate     : String? = null,
    @SerializedName("fulfilment_status"    ) var fulfilmentStatus   : String? = null,
    @SerializedName("spouse_phone_number"  ) var spousePhoneNumber  : String? = null,
    @SerializedName("updated_ts"           ) var updatedTs          : String? = null,
    @SerializedName("spouse_email_address" ) var spouseEmailAddress : String? = null,
    @SerializedName("card_expiry_date"     ) var cardExpiryDate     : String? = null,
    @SerializedName("created_ts"           ) var createdTs          : String? = null,
    @SerializedName("spouse_name"          ) var spouseName         : String? = null,
    @SerializedName("spouse_date_of_birth" ) var spouseDateOfBirth  : String? = null,
    @SerializedName("card_type"            ) var cardType           : String? = null,
    @SerializedName("spouse_gender"        ) var spouseGender       : String? = null,
    @SerializedName("card_start_date"      ) var cardStartDate      : String? = null,
    @SerializedName("card_number"          ) var cardNumber         : String? = null,
    @SerializedName("membership_plan_code" ) var membershipPlanCode : String? = null,
    @SerializedName("member_id"            ) var memberId           : String? = null,
    @SerializedName("membership_plan_id"   ) var membershipPlanId   : String? = null

)
data class ApplicationResponse(
    val statusCode: HttpStatusCode,
    val body: Any
)