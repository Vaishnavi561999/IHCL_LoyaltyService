package com.ihcl.qwikcilver.dto.balanceenquiry

 import kotlinx.serialization.Serializable

@Serializable
data class FEBalanceEnquiryResponse(
    val ResponseMessage:String?,
    val ResponseCode:String?,
    val Cards: List<ResCard?>?
)