package com.ihcl.qwikcilver.model

data class QCAuthToken (
    val approvalCode:String?,
    val authToken:String?,
    val batchId:Int?
)