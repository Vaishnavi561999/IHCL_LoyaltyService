package com.ihcl.qwikcilver.dto.balanceenquiry.response

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val ActivationAmount: Double?,
    val ActivationCode: String?,
    val ActivationDate: String?,
    val ActivationURL: String?,
    val AdjustmentAmount: Double?,
    val ApprovalCode: String?,
    val Balance: Double?,
    val BarCode: String?,
    val Beneficiaries: String?,
    val Buckets: String?,
    val CardBehaviourTypeId: Int?,
    val CardCreationType: String?,
    val CardCurrencySymbol: String?,
//    val CardFormats: List<String?>?,
    val CardIssuingMode: String?,
    val CardNativeBalanceWithoutSymbol: String?,
    val CardNumber: String?,
    val CardPin: String?,
    val CardProgramGroupName: String?,
    val CardProgramGroupType: String?,
    val CardStatus: String?,
    val CardStatusId: Int?,
    val CardType: String?,
    val CorporateName: String?,
    val CurrencyCode: String?,
    val CurrencyConversionRate: String?,
    val CurrencyConvertedXactionAmount: Double?,
    val EmployeeId: String?,
    val ErrorCode: String?,
    val ErrorDescription: String?,
    val ExpiryDate: String?,
    val ExtendedParameters: String?,
    val Holder: Holder?,
    val InvoiceNumber: String?,
    val IssuerName: String?,
    val NativeCardBalance: Double?,
    val NativeCurrencyCode: String?,
    val Notes: String?,
    val PreAuthCode: String?,
    val PreXactionCardBalance: Double?,
    val PreXactionCardBalanceInNativeCurrency: String?,
    val PreXactionCardBalanceInNativeCurrencyWithSymbol: String?,
    val Reason: String?,
    val RecentTransactions: String?,
    val RedeemStartDate: String?,
    val ReloadableAmount: String?,
    val ResponseCode: Int?,
    val ResponseMessage: String?,
    val Reusable: Boolean?,
    val SequenceNumber: String?,
    val ThemeId: String?,
    val TotalPreAuthAmount: Double?,
    val TotalRedeemedAmount: Double?,
    val TotalReloadedAmount: Double?,
    val Trackdata: String?,
    val TransactionAmount: Double?,
    val TransactionDateTime: String?,
    val TransferCardBalance: Double?,
    val TransferCardExpiry: String?,
    val TransferCardNumber: String?,
    val Transferable: Boolean?
)