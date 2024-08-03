package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName


data class MemberLookupDTO(
    val data: Datas
)

data class Datas(
    val id:Int,
    val user: Users,
    @SerializedName("profile_image")
    val profileImage:String,
    @SerializedName("member_id")
    val memberId:String,
    val salutation:String,
    @SerializedName("member_name")
    val memberName:String,
    @SerializedName("date_of_birth")
    val dateOfBirth:String,
    val age: String,
    val gender: String,
    @SerializedName("wedding_anniversary")
    val weddingAnniversary: String,
    val nationality: String,
    @SerializedName("creditcard_number")
    val creditCardNumber: String,
    val mobile: String,
    @SerializedName("address_line1")
    val addressLine1: String,
    @SerializedName("address_line2")
    val addressLine2: String,
    val region: Int,
    @SerializedName("region_name")
    val regionName: String,
    @SerializedName("postal_code")
    val postalCode: String,
    val country: String,
    @SerializedName("country_name")
    val countryName: String,
    val company: String,
    @SerializedName("favorite_store")
    val favoriteStore: String,
    @SerializedName("enrolling_sponsor")
    val enrollingSponsor: String,
    @SerializedName("date_of_joining")
    val dateOfJoining: String,
    @SerializedName("membership_tenure")
    val membershipTenure: String,
    @SerializedName("favorite_sponsors")
    val favoriteSponsors: List<Any>,
    @SerializedName("extra_data")
    val extraData: ExtraDataS,
    @SerializedName("external_id")
    val externalId: String,
    @SerializedName("last_activity_date")
    val lastActivityDate: String,
    @SerializedName("merged_member")
    val mergedMember: String,
    @SerializedName("enrollment_referrer")
    val enrollmentReferrer: String,
    val validated: String,
    @SerializedName("version_counter")
    val versionCounter: String,
    @SerializedName("member_type")
    val memberType: String,
    @SerializedName("tier_start_date")
    val tierStartDate: String,
    @SerializedName("tier_end_date")
    val tierEndDate: String,
    @SerializedName("enrollment_user")
    val enrollmentUser: String,
    @SerializedName("region_code")
    val regionCode: String,
    @SerializedName("country_code")
    val countryCode: String,
    @SerializedName("all_sponsor_follower")
    val allSponsorFollower: String,
    @SerializedName("membership_stage")
    val membershipStage: String,
    @SerializedName("points_expiration")
    val pointsExpiration: List<Any>,
    val balances: List<Any>,
)
data class ExtraDataS(
    val gst: String,
    @SerializedName("pa_name")
    val paName: String,
    @SerializedName("lead_city")
    val leadCity: String,
    @SerializedName("site_number")
    val siteNumber: String,
    @SerializedName("source_unit")
    val sourceUnit: String,
    @SerializedName("invoice_city")
    val invoiceCity: String,
    @SerializedName("alliance_type")
    val allianceType: String,
    @SerializedName("chambers_type")
    val chambersType: String,
    @SerializedName("member_consent")
    val memberConsent: String,
    @SerializedName("pa_phone_number")
    val paPhoneNumber: String,
    @SerializedName("pa_email_address")
    val paEmailAddress: String,
    @SerializedName("payment_received")
    val paymentReceived: String,
    @SerializedName("document_received")
    val documentReceived: String,
    @SerializedName("country_phone_code")
    val countryPhoneCode: String,
    @SerializedName("member_determinant")
    val memberDeterminant: String,
    @SerializedName("ready_for_approval")
    val readyForApproval: String,
    @SerializedName("voucher_assignment")
    val voucherAssignment: String,
    @SerializedName("activate_membership")
    val activateMembership: String,
    @SerializedName("transfer_membership")
    val transferMembership: String,
    @SerializedName("relationship_manager")
    val relationshipManager: String,
    @SerializedName("membership_activation_date")
    val membershipActivationDate: String,
    @SerializedName("primary_approval_completed")
    val primaryApprovalCompleted: String,
    @SerializedName("communication_address_line1")
    val communicationAddressLine1: String,
    @SerializedName("secondary_approval_completed")
    val secondaryApprovalCompleted: String,
    @SerializedName("chambers_payment_confirmation")
    val chambersPaymentConfirmation: String,
    @SerializedName("lead_unit_payment_confirmation")
    val leadUnitPaymentConfirmation: String,

)
data class Users(
    @SerializedName("first_name")
    val firstName:String,
    @SerializedName("last_name")
    val lastName:String,
    val email:String
)

data class MemberLookupDTORes(
    @SerializedName("member_id")
    val member_id:String
)