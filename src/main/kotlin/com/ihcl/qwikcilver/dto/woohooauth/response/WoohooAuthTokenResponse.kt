package com.ihcl.qwikcilver.dto.woohooauth.response

import kotlinx.serialization.Serializable

@Serializable
data class WoohooAuthTokenResponse(
    val token: String?
)