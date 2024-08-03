package com.ihcl.qwikcilver.dto.loyaltygravty.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MemberEnrollment(
    var mobile: String?,
    var gender: String,
    var salutation: String,
    var country: Int,
    @SerializedName("date_of_birth")
    var dateOfBirth: String,
    @SerializedName("address_line1")
    var addressLine1: String?,
    @SerializedName("address_line2")
    var addressLine2: String,
    @SerializedName("extra_data")
    var extraData: ExtraData,
    var user: User,
    @SerializedName("enrollment_touchpoint")
    var enrollmentTouchpoint: Int,
    @SerializedName("enrollment_channel")
    var enrollmentChannel: String,
    @SerializedName("enrolling_sponsor")
    var enrollingSponsor: Int,
    @SerializedName("enrolling_location")
    var enrollingLocation: String

)
@Serializable
data class ExtraData(
    @SerializedName("epicure_type")
    var epicureType: String,
    var domicile: String,
    var state: String,
    @SerializedName("country_code")
    var countryCode: String
)

@Serializable
data class User(
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    var email: String
)
