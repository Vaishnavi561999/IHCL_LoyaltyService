package com.ihcl.qwikcilver.dto.balanceenquiry.response

import kotlinx.serialization.Serializable

@Serializable
data class Holder(
    val FirstName: String?,
    val LastName: String?,
    val Salutation:String?
)