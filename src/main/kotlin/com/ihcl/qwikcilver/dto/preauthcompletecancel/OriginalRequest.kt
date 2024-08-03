package com.ihcl.qwikcilver.dto.preauthcompletecancel

import kotlinx.serialization.Serializable

@Serializable
data class OriginalRequest(
    val OriginalAmount: Double?,
    val OriginalApprovalCode: String?,
    val OriginalBatchNumber: String?,
    val OriginalTransactionId: String?
)