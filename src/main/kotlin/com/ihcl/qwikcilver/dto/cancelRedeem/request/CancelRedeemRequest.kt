package com.ihcl.qwikcilver.dto.cancelRedeem.request

import kotlinx.serialization.Serializable

@Serializable
data class CancelRedeemRequest(
    val Cards: List<RequestCard?>?,
    val InputType: Int?,
    val Notes: String?,
    val transactionModeId: String?
)
@Serializable
data class RequestCard(
    val Amount: Int?,
    val CardNumber: String?,
    val OriginalRequest: OriginalRequest?
)
@Serializable
data class OriginalRequest(
    val OriginalAmount: Int?,
    val OriginalApprovalCode: Int?,
    val OriginalBatchNumber: Int?,
    val OriginalTransactionId: Int?
)
