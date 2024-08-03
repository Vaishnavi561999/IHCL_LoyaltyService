package com.ihcl.qwikcilver.dto.woohooauth.response

import kotlinx.serialization.Serializable

@Serializable
data class WoohooAuthCodeResponse(
    val authorizationCode: String?
)