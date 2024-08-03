package com.ihcl.qwikcilver.dto.neucoins.response

data class ReverseNeuCoinsResponseDTO(
    val customerId: Int?,
    val identifier: Identifier?,
    val orgId: Int?,
    val pointsReversed: Double?,
    val pointsToBeReversed: Double?,
    val redemptionId: String?,
    val reversalId: String?,
    val warnings: List<Any?>?
)

data class Identifier(
    val type: String?,
    val value: String?
)