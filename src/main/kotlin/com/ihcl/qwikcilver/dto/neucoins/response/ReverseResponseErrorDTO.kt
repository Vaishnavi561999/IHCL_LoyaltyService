package com.ihcl.qwikcilver.dto.neucoins.response

data class ReverseResponseErrorDTO(
    val errors: List<Error?>?
)

data class Error(
    val code: Int?,
    val message: String?,
    val status: Boolean?
)