package com.ihcl.qwikcilver.dto.reverseRedeem

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern

data class FEReverseRedeemRequest(
    val Amount: Int?,
    val CardNumber: String?,
    val TransactionId: String?
)

val validateFEReverseRedeemRequest = Validation<FEReverseRedeemRequest>{
    FEReverseRedeemRequest::CardNumber required {
        pattern("^[0-9]{16}$")
    }
    FEReverseRedeemRequest::Amount required {}
    FEReverseRedeemRequest::TransactionId required {}
}