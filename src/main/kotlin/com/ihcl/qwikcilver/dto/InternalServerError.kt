package com.ihcl.qwikcilver.dto

import kotlinx.serialization.Serializable

@Serializable
data class InternalServerError (
    val message:String
)
data class PrimaryCardCreationException(
    val message: String
)
data class BadRequest(
    val message: String
)