package com.ihcl.qwikcilver.dto.balanceenquiry.request

import kotlinx.serialization.Serializable

@Serializable
data class BalanceEnquiryRequest(

    val TransactionTypeId: Int?,
    val InputType: String?,
    val Cards: List<Card?>?,
    )