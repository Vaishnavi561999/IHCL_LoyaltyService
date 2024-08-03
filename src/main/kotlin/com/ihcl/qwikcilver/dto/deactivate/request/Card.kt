package com.ihcl.qwikcilver.dto.deactivate.request

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val CardNumber: String?,
    val CardPin: String?
)