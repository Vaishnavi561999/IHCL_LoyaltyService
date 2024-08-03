package com.ihcl.qwikcilver.dto.reload.response

import kotlinx.serialization.Serializable

@Serializable
data class Holder(
    val FirstName: String?,
    val LastName: String?
)