package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName


data class FetchEpicureMemberShip (

    @SerializedName("path"      ) var path      : String?            = null,
    @SerializedName("timestamp" ) var timestamp : String?            = null,
    @SerializedName("status"    ) var status    : Status?      = null,
    @SerializedName("errorCode"    ) var errorCode    : String? =null,
    @SerializedName("errorReason"    ) var errorReason     : String? =null,
    @SerializedName("errorMessage"    ) var errorMessage    : String? =null,
    @SerializedName("message"   ) var message   : ArrayList<Message> = arrayListOf(),


)
data class Status (

    @SerializedName("code"           ) var code           : Int?    = null,
    @SerializedName("displayMessage" ) var displayMessage : String? = null

)
data class Message (

    @SerializedName("_id"            ) var Id             : String? = null,
    @SerializedName("customerHash"   ) var customerHash   : String? = null,
    @SerializedName("custId"         ) var custId         : String? = null,
    @SerializedName("programId"      ) var programId      : String? = null,
    @SerializedName("action"         ) var action         : String? = null,
    @SerializedName("status"         ) var status         : String? = null,
    @SerializedName("source"         ) var source         : String? = null,
    @SerializedName("planId"         ) var planId         : String? = null,
    @SerializedName("subscriptionId" ) var subscriptionId : String? = null,
    @SerializedName("brandPlanId"    ) var brandPlanId    : String? = null,
    @SerializedName("brandMembershipId"    ) var brandMembershipId    : String? = null,
    @SerializedName("brandPlanName"  ) var brandPlanName  : String? = null,
    @SerializedName("displayName"    ) var displayName    : String? = null,
    @SerializedName("purchaseType"   ) var purchaseType   : String? = null,
    @SerializedName("startDate"      ) var startDate      : String? = null,
    @SerializedName("endDate"        ) var endDate        : String? = null,
    @SerializedName("createdAt"      ) var createdAt      : String? = null,
    @SerializedName("modifiedAt"     ) var modifiedAt     : String? = null

)

data class FetchMemberShipErrorDTO (

    @SerializedName("errorCode"    ) var errorCode    : String? = null,
    @SerializedName("errorReason"  ) var errorReason  : String? = null,
    @SerializedName("errorMessage" ) var errorMessage : String? = null

)