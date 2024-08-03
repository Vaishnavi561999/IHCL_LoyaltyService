package com.ihcl.qwikcilver.dto.reload.request

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val Amount: Double?,
    val CardNumber: String?,
)