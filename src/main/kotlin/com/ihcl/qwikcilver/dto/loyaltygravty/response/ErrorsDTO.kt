package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName

data class AvailPrivilegesDTO(
    val error:AvailPrivilegeError
)
data class AvailPrivilegeError(
    val code:String,
    val message:String,
    val scope:String,
)
data class ErrorsDTO(
    var error: Errorss?
)
data class Errorss (
    var mobile: List<ErrorObject>?,
    var email: List<ErrorObject>?,
    var gender: List<ErrorObject>?,
    var salutation: List<ErrorObject>?,
    var country: List<ErrorObject>?,
    var date_of_birth: List<ErrorObject>?,
    var scope: String,
    var message: String,
    var code: String,
    var type: String,
    var status_code: String,
    var data: DataError,
    var enrollment_touchpoint: List<ErrorObject>?,
    var non_field_errors: List<ErrorObject>?,
    var enrollment_channel: List<ErrorObject>?,
    var card_start_date: List<ErrorObject>?,
    var membership_plan_id: List<ErrorObject>?,
    var name_on_card: List<ErrorObject>?,
    var user: UserError,

)
data class DataError(
    var member_id:String
)
data class UserError(
    var first_name: List<ErrorObject>?,
    var last_name: List<ErrorObject>?,
)
data class ErrorObject (
    var message: String,
    var code: String,
)
data class Forbidden(
    val message:String
)

data class MembersErrorDTO(
    var error: Errors
)
data class Errors(
    val message:String,
    val code:String,
    val type:String,
    @SerializedName("status_code")
    val statusCode:Int,
    val data:String,
    val scope:String,
)
data class MemberShipBadRequestError(
    val error: MemberShipError
)
data class MemberShipError(
    val payment_date:ArrayList<Messages>?,
    val orion_payment_method:ArrayList<Messages>?,
    val orion_payment_type:ArrayList<Messages>?,
    val membership_plan_start_date:ArrayList<Messages>?,
    val membership_plan_id:ArrayList<Messages>?,
    val card_start_date:ArrayList<Messages>?,
    val name_on_card:ArrayList<Messages>?,
    val scope:String?
)
data class Messages(
    val message:String,
    val code: String
)


