package com.ihcl.qwikcilver.dto.balanceenquiry

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalBalanceEnquiryRequest(
    val balanceEnquiry: List<BalanceEnquiry>
)
@Serializable
data class BalanceEnquiry(
    val CardNumber:String?,
    val CardPin:String?,
    val type:String?
)

val validateOriginalBalanceEnquiryRequest = Validation {
    OriginalBalanceEnquiryRequest::balanceEnquiry onEach {
        BalanceEnquiry::CardNumber required {
            pattern("^[0-9]{16}$")
        }

    }


}