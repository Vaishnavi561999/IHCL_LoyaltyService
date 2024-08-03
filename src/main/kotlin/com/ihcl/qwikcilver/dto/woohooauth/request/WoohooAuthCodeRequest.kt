package com.ihcl.qwikcilver.dto.woohooauth.request

import kotlinx.serialization.Serializable

@Serializable
data class WoohooAuthCodeRequest(
    val clientId: String?,
    val password: String?,
    val username: String?
)