package com.ihcl.qwikcilver.dto.loyaltygravty.response

import com.google.gson.annotations.SerializedName

data class VoucherRedemptionGetPrivilegeDTO(
    @SerializedName("data")
    var data: List<Data>
)
data class VoucherRedemptionGetChambersPrivilegeDTO(
    @SerializedName("data")
    var data: List<ChambersData>
)
data class ChambersData(

    @SerializedName("memberID")
    var memberID: String?,
    @SerializedName("privilegeCode")
    var privilegeCode: String?,
    @SerializedName("uniquePrivilegeCode")
    var uniquePrivilegeCode: String?,
    @SerializedName("extraData")
    var extraData: PrivilegeChambersExtraData,
    @SerializedName("productName")
    var productName: String?,
    @SerializedName("productDescription")
    var productDescription: String?,
    @SerializedName("productCategory")
    var productCategory: String?,
    @SerializedName("offerName")
    var offerName: String?,
    @SerializedName("offerType")
    var offerType: String?,
    @SerializedName("offerDesc")
    var offerDesc: String?,
    @SerializedName("offerTitle")
    var offerTitle: String?,
    @SerializedName("cdc")
    var cdc: String?,
    @SerializedName("offer_desktop_image")
    var offerDesktopImage: String?,
    @SerializedName("offer_mobile_image")
    var offerMobileImage: String?,
    @SerializedName("validTill")
    var validTill: String?,
    var isReedemable: Boolean,
    @SerializedName("pin")
    var pin: String?,
    @SerializedName("createdOn")
    var createdOn: String?,
    @SerializedName("status")
    var status: String?,
    var label:String?,
    var labelType:String?,
    @SerializedName("bitID")
    var bitID: String?,
    @SerializedName("chambersExternalId")
    var chambersExternalId: String?,

)


data class Data(

    @SerializedName("memberID")
    var memberID: String?,
    @SerializedName("privilegeCode")
    var privilegeCode: String?,
    @SerializedName("uniquePrivilegeCode")
    var uniquePrivilegeCode: String?,
    var extraData: PrivilegeExtraData,
    @SerializedName("productName")
    var productName: String?,
    @SerializedName("productDescription")
    var productDescription: String?,
    @SerializedName("productCategory")
    var productCategory: String?,
    @SerializedName("offerName")
    var offerName: String?,
    @SerializedName("offerType")
    var offerType: String?,
    @SerializedName("offerDesc")
    var offerDesc: String?,
    @SerializedName("offerTitle")
    var offerTitle: String?,
    @SerializedName("cdc")
    var cdc: String?,
    @SerializedName("offer_desktop_image")
    var offerDesktopImage: String?,
    @SerializedName("offer_mobile_image")
    var offerMobileImage: String?,
    @SerializedName("validTill")
    var validTill: String?,
    var isReedemable: Boolean,
    @SerializedName("pin")
    var pin: String?,
    @SerializedName("createdOn")
    var createdOn: String?,
    @SerializedName("status")
    var status: String?,
    var label:String?,
    var labelType:String?,
    @SerializedName("bitID")
    var bitID: String?,

    )
data class PrivilegeExtraData(
    @SerializedName("program_prefix")var programPrefix:String?,
    @SerializedName("product_code") var productCode:String?,
    var promocode:String?,
    @SerializedName("redemption_source") var redemptionSource:ArrayList<String>?
)
data class PrivilegeChambersExtraData(
    @SerializedName("program_prefix")
    var programPrefix:String?,
    @SerializedName("product_code")
    var productCode:String?,
    @SerializedName("redemption_source") var redemptionSource:ArrayList<String>?,
)
data class Privileges(
    val count:String,
    val epicure:Epicure,
    val chamber:Chamber,
)
data class Epicure(
    val pendingVouchers: List<List<Data>>,
    val redeemedVouchers: List<List<Data>>
)
data class Chamber(
    val pendingVouchers: List<List<ChambersData>>,
    val redeemedVouchers: List<List<ChambersData>>
)

