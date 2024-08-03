package com.ihcl.qwikcilver.dto.neucoins.response

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetNeuCoinsResponseDto(
    val blockedRedeemption: String?,
    val blockedRedeemptionReason: String?,
    val groupLoyaltyProgramDetails: List<GroupLoyaltyProgramDetail?>?
)

@Serializable
data class GroupLoyaltyProgramDetail(
    val description: String?,
    val groupProgramId: Int?,
    @Contextual @SerialName("lifetimePoints")
    val lifetimePoints: Number,
    val loyaltyPoints: Int?,
    val pointsToCurrencyRatio: Int?,
    val programsList: List<Programs?>?,
    val promisedPoints: Int?,
    val title: String?
)

@Serializable
data class Programs(
    val description: String?,
    val id: Int?,
    val name: String?
)
@Serializable
data class GetLoyaltyPointsErrorResponseDTO(
    val errorCode:String,
    val errorReason:String,
    val errorMessage:String
)
