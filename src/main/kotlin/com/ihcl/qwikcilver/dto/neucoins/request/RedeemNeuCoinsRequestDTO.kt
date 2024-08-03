package com.ihcl.qwikcilver.dto.neucoins.request

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern

data class RedeemNeuCoinsRequestDTO(
    val customFields: CustomFields?,
    val externalReferenceNumber: String?,
    val pointsRedeemed: String?,
    val transactionNumber: String?
)

data class CustomFields(
    val `field`: List<Field?>?
)

data class Field(
    val name: String?,
    val value: String?
)

val validateRedeemNeuCoinsRequestDTO = Validation{
    RedeemNeuCoinsRequestDTO::externalReferenceNumber required{
        pattern("\\S+") //should not be empty
    }
    RedeemNeuCoinsRequestDTO::pointsRedeemed required {
        pattern("\\S+") //should not be empty
    }
    RedeemNeuCoinsRequestDTO::transactionNumber required {
        pattern("\\S+") //should not be empty
    }
}

