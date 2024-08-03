package com.ihcl.qwikcilver.dto.preauthreversal

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalPreauthReversalRequest(
    val Amount: Double?,
    val CardNumber: String?,
    val TransactionDateTime: String?,
    val TransactionId: String?,
    val InvoiceNumber:String?,
    val InvoiceDate:String?,
    val BillAmount:String?,
)

val validatePreauthReversalRequest = Validation<OriginalPreauthReversalRequest>{
    OriginalPreauthReversalRequest::CardNumber required{
        pattern("^[0-9]{16}$")
    }
    OriginalPreauthReversalRequest::TransactionDateTime required{}
    OriginalPreauthReversalRequest::TransactionId required{}
    OriginalPreauthReversalRequest::Amount required{}
}