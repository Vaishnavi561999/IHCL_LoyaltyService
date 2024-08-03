package com.ihcl.qwikcilver.dto.woohooauth.request

import kotlinx.serialization.Serializable

@Serializable

data class WoohooAuthTokenRequest(
    val authorizationCode: String?,
    val clientId: String?,
    val clientSecret: String?
)