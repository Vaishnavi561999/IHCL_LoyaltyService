package com.ihcl.qwikcilver.dto.preauthcompletereversal.request

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val Amount: Double?,
    val CardNumber: String?
)