data class BenefitsCarouselDTO (

    @SerializedName("id"                     ) var id                   : Int?     = null,
    @SerializedName("usage_date"             ) var usageDate            : String?  = null,
    @SerializedName("unique_privilege_code"  ) var uniquePrivilegeCode  : String?  = null,
    @SerializedName("member_id"              ) var memberId             : String?  = null,
    @SerializedName("privilege_code"         ) var privilegeCode        : String?  = null,
    @SerializedName("product"                ) var product              : BenefitsProduct?=null,
    @SerializedName("offer"                  ) var offer                : BenefitsOffer?   = null,
    @SerializedName("bit_reference"          ) var bitReference         : String?  = null,
    @SerializedName("start_date"             ) var startDate            : String?  = null,
    @SerializedName("end_date"               ) var endDate              : String?  = null,
    @SerializedName("value"                  ) var value                : Int?     = null,
    @SerializedName("availment_count"        ) var availmentCount       : Int?     = null,
    @SerializedName("status"                 ) var status               : String?  = null,
    @SerializedName("external_code"          ) var externalCode         : String?  = null,
    @SerializedName("original_member_id"     ) var originalMemberId     : String?  = null,
    @SerializedName("unique_flag"            ) var uniqueFlag           : Boolean? = null,
    @SerializedName("avail_via_pin"          ) var availViaPin          : String?  = null,
    @SerializedName("usage_count"            ) var usageCount           : Int?     = null,
    @SerializedName("total_count"            ) var totalCount           : Int?     = null,
    @SerializedName("enrollment_date"        ) var enrollmentDate       : String?  = null,
    @SerializedName("is_parent_privilege"    ) var isParentPrivilege    : Boolean? = null,
    @SerializedName("action"                 ) var action               : String?  = null,
    @SerializedName("assigned_bit_reference" ) var assignedBitReference : String?  = null,
    @SerializedName("parent_privilege"       ) var parentPrivilege      : String?  = null,
    @SerializedName("tier_class_code"        ) var tierClassCode        : String?  = null,
    @SerializedName("tier_code"              ) var tierCode             : String?  = null,
    @SerializedName("source_type"            ) var sourceType           : String?  = null,
    @SerializedName("pin"            ) var pin : String?  = null,

    )

data class BenefitsExtraData (

    @SerializedName("promocode") var promocode:String?=null,
    @SerializedName("product_code" ) var productCode : String? = null,
    @SerializedName("program_prefix" ) var programPrefix : String? = null,
    @SerializedName("redemption_source") var redemptionSource:ArrayList<String>?= null,


    )
data class BenefitsProduct (

    @SerializedName("id"                           ) var id                        : Int?       = null,
    @SerializedName("name"                         ) var name                      : String?    = null,
    @SerializedName("sponsor_name"                 ) var sponsorName               : String?    = null,
    @SerializedName("type"                         ) var type                      : String?    = null,
    @SerializedName("cdc"                          ) var cdc                       : String?    = null,
    @SerializedName("award_currency"               ) var awardCurrency             : String?    = null,
    @SerializedName("award_price"                  ) var awardPrice                : String?    = null,
    @SerializedName("cost"                         ) var cost                      : Int?       = null,
    @SerializedName("usage"                        ) var usage                     : String?    = null,
    @SerializedName("category_name"                ) var categoryName              : String?    = null,
    @SerializedName("extra_data"                   ) var extraData                 : BenefitsExtraData?=null,
    @SerializedName("has_external_privilege"       ) var hasExternalPrivilege      : Boolean?   = null,
    @SerializedName("has_external_privilege_codes" ) var hasExternalPrivilegeCodes : Boolean?   = null,
    @SerializedName("description" ) var description : String?   = null,

)
data class BenefitsOffer (

    @SerializedName("id"                 ) var id               : Int?       = null,
    @SerializedName("offer_name"         ) var offerName        : String?    = null,
    @SerializedName("subtitle"           ) var subtitle         : String?    = null,
    @SerializedName("offer_type"         ) var offerType        : String?    = null,
    @SerializedName("price"              ) var price            : String?    = null,
    @SerializedName("offer_description"  ) var offerDescription : String?    = null,
    @SerializedName("cost"               ) var cost             : String?    = null,
    @SerializedName("term_and_condition" ) var termAndCondition : String?    = null,
    @SerializedName("is_base_offer"      ) var isBaseOffer      : Boolean?   = null,
    @SerializedName("is_template_based"  ) var isTemplateBased  : Boolean?   = null

)