package com.ihcl.qwikcilver.dto.cancelRedeem

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern

data class FECancelRedeemRequest(
    val Amount: Int?,
    val CardNumber: String?,
    val OriginalApprovalCode: Int?,
    val OriginalBatchNumber: Int?,
    val OriginalTransactionId: Int?,
    val Note:String
)

val validateFECancelRedeemRequest = Validation<FECancelRedeemRequest>{
    FECancelRedeemRequest::CardNumber required {
        pattern("^[0-9]{16}$")
    }
    FECancelRedeemRequest::Amount required {}
    FECancelRedeemRequest::OriginalApprovalCode required {}
    FECancelRedeemRequest::OriginalBatchNumber required {}
    FECancelRedeemRequest::OriginalTransactionId required {}
}