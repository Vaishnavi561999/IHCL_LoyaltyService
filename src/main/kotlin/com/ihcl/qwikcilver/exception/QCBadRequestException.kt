package com.ihcl.qwikcilver.exception

data class QCBadRequestException (
    var statusCode: Int?,
    var errorMessage: List<String>,
    override val message: String?
):Exception()