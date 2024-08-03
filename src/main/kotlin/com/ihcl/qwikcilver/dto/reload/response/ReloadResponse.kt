package com.ihcl.qwikcilver.dto.reload.response

import kotlinx.serialization.Serializable

@Serializable
data class ReloadResponse(
    val ApprovalCode: String?,
    val BusinessReferenceNumber: String?,
    val Cards: List<Card>?,
    val CurrentBatchNumber: Int?,
    val ErrorCode: String?,
    val ErrorDescription: String?,
    val IdempotencyKey: String?,
    val InputType: String?,
    val Notes: String?,
    val NumberOfCards: Int?,
    val ResponseCode: String?,
    val ResponseMessage: String?,
    val TotalAmount: Double?,
    val TransactionId: Int?,
    val TransactionTypeId: Int?
)