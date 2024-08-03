package com.ihcl.qwikcilver.dto.preauthcancel

import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class OriginalPreauthCancelRequest(
    val CardNumber: String?,
    val OriginalRequest: OriginalRequest?
)
const val NUMERIC_PATTERN = "^[0-9]+$"
val validatePreauthCancelRequest = Validation<OriginalPreauthCancelRequest>{
    OriginalPreauthCancelRequest::CardNumber required{
        pattern("^[0-9]{16}$")
    }
    OriginalPreauthCancelRequest::OriginalRequest required{
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