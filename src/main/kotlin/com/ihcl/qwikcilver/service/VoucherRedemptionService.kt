package com.ihcl.qwikcilver.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ihcl.qwikcilver.config.Configuration
import com.ihcl.qwikcilver.dto.loyaltygravty.request.*
import com.ihcl.qwikcilver.dto.loyaltygravty.response.*
import com.ihcl.qwikcilver.dto.loyaltygravty.response.ChamberVoucherAvailResponse
import com.ihcl.qwikcilver.plugins.ConfigureClient
import com.ihcl.qwikcilver.util.Constants.AVAILABLE
import com.ihcl.qwikcilver.util.Constants.BENEFITS_PAGE
import com.ihcl.qwikcilver.util.Constants.CHAMBERS
import com.ihcl.qwikcilver.util.Constants.CHAMBERS_ID_STATUS
import com.ihcl.qwikcilver.util.Constants.DEFAULT_BENEFITS_PAGE_SIZE
import com.ihcl.qwikcilver.util.Constants.DINING
import com.ihcl.qwikcilver.util.Constants.EPICURE
import com.ihcl.qwikcilver.util.Constants.Gravty.BENEFITS_STATUS
import com.ihcl.qwikcilver.util.Constants.OTHERS
import com.ihcl.qwikcilver.util.Constants.PARTIAL_USED
import com.ihcl.qwikcilver.util.Constants.PMS
import com.ihcl.qwikcilver.util.Constants.POS
import com.ihcl.qwikcilver.util.Constants.PRIVILEGES
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_BAR
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_BOOKING
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_BUSINESS_CENTRE
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_DINING
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_FOOD
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_LAUNDRY
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_QMIN
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_SALON
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_SPA
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_WASH
import com.ihcl.qwikcilver.util.Constants.PRODUCT_NAME_WELLNESS
import com.ihcl.qwikcilver.util.Constants.STAY
import com.ihcl.qwikcilver.util.Constants.VOUCHERS
import com.ihcl.qwikcilver.util.Constants.WEB
import com.ihcl.qwikcilver.util.Constants.WELLNESS
import com.ihcl.qwikcilver.util.validatePrivilegePin
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.response.*
import io.ktor.util.*
import org.koin.java.KoinJavaComponent
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class VoucherRedemptionService {
    private val props = Configuration.env
    private val log: Logger = LoggerFactory.getLogger(javaClass)
    val authService by KoinJavaComponent.inject<AuthorizationService>(AuthorizationService::class.java)
    private val fetchMemberShip by KoinJavaComponent.inject<FetchMemberShipsService>(FetchMemberShipsService::class.java)

    /* Method returns get privileges for member */

    private suspend fun getPrivileges(memberId:String): HttpResponse {
        val response:HttpResponse
        try {
            val token= authService.getGravtyAuthToken()

            response= ConfigureClient.client.get ("${props.gravtyBenefitsCarousel}$memberId/$PRIVILEGES/$BENEFITS_STATUS&$BENEFITS_PAGE=$DEFAULT_BENEFITS_PAGE_SIZE"){
                headers{
                    append(props.gravityHeaderKey,props.epicureHeaderCode)
                    append(props.authorizationKey,token)
                }
                timeout {
                    requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                }
                contentType(ContentType.Application.Json)
            }
            if (response.status== HttpStatusCode.Unauthorized){
                authService.getGravtyAuthToken()
                return getPrivileges(memberId)
            }

        }catch (e:Exception){
            log.error("Exception at get privilege API : ${e.stackTraceToString()}")
            throw e.message?.let { BadRequestException(it) }!!
        }

        log.debug("Get privilege Epicure API response.  : {}", response.status)
        return response
    }
    suspend fun getChamberPrivileges(memberId:String, chamberToken:String): HttpResponse {
        val response:HttpResponse
        try {
            response= ConfigureClient.client.get ("${props.gravtyBenefitsCarousel}$memberId/$PRIVILEGES/$BENEFITS_STATUS&$BENEFITS_PAGE=$DEFAULT_BENEFITS_PAGE_SIZE"){
                headers{
                    append(props.gravityHeaderKey,props.chambersHeaderKey)
                    append(props.authorizationKey,chamberToken)
                }
                contentType(ContentType.Application.Json)
            }
            if (response.status== HttpStatusCode.Unauthorized){
                fetchMemberShip.getChambersAuthToken()
                return getChamberPrivileges(memberId,chamberToken)
            }
        }catch (e:Exception){
            log.error("Exception at Get Chamber privileges API Response  : ${e.stackTraceToString()}")
            throw e.message?.let { BadRequestException(it) }!!
        }

        log.info("Get Chamber privileges Epicure API response.  : ${response.status}" )
        return response
    }


    suspend fun getAvailPrivileges(body: VoucherRedemptionAvailPrivileges): Any {
        val response:HttpResponse
        val pinEnabledBody = validatePrivilegePin(body)
        log.info("Request body ...$pinEnabledBody")
        if (body.type == EPICURE) {
            log.info("Calling epicure avail")
            try {
                val token =  authService.getGravtyAuthToken()
                response = ConfigureClient.client.post(props.availPrivilege) {
                    headers {
                        append(props.gravityHeaderKey, props.epicureHeaderCode)
                        append(props.authorizationKey,token)
                    }
                    timeout {
                        requestTimeoutMillis = props.requestTimeoutMillis.toLong()
                    }
                    contentType(ContentType.Application.Json)
                    setBody(pinEnabledBody)
                }
                response.takeIf { it.status == HttpStatusCode.Unauthorized }?.run {
                    authService.getGravtyAuthToken()
                    return getAvailPrivileges(body)
                }
            } catch (e: Exception) {
                log.error("VoucherRedemptionAvail privilege API Response for Epicure while getting  : Exception raised : ${e.message}")
                throw e.message?.let { BadRequestException(it) }!!
            }
            log.debug("Voucher Redemption avail privilege Epicure API response.  : {}", response.status)

            when (response.status) {
                HttpStatusCode.Created->{
                    val chamberResponse =response.body() as VoucherAvailResponse
                    return ChamberVoucherAvailResponse(EPICURE,chamberResponse)
                }
                HttpStatusCode.OK -> {
                    return response.body() as VoucherRedemptionAvailPrivilegeDTO
                }

                HttpStatusCode.BadRequest -> {
                    return response.body() as AvailPrivilegesDTO
                }
                HttpStatusCode.Forbidden -> {
                    return response.body() as Forbidden
                }
                HttpStatusCode.NotAcceptable->{
                    return response.body() as VoucherRedemptionErrorDTO
                }
            }

        }
        else{
            log.info("Calling chamber avail")
            try {
                val token = fetchMemberShip.getChambersAuthToken()
                response = ConfigureClient.client.post(props.availPrivilege) {
                    headers {
                        append(props.gravityHeaderKey, props.chambersHeaderKey)
                        append(props.authorizationKey, token)
                    }
                    contentType(ContentType.Application.Json)
                    setBody(pinEnabledBody)
                }
                if (response.status== HttpStatusCode.Unauthorized){
                    fetchMemberShip.getChambersAuthToken()
                    return getAvailPrivileges(body)
                }
            } catch (e: Exception) {
                log.error("VoucherRedemptionAvail privilege API Response for chambers while getting  : Exception raised : ${e.message}")
                throw e.message?.let { BadRequestException(it) }!!
            }
            log.debug("Voucher Redemption avail privilege Chambers API response.  : {}", response.status)

            when (response.status) {
                HttpStatusCode.Created->{
                    val chamberResponse =response.body() as VoucherAvailResponse
                    return ChamberVoucherAvailResponse(CHAMBERS,chamberResponse)
                }
                HttpStatusCode.OK -> {
                    return response.body() as VoucherRedemptionAvailPrivilegeDTO
                }

                HttpStatusCode.BadRequest -> {
                    return response.body() as AvailPrivilegesDTO
                }
                HttpStatusCode.Forbidden -> {
                    return response.body() as Forbidden
                }
                HttpStatusCode.NotAcceptable->{
                    return response.body() as VoucherRedemptionErrorDTO
                }
            }
        }
        return response
    }

    suspend fun getPrivileges(
        call: ApplicationCall,
        productCategory: String?,
        type: String?,
        token: String?,
        limit: String
    ){
        val fetchMemberShips =
            token?.let { it1 -> fetchMemberShip.getMemberShipDetails(it1) }
        val errorCode = fetchMemberShips?.errorCode?.toInt()
        when (errorCode) {
            401 -> {
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    FetchMemberShipErrorDTO(fetchMemberShips.errorCode, fetchMemberShips.errorReason, fetchMemberShips.errorMessage)
                )
            }
            404 -> {
                call.respond(
                    status = HttpStatusCode.NotFound,
                    FetchMemberShipErrorDTO(fetchMemberShips.errorCode, fetchMemberShips.errorReason, fetchMemberShips.errorMessage)
                )
            }
            422 -> {
                call.respond(
                    status = HttpStatusCode.OK,
                    FetchMemberShipErrorDTO(fetchMemberShips.errorCode, fetchMemberShips.errorReason, fetchMemberShips.errorMessage)
                )
            }
        }
        call.respond(processMemberships(fetchMemberShips, limit.toInt(), productCategory, type))
    }

    private suspend fun processMemberships(
        fetchMemberShips: FetchEpicureMemberShip?,
        limit: Int?,
        productCategory: String?,
        type: String?
    ): Privileges {
        val epicurePendingVouchersType = mutableListOf<Data>()
        val redeemedEpicureVouchersType = mutableListOf<Data>()
        val pendingChamberVouchersType = mutableListOf<ChambersData>()
        val redeemedChamberVouchersType = mutableListOf<ChambersData>()

        fetchMemberShips?.message?.forEach { memberShip ->
            if (memberShip.brandMembershipId!=null) {
                when {
                    memberShip.brandMembershipId?.length!! > 4 -> {
                        val response = getPrivileges(memberShip.brandMembershipId!!)
                        processVouchers(response, productCategory, type)?.let { (pendingVouchers, redeemedVouchers) ->
                            epicurePendingVouchersType.addAll(pendingVouchers)
                            redeemedEpicureVouchersType.addAll(redeemedVouchers)
                        }
                    }
                    else -> {
                        val response = fetchMemberShip.getChambersResponse(memberShip.brandMembershipId!!)
                        processChamberVouchers(
                            response,
                            productCategory,
                            type,
                            memberShip.brandMembershipId
                        )?.let { (pendingVouchers, redeemedVouchers) ->
                            pendingChamberVouchersType.addAll(pendingVouchers)
                            redeemedChamberVouchersType.addAll(redeemedVouchers)
                        }
                    }
                }
            }
        }

        val epicurePendingVouchers = if (limit != null && limit.toInt() < epicurePendingVouchersType.size){
            epicurePendingVouchersType.take(limit.toInt())}
        else {
            epicurePendingVouchersType
        }
        val epicureRedeemedVouchers = if (limit != null && limit.toInt() < redeemedEpicureVouchersType.size){
            redeemedEpicureVouchersType.take(limit.toInt())}
        else {
            redeemedEpicureVouchersType
        }
        val chamberPendingVouchers = if (limit != null && limit.toInt() < pendingChamberVouchersType.size){
            pendingChamberVouchersType.take(limit.toInt())}
        else {
            pendingChamberVouchersType
        }
        val chamberRedeemedVouchers = if (limit != null && limit.toInt() < redeemedChamberVouchersType.size){
            redeemedChamberVouchersType.take(limit.toInt())}
        else {
            redeemedChamberVouchersType
        }

        val epicurePendingVoucher=epicurePendingVouchers.groupBy { it.productName }.values.map { it.toList() }
        val epicureRedeemVoucher=epicureRedeemedVouchers.groupBy { it.productName }.values.map { it.toList() }
        val chamberPendingVoucher=chamberPendingVouchers.groupBy { it.productName }.values.map { it.toList() }
        val chamberRedeemVoucher=chamberRedeemedVouchers.groupBy { it.productName }.values.map { it.toList() }

        return Privileges(
            "${epicurePendingVouchersType.filter { it.productCategory == VOUCHERS }.size + pendingChamberVouchersType.filter { it.productCategory == VOUCHERS }.size}",
            Epicure(epicurePendingVoucher, epicureRedeemVoucher),
            Chamber(chamberPendingVoucher, chamberRedeemVoucher)
        )
    }
    private suspend fun processVouchers(response: HttpResponse, productCategory: String?, type: String?): Pair<List<Data>, List<Data>>? {
        if (response.status != HttpStatusCode.OK) return null

        val listType = object : TypeToken<List<BenefitsCarouselDTO>>() {}.type
        val voucherRedeem: List<BenefitsCarouselDTO> = Gson().fromJson(response.bodyAsText(), listType)
        val updatedDataList = updateDataList(voucherRedeem)

        val sortedVouchers = sortVouchers(updatedDataList, productCategory)

        val (pendingVouchers, redeemedVouchers) = categorizeVouchers(sortedVouchers)

        val pendingEpicureVouchersType = filterVouchersByType(pendingVouchers, type)
        val redeemedEpicureVouchersType = filterVouchersByType(redeemedVouchers, type)

        return Pair(pendingEpicureVouchersType, redeemedEpicureVouchersType)
    }
    private fun updateDataList(voucherRedeem: List<BenefitsCarouselDTO>): List<Data> {
        return voucherRedeem.map { mapVoucherResponseToData(it) }
    }

    private fun mapVoucherResponseToData(voucherResponse: BenefitsCarouselDTO): Data {
        val voucherLabel = determineVoucherLabel(voucherResponse)
        val isRedeemable = isRedeemable(voucherResponse.product?.extraData?.redemptionSource)

        return Data(
            voucherResponse.memberId,
            voucherResponse.privilegeCode,
            voucherResponse.uniquePrivilegeCode,
            PrivilegeExtraData(
                voucherResponse.product?.extraData?.programPrefix,
                voucherResponse.product?.extraData?.productCode,
                voucherResponse.product?.extraData?.promocode,
                voucherResponse.product?.extraData?.redemptionSource
            ),
            voucherResponse.product?.name,
            voucherResponse.product?.description,
            voucherResponse.product?.categoryName?.toUpperCasePreservingASCIIRules(),
            voucherResponse.offer?.offerName,
            voucherResponse.offer?.offerType,
            voucherResponse.offer?.offerDescription,
            voucherResponse.offer?.subtitle,
            voucherResponse.product?.extraData?.redemptionSource?.joinToString(","),
            null,
            null,
            voucherResponse.endDate,
            isRedeemable,
            voucherResponse.pin,
            voucherResponse.startDate,
            voucherResponse.status,
            EPICURE,
            voucherLabel,
            voucherResponse.bitReference
        )
    }

    private fun determineVoucherLabel(voucherResponse: BenefitsCarouselDTO): String {
        return when {
            voucherResponse.product?.extraData?.redemptionSource?.joinToString(",")?.contains(PMS) == true || voucherResponse.product?.extraData?.redemptionSource?.joinToString(",")?.contains(WEB) == true -> STAY
            voucherResponse.product?.extraData?.redemptionSource?.joinToString(",")?.contains(POS) == true -> determinePosVoucherLabel(voucherResponse)
            else -> determinePosVoucherLabel(voucherResponse)
        }
    }

    private fun determinePosVoucherLabel(voucherResponse: BenefitsCarouselDTO): String {
        return when {
            voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_SPA) == true -> WELLNESS
            voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_BUSINESS_CENTRE) == true && voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_BOOKING) == true -> OTHERS
            voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_BOOKING) == true || voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_BAR) == true || voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_WASH) == true -> STAY
            voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_LAUNDRY) == true -> OTHERS
            voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_WELLNESS) == true || voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_SALON) == true -> WELLNESS
            else -> DINING
        }
    }

    private fun isRedeemable(redemptionSource: List<String>?): Boolean {
        return redemptionSource?.joinToString(",") == WEB || redemptionSource?.contains(WEB) == true
    }

    private fun sortVouchers(updatedDataList: List<Data>, productCategory: String?): List<Data> {
        return updatedDataList.sortedWith(
            compareByDescending<Data> { it.labelType == STAY }
                .thenByDescending { it.isReedemable }
                .thenBy { it.labelType }
                .thenByDescending { it.extraData.redemptionSource?.joinToString(",") == WEB }
                .thenBy { it.extraData.redemptionSource?.joinToString(",") }
        ).let { vouchersList ->
            if (!productCategory.isNullOrEmpty()) {
                vouchersList.filter { it.productCategory == productCategory.toUpperCasePreservingASCIIRules() }
            } else {
                vouchersList
            }
        }
    }

    private fun categorizeVouchers(sortedVouchers: List<Data>): Pair<List<Data>, List<Data>> {
        val pendingVouchers = mutableListOf<Data>()
        val redeemedVouchers = mutableListOf<Data>()

        for (data in sortedVouchers) {
            if (data.status == AVAILABLE || data.status == PARTIAL_USED) {
                pendingVouchers.add(data)
            } else {
                redeemedVouchers.add(data)
            }
        }

        return Pair(pendingVouchers, redeemedVouchers)
    }

    private fun filterVouchersByType(vouchers: List<Data>, type: String?): List<Data> {
        return if (type.isNullOrEmpty()) {
            vouchers
        } else {
            vouchers.filter { it.labelType == type }
        }
    }
    private suspend fun processChamberVouchers(
        response: HttpResponse,
        productCategory: String?,
        type: String?,
        chambersExternalId:String?
    ): Pair<List<ChambersData>, List<ChambersData>>? {
        if (response.status != HttpStatusCode.OK) return null

        val listType = object : TypeToken<List<BenefitsCarouselDTO>>() {}.type
        val voucherRedeem: List<BenefitsCarouselDTO> = Gson().fromJson(response.bodyAsText(), listType)
        val updatedDataList = processVoucherData(voucherRedeem,chambersExternalId)
        val sortedVouchers = sortVouchers(updatedDataList, productCategory,"")

        val (pendingVouchers, redeemedVouchers) = splitVouchersByStatus(sortedVouchers)

        val pendingChamberVouchersType = filterVouchersByType(pendingVouchers, type,"")
        val redeemedChamberVouchersType = filterVouchersByType(redeemedVouchers, type,"")

        return Pair(pendingChamberVouchersType, redeemedChamberVouchersType)
    }

    private fun processVoucherData(voucherRedeem: List<BenefitsCarouselDTO>,chambersExternalId:String?): List<ChambersData> {
        return voucherRedeem.map { voucherResponse ->
            val isRedeemable = voucherResponse.product?.extraData?.redemptionSource?.joinToString(",") == WEB || voucherResponse.product?.extraData?.redemptionSource?.joinToString(",")?.contains(WEB) == true
            val voucherLabel = determineChamberVoucherLabel(voucherResponse)
            ChambersData(
                voucherResponse.memberId,
                voucherResponse.privilegeCode,
                voucherResponse.uniquePrivilegeCode,
                PrivilegeChambersExtraData(
                    voucherResponse.product?.extraData?.programPrefix,
                    voucherResponse.product?.extraData?.productCode,
                    voucherResponse.product?.extraData?.redemptionSource
                ),
                voucherResponse.product?.name,
                voucherResponse.product?.description,
                voucherResponse.product?.categoryName?.toUpperCasePreservingASCIIRules(),
                voucherResponse.offer?.offerName,
                voucherResponse.offer?.offerType,
                voucherResponse.offer?.offerDescription,
                voucherResponse.offer?.subtitle,
                voucherResponse.product?.extraData?.redemptionSource?.joinToString(","),
                "",
                "",
                voucherResponse.endDate,
                isRedeemable,
                voucherResponse.pin,
                voucherResponse.startDate,
                voucherResponse.status,
                CHAMBERS,
                voucherLabel,
                voucherResponse.bitReference,
                chambersExternalId
            )
        }
    }

    private fun determineChamberVoucherLabel(voucherResponse: BenefitsCarouselDTO): String {
        return when {
            voucherResponse.product?.extraData?.redemptionSource?.joinToString(",") == WEB ||
            voucherResponse.product?.extraData?.redemptionSource?.joinToString(",") == PMS ||
                    voucherResponse.product?.extraData?.redemptionSource?.joinToString(",")?.contains(WEB) == true ||
                    voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_BOOKING) == true &&
                            voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_BUSINESS_CENTRE )==true ||
                    voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_BAR) == true ||
                    voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_WASH) == true -> {
                STAY
            }
            voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_SPA) == true ||
                    voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_WELLNESS) == true ||
                    voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_SALON) == true -> {
                WELLNESS
            }
            voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_DINING) == true ||
                    voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_FOOD) == true ||
                    voucherResponse.product?.name?.toLowerCasePreservingASCIIRules()?.contains(PRODUCT_NAME_QMIN) == true -> {
                DINING
            }
            else -> OTHERS
        }
    }

    private fun sortVouchers(updatedDataList: List<ChambersData>, productCategory: String?, productName:String): List<ChambersData> {
        log.info("voucher product name : $productName")
        return updatedDataList.filter { productCategory.isNullOrEmpty() || it.productCategory == productCategory.toUpperCasePreservingASCIIRules() }
            .sortedWith(
                compareByDescending<ChambersData> { it.labelType == STAY }
                    .thenByDescending { it.isReedemable }
                    .thenBy { it.labelType }
                    .thenByDescending { it.extraData.redemptionSource?.joinToString(",") == WEB }
                    .thenBy { it.extraData.redemptionSource?.joinToString(",") }
            )
    }

    private fun splitVouchersByStatus(sortedVouchers: List<ChambersData>): Pair<List<ChambersData>, List<ChambersData>> {
        val pendingVouchers = mutableListOf<ChambersData>()
        val redeemedVouchers = mutableListOf<ChambersData>()

        sortedVouchers.forEach { data ->
            if (data.status == AVAILABLE) {
                pendingVouchers.add(data)
            } else {
                redeemedVouchers.add(data)
            }
        }
        return Pair(pendingVouchers, redeemedVouchers)
    }

    private fun filterVouchersByType(vouchers: List<ChambersData>, type: String?,vouchersType:String): List<ChambersData> {
        log.info("VoucherType $vouchersType")
        return if (type.isNullOrEmpty()) {
            vouchers
        } else {
            vouchers.filter { it.labelType == type }
        }
    }


}