package com.ihcl.qwikcilver.dto.auth

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerializedName("ApprovalCode")
    val approvalCode: String?,
    @SerializedName("AuthToken")
    val authToken: String?,
    @SerializedName("BatchId")
    val batchId: Int?,
    @SerializedName("CultureName")
    val cultureName: String?,
    @SerializedName("CurrencyDecimalDigits")
    val currencyDecimalDigits: Int?,
    @SerializedName("CurrencyPosition")
    val currencyPosition: Int?,
    @SerializedName("CurrencySymbol")
    val currencySymbol: String?,
    @SerializedName("DateAtServer")
    val dateAtServer: String?,
    @SerializedName("DisplayUnitForPoints")
    val displayUnitForPoints: String?,
    @SerializedName("ErrorCode")
    val errorCode: String?,
    @SerializedName("ErrorDescription")
    val errorDescription: String?,
    @SerializedName("IntegerAmounts")
    val integerAmounts: Boolean?,
    @SerializedName("InvoiceNumberMandatory")
    val invoiceNumberMandatory: Boolean?,
    @SerializedName("MaskCardNumber")
    val maskCardNumber: Boolean?,
    @SerializedName("MerchantId")
    val merchantId: Int?,
    @SerializedName("MerchantName")
    val merchantName: String?,
    @SerializedName("Notes")
    val notes: String?,
    @SerializedName("NumericUserPwd")
    val numericUserPwd: Boolean?,
    @SerializedName("OutletAddress1")
    val outletAddress1: String?,
    @SerializedName("OutletAddress2")
    val outletAddress2: String?,
    @SerializedName("OutletCity")
    val outletCity: String?,
    @SerializedName("OutletPinCode")
    val outletPinCode: String?,
    @SerializedName("OutletState")
    val outletState: String?,
    @SerializedName("OutletTelephone")
    val outletTelephone: String?,
    @SerializedName("PrintMerchantCopy")
    val printMerchantCopy: Boolean?,
    @SerializedName("ReceiptFooterLine1")
    val receiptFooterLine1: String?,
    @SerializedName("ReceiptFooterLine2")
    val receiptFooterLine2: String?,
    @SerializedName("ReceiptFooterLine3")
    val receiptFooterLine3: String?,
    @SerializedName("ReceiptFooterLine4")
    val receiptFooterLine4: String?,
    @SerializedName("ResponseCode")
    val responseCode: Int?,
    @SerializedName("ResponseMessage")
    val responseMessage: String?,
    @SerializedName("Result")
    val result: Boolean?,
    @SerializedName("TransactionId")
    val transactionId: Long?,
    @SerializedName("TransactionStatus")
    val transactionStatus: Boolean?,
    @SerializedName("TransactionType")
    val transactionType: String?
)