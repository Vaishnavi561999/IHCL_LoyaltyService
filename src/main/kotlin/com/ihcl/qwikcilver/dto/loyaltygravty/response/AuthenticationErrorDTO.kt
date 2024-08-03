package com.ihcl.qwikcilver.dto.loyaltygravty.response

data class AuthenticationErrorDTO(
    var error: AuthError
)
data class AuthError (
    var code: String,
    var message: String,
    var scope: String,
)