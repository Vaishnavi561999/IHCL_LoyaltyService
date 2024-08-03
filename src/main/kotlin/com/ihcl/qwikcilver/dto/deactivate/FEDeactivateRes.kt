package com.ihcl.qwikcilver.dto.deactivate

import kotlinx.serialization.Serializable

@Serializable
data class FEDeactivateRes(
    val transactionID:Int?,
    val ResponseCode: Int?,
    val Cards: List<ResCard?>?
)