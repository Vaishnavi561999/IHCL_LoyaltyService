package com.ihcl.qwikcilver.exception

import io.ktor.http.*

class HttpResponseException(val data: Any, val statusCode: HttpStatusCode) : Exception()