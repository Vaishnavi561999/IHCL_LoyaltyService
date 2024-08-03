package com.ihcl.qwikcilver.dto.preauth.request

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val Amount: String?,
    val CardNumber: String?,
    val CardPin: String?
)