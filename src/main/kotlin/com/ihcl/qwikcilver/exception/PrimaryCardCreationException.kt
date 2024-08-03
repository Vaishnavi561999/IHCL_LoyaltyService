package com.ihcl.qwikcilver.exception

class PrimaryCardCreationException(
    override val message: String?
) : Exception()
class BadRequestException(
    override val message: String?
): Exception()