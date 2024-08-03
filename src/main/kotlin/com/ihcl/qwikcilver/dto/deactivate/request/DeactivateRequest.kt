package com.ihcl.qwikcilver.dto.deactivate.request

import kotlinx.serialization.Serializable

@Serializable
data class DeactivateRequest(
    val Cards: List<Card?>?,
    val inputType: String?,
    val transactionTypeId: Int?
)