package com.ihcl.qwikcilver.dto.balanceenquiry.request

import kotlinx.serialization.Serializable

@Serializable
data class Card (
    val CardNumber: String?,
    val CardPin:String?
)