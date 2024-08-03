package com.ihcl.qwikcilver.dto.redeem

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalRedeemRequest (
    val billAmount:Double,
    val idempotencyKey:String,
    val propertyName:String,
    val cardNumber:String,
    val cardPin:String,
    val amount:Double,
    val invoiceNumber:String

)

val validateRedeemRequest = Validation<OriginalRedeemRequest>{
    OriginalRedeemRequest::cardNumber required{
        pattern("^[0-9]{16}$")
    }
    OriginalRedeemRequest::cardPin required{
        pattern("^[0-9]{6}")
    }
    OriginalRedeemRequest::amount required{}
}