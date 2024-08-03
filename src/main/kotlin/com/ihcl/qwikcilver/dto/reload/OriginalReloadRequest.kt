package com.ihcl.qwikcilver.dto.reload

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalReloadRequest(
    val cardNumber:String,
    val amount:Double,
    val invoiceNumber:String,
    val idempotencyKey:String
)

val validateReloadRequest = Validation<OriginalReloadRequest>{
    OriginalReloadRequest::cardNumber required{
        pattern("^[0-9]{16}$")
    }
    OriginalReloadRequest::amount required{}
}