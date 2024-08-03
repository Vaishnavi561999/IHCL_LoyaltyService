package com.ihcl.qwikcilver.dto.preauthcompletecancel.request

import kotlinx.serialization.Serializable

@Serializable
data class PreauthCompleteCancelRequest(
    val Cards: List<Card?>?,
    val InputType: String?,
    val Notes: String?,
    val TransactionModeId: Int?
)