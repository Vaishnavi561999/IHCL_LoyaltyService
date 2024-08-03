package com.ihcl.qwikcilver.dto.preauthcomplete.request

import kotlinx.serialization.Serializable

@Serializable
data class PreauthCompleteRequest(
    val Cards: List<Card?>?,
    val InputType: String?,
    val Notes: String?,
    val PreAuthType: Int?,
    val TransactionTypeId: Int?
)