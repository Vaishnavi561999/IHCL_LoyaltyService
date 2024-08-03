package com.ihcl.qwikcilver.dto.preauthcompletecancel.request

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val CardNumber: String?,
    val OriginalRequest: OriginalRequest?
)