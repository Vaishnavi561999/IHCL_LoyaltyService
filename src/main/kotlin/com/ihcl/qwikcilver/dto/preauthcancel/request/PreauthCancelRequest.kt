package com.ihcl.qwikcilver.dto.preauthcancel.request

import kotlinx.serialization.Serializable

@Serializable
data class PreauthCancelRequest(
    val Cards: List<Card?>?,
    val InputType: String?,
    val Notes: String?,
    val TransactionModeId: Int?
)