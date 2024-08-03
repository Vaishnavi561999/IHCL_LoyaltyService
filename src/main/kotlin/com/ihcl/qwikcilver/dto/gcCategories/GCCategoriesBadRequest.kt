package com.ihcl.qwikcilver.dto.gcCategories

import kotlinx.serialization.Serializable

@Serializable
data class GCCategoriesBadRequest(
    val code: Int?,
    val message: String?,
    val messages: List<String?>?
)