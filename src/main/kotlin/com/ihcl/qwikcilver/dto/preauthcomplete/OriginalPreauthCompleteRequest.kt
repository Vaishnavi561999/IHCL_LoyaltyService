package com.ihcl.qwikcilver.dto.preauthcomplete

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalPreauthCompleteRequest(
    val Amount: String?,
    val CardNumber: String?,
    val CurrencyCode: String?,
    val PreAuthCode: String?
)

val validatePreauthCompleteRequest = Validation<OriginalPreauthCompleteRequest>{
    OriginalPreauthCompleteRequest::CardNumber required{
        pattern("^[0-9]{16}$")
    }
    OriginalPreauthCompleteRequest::CurrencyCode required{
        pattern("^[A-Z]{3}$")
    }
    OriginalPreauthCompleteRequest::PreAuthCode required {
        pattern("^[0-9]{10}$")
    }
    OriginalPreauthCompleteRequest::Amount required {}
}