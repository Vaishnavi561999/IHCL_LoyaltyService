package com.ihcl.qwikcilver.dto.preauthcompletereversal.request

import kotlinx.serialization.Serializable

@Serializable
data class PreauthCompleteReversalRequest(
    val Cards: List<Card?>?,
    val InputType: String,
    val Notes: String?,
    val InvoiceNumber:String?,
    val InvoiceDate:String?,
    val BillAmount:String?,
)