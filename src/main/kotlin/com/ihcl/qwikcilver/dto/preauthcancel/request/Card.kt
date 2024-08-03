package com.ihcl.qwikcilver.dto.preauthcancel.request

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val CardNumber: String?,
    val OriginalRequest: OriginalRequest?
)