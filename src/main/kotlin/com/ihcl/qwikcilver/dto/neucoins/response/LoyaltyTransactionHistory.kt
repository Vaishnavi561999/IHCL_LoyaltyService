package com.ihcl.qwikcilver.dto.neucoins.response

import kotlinx.serialization.Serializable


@Serializable
data class LoyaltyTransactionHistoryDTO(
    val response:TransactionHistory
)
@Serializable
data class TransactionHistory(
    val customer:CustomerDetails,
    val transactions:List<Transactions>
)
@Serializable
data class CustomerDetails(
    val firstname:String,
    val lastname:String,
    val user_id:String,
    val external_id:String,
)
@Serializable
data class Transactions(
    val id :String,
    val number :String,
    val type :String,
    val amount :String,
    val billing_time :String,
    val redeemable_from :String,
    val gross_amount :String,
    val discount :String,
    val store :String,
    val store_code :String,
    val points :String,
    val points_type :String,
    val txn_source :String,
    val program_id :String,
    val points_label :String,
    val label :String,
    val expiry_date :String,
)


data class LoyaltyTransactionHistoryError(
    val timestamp:String,
    val status:String,
    val errorCode:String,
    val message:String,
    val errors:List<String>,
)
data class LoyaltyTransactionHistoryUnauthorized(
    val errorCode:String,
    val errorReason:String,
    val errorMessage:String,
)