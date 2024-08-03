package com.ihcl.qwikcilver.dto.preauthreversal.response

import kotlinx.serialization.Serializable

@Serializable
data class PreauthReversalResponse(
    val ApprovalCode: String?,
    val BusinessReferenceNumber: String?,
    val Cards: List<Card?>?,
    val CostCentre: String?,
    val CurrentBatchNumber: Int?,
    val ErrorCode: String?,
    val ErrorDescription: String?,
    val ExecutionMode: Int?,
    val GeneralLedger: String?,
    val IdempotencyKey: String?,
    val InputType: String?,
    val Notes: String?,
    val NumberOfCards: Int?,
    val ResponseCode: String?,
    val ResponseMessage: String?,
    val TotalAmount: Double?,
    val TotalCards: Int?,
    val TransactionId: Int?,
    val TransactionTypeId: Int?
)