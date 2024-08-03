package com.ihcl.qwikcilver.dto.loyaltygravty.request

import com.google.gson.annotations.SerializedName

data class AddOnCard(
    @SerializedName("member_id"            ) var memberId           : String? = null,
    @SerializedName("membership_plan_id"   ) var membershipPlanId   : String? = null,
    @SerializedName("card_start_date"      ) var cardStartDate      : String? = null,
    @SerializedName("card_expiry_date"     ) var cardExpiryDate     : String? = null,
    @SerializedName("card_status"          ) var cardStatus         : String? = null,
    @SerializedName("name_on_card"         ) var nameOnCard         : String? = null,
    @SerializedName("relationship_type"    ) var relationshipType   : String? = null,
    @SerializedName("spouse_name"          ) var spouseName         : String? = null,
    @SerializedName("spouse_gender"        ) var spouseGender       : String? = null,
    @SerializedName("spouse_date_of_birth" ) var spouseDateOfBirth  : String? = null,
    @SerializedName("spouse_email_address" ) var spouseEmailAddress : String? = null,
    @SerializedName("membership_plan_code" ) var membershipPlanCode : String? = null,
    @SerializedName("spouse_phone_number"  ) var spousePhoneNumber  : String? = null,
    @SerializedName("receipt_number"       ) var receiptNumber      : String? = null,
    @SerializedName("card_type"            ) var cardType           : String? = null,
    @SerializedName("dispatched_date"      ) var dispatchedDate     : String? = null,
    @SerializedName("fulfilment_status"    ) var fulfilmentStatus   : String? = null,

)
