package com.ihcl.qwikcilver.dto.reload.request

import kotlinx.serialization.Serializable

@Serializable
data class ReloadRequest(
    val Cards: List<Card?>?,
    val InputType: String?,
    val NumberOfCards: String?,
    val TransactionTypeId: Int?,
    val invoiceNumber: String,
    val idempotencyKey:String?
)