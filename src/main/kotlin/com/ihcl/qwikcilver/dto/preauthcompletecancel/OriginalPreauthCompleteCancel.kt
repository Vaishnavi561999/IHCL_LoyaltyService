package com.ihcl.qwikcilver.dto.preauthcompletecancel

import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalPreauthCompleteCancel(
    val CardNumber: String?,
    val OriginalRequest: OriginalRequest?
)
const val NUMERIC_PATTERN = "^[0-9]+$"
val validatePreauthCompleteCancel = Validation<OriginalPreauthCompleteCancel>{
    OriginalPreauthCompleteCancel::CardNumber required{
        pattern("^[0-9]{16}$")
    }
    OriginalPreauthCompleteCancel::OriginalRequest required{
       OriginalRequest::OriginalAmount required {}
       OriginalRequest::OriginalApprovalCode required {
           minLength(1)
           maxLength(12)
           pattern(NUMERIC_PATTERN)
       }
       OriginalRequest::OriginalBatchNumber required {
           minLength(1)
           maxLength(10)
           pattern(NUMERIC_PATTERN)
       }
       OriginalRequest::OriginalTransactionId required {
           minLength(1)
           maxLength(20)
           pattern(NUMERIC_PATTERN)
       }
    }
}