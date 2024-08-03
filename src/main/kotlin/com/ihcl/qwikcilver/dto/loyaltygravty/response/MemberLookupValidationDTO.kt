package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName

data class MemberLookupValidationDTO(
    @SerializedName("id") var id: Int?,
    @SerializedName("user") var user: UserLookupValidation?,
    @SerializedName("profile_image") var profileImage: String?,
    @SerializedName("member_id") var memberId: String?,
    @SerializedName("salutation") var salutation: String?,
    @SerializedName("middle_name") var middleName: String?,
    @SerializedName("date_of_birth") var dateOfBirth: String?,
    @SerializedName("age") var age: Int?,
    @SerializedName("gender") var gender: String?,
    @SerializedName("marital_status") var maritalStatus: String?,
    @SerializedName("wedding_anniversary") var weddingAnniversary: String?,
    @SerializedName("nationality") var nationality: String?,
    @SerializedName("program_opt_in") var programOptIn: String?,
    @SerializedName("creditcard_number") var creditcardNumber: String?,
    @SerializedName("mobile") var mobile: String?,
    @SerializedName("address_line1") var addressLine1: String?,
    @SerializedName("address_line2") var addressLine2: String?,
    @SerializedName("city") var city: String?,
    @SerializedName("city_name") var cityName: String?,
    @SerializedName("region") var region: String?,
    @SerializedName("region_name") var regionName: String?,
    @SerializedName("postal_code") var postalCode: String?,
    @SerializedName("country") var country: Int?,
    @SerializedName("country_name") var countryName: String?,
    @SerializedName("company") var company: String?,
    @SerializedName("job_title") var jobTitle: String?,
    @SerializedName("favorite_store") var favoriteStore: String?,
    @SerializedName("enrollment_channel") var enrollmentChannel: String?,
    @SerializedName("enrolling_sponsor") var enrollingSponsor: Int?,
    @SerializedName("associate_id") var associateId: String?,
    @SerializedName("receive_offers") var receiveOffers: Boolean?,
    @SerializedName("date_of_joining") var dateOfJoining: String?,
    @SerializedName("membership_tenure") var membershipTenure: Int?,
    @SerializedName("favorite_sponsors") var favoriteSponsors: ArrayList<String>,
    @SerializedName("extra_data") var extraData: ExtraDataValidation?,
    @SerializedName("last_activity_date") var lastActivityDate: String?,
    @SerializedName("days_since_bit") var daysSinceBit: Int?,
    @SerializedName("last_accrual_date") var lastAccrualDate: String?,
    @SerializedName("last_redemption_date") var lastRedemptionDate: String?,
    @SerializedName("merged_member") var mergedMember: String?,
    @SerializedName("validated") var validated: Boolean?,
    @SerializedName("phone_validated") var phoneValidated: String?,
    @SerializedName("email_validated") var emailValidated: String?,
    @SerializedName("version_counter") var versionCounter: Long?,
    @SerializedName("member_type") var memberType: String?,
    @SerializedName("city_code") var cityCode: String?,
    @SerializedName("region_code") var regionCode: String?,
    @SerializedName("country_code") var countryCode: String?,
    @SerializedName("all_sponsor_follower") var allSponsorFollower: String?,
    @SerializedName("balances") var balances: ArrayList<String>,
    @SerializedName("tier_data") var tierData: String?,
    @SerializedName("membership_stage") var membershipStage: String?,
    @SerializedName("member_name") var memberName: String?,

    )

data class UserLookupValidation(

    @SerializedName("first_name") var firstName: String?,
    @SerializedName("last_name") var lastName: String?,
    @SerializedName("email") var email: String?,

    )

data class ExtraDataValidation(

    @SerializedName("state") var state: String?,
    @SerializedName("domicile") var domicile: String?,
    @SerializedName("gst_number") var gstNumber: String?,
    @SerializedName("tic_member") var ticMember: String?,
    @SerializedName("tic_number") var ticNumber: String?,
    @SerializedName("country_code") var countryCode: String?,
    @SerializedName("epicure_type") var epicureType: String?,
    @SerializedName("enroling_agent") var enrolingAgent: String?,
    @SerializedName("analytics_opt_in") var analyticsOptIn: String?,
    @SerializedName("enrolment_source") var enrolmentSource: String?,
    @SerializedName("secondary_email_address") var secondaryEmailAddress: String?,
    @SerializedName("secondary_mobile_number") var secondaryMobileNumber: String?

)

data class ChambersTokenCredentials(
    val username:String,
    val password:String
)
data class ChambersToken(
    val token:String?,
    val expires_in:String?
)
data class ChambersLookUpValidation(
    val member_id:String,
    val external_id:String,
)

data class ChambersMemberLookup(

    @SerializedName("member_id") var memberId:String?
)