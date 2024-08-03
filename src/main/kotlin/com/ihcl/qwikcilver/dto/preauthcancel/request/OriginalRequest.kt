package com.ihcl.qwikcilver.dto.preauthcancel.request

import kotlinx.serialization.Serializable

@Serializable
data class OriginalRequest(
    val OriginalAmount: String?,
    val OriginalApprovalCode: String?,
    val OriginalBatchNumber: String?,
    val OriginalTransactionId: String?
)