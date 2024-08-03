package com.ihcl.qwikcilver.dto.preauth

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalPreauthRequest (
    val cardNumber:String?,
    val cardPin:String?,
    val amount:String?
)

val validatePreauthRequest = Validation<OriginalPreauthRequest>{
    OriginalPreauthRequest::cardNumber required{
        pattern("^[0-9]{16}$")
    }
    OriginalPreauthRequest::cardPin required{
        pattern("^[0-9]{6}$")
    }
    OriginalPreauthRequest::amount required {}
}