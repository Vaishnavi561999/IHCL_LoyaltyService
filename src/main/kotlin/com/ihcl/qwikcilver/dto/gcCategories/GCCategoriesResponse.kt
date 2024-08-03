package com.ihcl.qwikcilver.dto.gcCategories

import kotlinx.serialization.Serializable

@Serializable
data class GCCategoriesResponse(
    val description: String?,
    val id: String?,
    val images: Images?,
    val name: String?,
    val subcategories: List<String?>?,
    val subcategoriesCount: Int?,
    val url: String?
)
@Serializable
data class Images(
    val image: String?,
    val thumbnail: String?
)