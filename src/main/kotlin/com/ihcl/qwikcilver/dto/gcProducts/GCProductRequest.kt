package com.ihcl.qwikcilver.dto.gcProducts

import kotlinx.serialization.Serializable

@Serializable
data class GCProductRequest(
    val categoryId: String?
)
@Serializable
data class ActivateGCRequest(
    val orderId:String?
)