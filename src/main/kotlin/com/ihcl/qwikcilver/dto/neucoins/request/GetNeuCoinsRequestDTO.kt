package com.ihcl.qwikcilver.dto.neucoins.request

import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern

data class GetNeuCoinsRequestDTO(
    val authorization: String?
)

val validateGetNeuCoinsRequestDTO = Validation{
    GetNeuCoinsRequestDTO::authorization required{
        pattern("\\S+") //should not be empty
    }
}