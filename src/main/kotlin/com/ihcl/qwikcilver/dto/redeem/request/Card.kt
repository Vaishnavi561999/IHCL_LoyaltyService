package com.ihcl.qwikcilver.dto.redeem.request

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val Amount: Double?,
    val CardNumber: String?,
    val CardPin: String?,
    val InvoiceNumber:String?
)