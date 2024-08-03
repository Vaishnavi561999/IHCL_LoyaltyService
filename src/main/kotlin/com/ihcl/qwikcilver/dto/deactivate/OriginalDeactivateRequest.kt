package com.ihcl.qwikcilver.dto.deactivate

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalDeactivateRequest (
    val CardNumber:String,
    val CardPin:String
)

val validateDeactivateRequest = Validation<OriginalDeactivateRequest>{
    OriginalDeactivateRequest::CardNumber required{
        pattern("^[0-9]{16}$")
    }
    OriginalDeactivateRequest::CardPin required{
        pattern("^[0-9]{6}$")
    }
}