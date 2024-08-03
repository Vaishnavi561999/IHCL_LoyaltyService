package com.ihcl.qwikcilver.dto.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    @SerializedName("TerminalId")
    val terminalId: String?,
    @SerializedName("UserName")
    val userName: String?,
    @SerializedName("Password")
    val password: String?
)