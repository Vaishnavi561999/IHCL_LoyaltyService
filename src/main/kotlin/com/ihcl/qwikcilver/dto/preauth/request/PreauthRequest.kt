package com.ihcl.qwikcilver.dto.preauth.request

import kotlinx.serialization.Serializable

@Serializable
data class PreauthRequest(
    val Cards: List<Card?>?,
    val InputType: String?,
    val PreAuthType: Int?,
    val TransactionTypeId: Int?
)