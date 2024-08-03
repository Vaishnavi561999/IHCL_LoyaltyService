package com.ihcl.qwikcilver.dto.preauthcomplete.request

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val Amount: String?,
    val CardNumber: String?,
    val CurrencyCode: String?,
    val PreAuthCode: String?
)