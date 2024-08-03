package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList


data class MemberEnrollmentCreateDTO (
    var id :Int,
    var user: User,
    @SerializedName("profile_image")
    var profileImage: Any,
    @SerializedName("member_id")
    var memberId: String,
    var salutation: String,
    @SerializedName("middle_name")
    var middleName: Any,
    @SerializedName("date_of_birth")
    var dateOfBirth: String,
    var age:Int,
    var gender: String,
    @SerializedName("marital_status")
    var maritalStatus: Any,
    @SerializedName("wedding_anniversary")
    var weddingAnniversary: Any,
    var nationality: Any,
    @SerializedName("program_opt_in")
    var programOptIn: String,
    @SerializedName("creditcard_number")
    var creditcardNumber: Any,
    var mobile: String,
    @SerializedName("address_line1")
    var addressLine1: String,
    @SerializedName("address_line2")
    var addressLine2: String,
    var city: Any,
    @SerializedName("city_name")
    var cityName: Any,
    var region: Any,
    @SerializedName("region_name")
    var regionName: Any,
    @SerializedName("postal_code")
    var postalCode: Any,
    var country :Int,
    @SerializedName("country_name")
    var countryName: String,
    var company: Any,
    @SerializedName("job_title")
    var jobTitle: Any,
    @SerializedName("favorite_store")
    var favoriteStore: Any,
    @SerializedName("enrollment_channel")
    var enrollmentChannel: String,
    @SerializedName("enrolling_sponsor")
    var enrollingSponsor :Int,
    @SerializedName("associate_id")
    var associateId: Any,
    @SerializedName("receive_offers")
    var receiveOffers :Boolean,
    @SerializedName("date_of_joining")
    var dateOfJoining: String,
    @SerializedName("membership_tenure")
    var membershipTenure:Int,
    @SerializedName("favorite_sponsors")
    var favoriteSponsors: ArrayList<Any>,
    @SerializedName("extra_data")
    var extraData: ExtraData,
    @SerializedName("last_activity_date")
    var lastActivityDate: Date,
    @SerializedName("days_since_bit")
    var daysSinceBit :Int,
    @SerializedName("last_accrual_date")
    var lastAccrualDate: Any,
    @SerializedName("last_redemption_date")
    var lastRedemptionDate: Any,
    @SerializedName("merged_member")
    var mergedMember: Any,
    var validated:Boolean,
    @SerializedName("phone_validated")
    var phoneValidated: Any,
    @SerializedName("email_validated")
    var emailValidated: Any,
    @SerializedName("version_counter")
    var versionCounter: Long,
    @SerializedName("member_type")
    var memberType: String,
    @SerializedName("family_designation")
    var familyDesignation: Any,
    @SerializedName("region_code")
    var regionCode: String,
    @SerializedName("city_code")
    var cityCode: String,
    @SerializedName("country_code")
    var countryCode: String,
    @SerializedName("all_sponsor_follower")
    var allSponsorFollower: Any,
    var balances: ArrayList<Any>,
    @SerializedName("membership_stage")
    var membershipStage: String,
    @SerializedName("tier_data")
    var tierData: String,

    )
data class ExtraData(
    @SerializedName("epicure_type")
    var epicureType: String,
    var domicile: String,
    var state: String,
    @SerializedName("country_code")
    var countryCode: String,
)


data class User(
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    var email: String
)

data class MemberEnrollmentAcceptDto (

    @SerializedName("mobile"                ) var mobile               : String?    = null,
    @SerializedName("gender"                ) var gender               : String?    = null,
    @SerializedName("salutation"            ) var salutation           : String?    = null,
    @SerializedName("country"               ) var country              : Int?       = null,
    @SerializedName("address_line2"         ) var addressLine2         : String?    = null,
    @SerializedName("extra_data"            ) var extraData            : MemberEnrollmentAcceptExtraData? ,
    @SerializedName("user"                  ) var user                 : MemberEnrollmentAcceptUser?     ,
    @SerializedName("enrollment_touchpoint" ) var enrollmentTouchpoint : Int?       = null,
    @SerializedName("enrollment_channel"    ) var enrollmentChannel    : String?    = null,
    @SerializedName("enrolling_sponsor"     ) var enrollingSponsor     : Int?       = null,
    @SerializedName("enrolling_location"    ) var enrollingLocation    : String?    = null,
    @SerializedName("member_id"             ) var memberId             : String?    = null

)

data class MemberEnrollmentAcceptExtraData (

    @SerializedName("epicure_type" ) var epicureType : String? = null,
    @SerializedName("domicile"     ) var domicile    : String? = null,
    @SerializedName("state"        ) var state       : String? = null,
    @SerializedName("country_code" ) var countryCode : String? = null

)

data class MemberEnrollmentAcceptUser (

    @SerializedName("first_name" ) var firstName : String? = null,
    @SerializedName("last_name"  ) var lastName  : String? = null,
    @SerializedName("email"      ) var email     : String? = null

)