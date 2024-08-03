package com.ihcl.qwikcilver.dto.preauthreversal.request

import kotlinx.serialization.Serializable

@Serializable
data class PreauthReversalRequest(
    val Cards: List<Card?>?,
    val InputType: String?,
    val Notes: String?,
    val InvoiceNumber:String?,
    val InvoiceDate:String?,
    val BillAmount:String?,
)