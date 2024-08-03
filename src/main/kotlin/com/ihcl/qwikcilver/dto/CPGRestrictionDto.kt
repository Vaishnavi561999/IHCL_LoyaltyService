package com.ihcl.qwikcilver.dto

data class CPGRestrictionDto(
    val _id:String?,
    val giftCardValues:List<CPGValueDetails>?
)
data class CPGValueDetails(
    val cardType:String?,
    val merchant:String?,
    val online:String?,
    val booking:String?
)