package com.ihcl.qwikcilver.dto.neucoins.request

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class ReverseNeuCoinRequestDTO(
    val identifier: ResIdentifier?,
    val pointsToBeReversed: String?,
    val redemptionId: String?
)

@Serializable
data class ResIdentifier(
    val type: String?,
    val value: String?
)

val validateReverseNeuCoinRequestDTO = Validation{
    ReverseNeuCoinRequestDTO::redemptionId required{
        pattern("\\S+") //should not be empty
    }
    ReverseNeuCoinRequestDTO::pointsToBeReversed required {
        pattern("\\S+") //should not be empty
    }
    ReverseNeuCoinRequestDTO::identifier required {
    }
}