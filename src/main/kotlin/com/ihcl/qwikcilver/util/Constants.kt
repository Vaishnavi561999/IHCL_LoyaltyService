package com.ihcl.qwikcilver.util

object Constants {
    object WOOHOO {
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val TIMEZONE_ID = "UTC"
        const val TOKENKEY = "WOOHOOTOKEN"
        const val STATUS_URL = "status"
        const val PRODUCT_URL = "products"
        const val ACTIVATE_URL = "cards"
        const val COUNTRY = "IN"
    }
    object Gravty{
        const val BENEFITS_STATUS="?status=AVAILABLE%2CPARTIAL_USED%2CUSED"
    }

    object QC {
        const val HEADER_DATEATCLIENT = "DateAtClient"
        const val HEADER_TRANSACTIONID = "TransactionId"
        const val HEADER_SIGNATURE = "Signature"
        const val SYNC_ONLY_FALSE = false
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val INVALID_TOKEN = "10744"
        const val TOKEN_EXPIRED = "10745"
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"
        const val CHECK_FAIL_TRANSACTIONID = "10019"
        const val REQUEST_RESPONSECODE = 0
        const val TOKENkEY = "GCTOKEN"
        const val RELOADTOKENKEY = "RELOADGCTOKEN"
        const val GIFT_CARD_CATEGORY_RETRIEVE_COUNT = "giftCardCategoryRetrieveCount"
        const val RELOAD_NUMBER_OF_CARDS = "1"
        const val REVERSAL_PREAUTH_NOTE = "pre auth reversal"
        const val PREAUTH_COMPLETENOTE = "test notes"
        const val PREAUTH_REVERSALCOMPLETE_NOTE = "Preauth complete reversal"
        const val PREAUTH_CANCEL_NOTE = "Cancel transaction"
        const val TRANSACTION_ID = "QC_TRANSACTION_ID"
        const val INVALID_GC_CATALOGUE_CREDENTIALS = "Invalid username or password. Please try again."
        const val INVALID_GC_CATALOGUE_CREDENTIALS_ACCOUNT_RESTRICT = "Your Account has been temporarily restricted....!!!. Please try again after some time.."
        const val INITIAL_GC_CATALOGUE_VALUE = "2"

        const val TIME_ZONE_ID = "Asia/Kolkata"
        const val QC_WOOHOO_TOKEN_EXPIRY = 604800

        object TransactionType {
            const val BALANCE_ENQUIRY = 306
            const val DEACTIVATE = 304
            const val RELOAD = 303
            const val REDEEM = 302
        }

        object Path {
            const val CANCEl_URL = "/gc/transactions/cancel"
            const val REVERSE_URL = "/gc/transactions/reverse"
            const val TRANSACTIONS_URL = "/gc/transactions"
            const val CATALOGUE_URL = "/rest/v3/catalog/categories"
            const val GET_METHOD = "GET"
            const val POST_METHOD = "POST"
            const val AUTHORIZATION = "/authorize"
            const val WOHOO_ENDPOINT = "/rest/v3/order"
            const val AUTHCODEURL = "/oauth2/verify"
            const val AUTHTOKENURL = "/oauth2/token"
            const val WOHOO_ENDPOINT_GC = "/rest/v3/orders"
        }

        object RequestParams {
            const val PREAUTHTYPE = 1
            const val INPUTTYPE = "1"
            const val PREAUTHCOMPLETETYPE = 2
            const val TRANSACTIONMODEID = 0
        }
    }

    const val MEMBERSHIP_PLAN_NOT_CREATED = "Bit cancellation voucher reversal is not done"
    const val BIT_CANCELLATION_NOT_DONE = "Bit cancellation voucher reversal is not done"
    const val CLIENT_ID = "client_id"
    const val STORE_ID = "store_id"
    const val AUTHORIZATION = "authorization"
    const val PARTNER_ID = "partner_id"
    const val PMS = "PMS"
    const val POS = "POS"
    const val WEB = "WEB"
    const val VOUCHERS = "VOUCHERS"
    const val EPICURE = "EPICURE"
    const val CHAMBERS = "THE CHAMBERS"
    const val BENEFITS_PAGE = "page_size"
    const val DEFAULT_BENEFITS_PAGE_SIZE= "400"
    const val STAY = "STAY"
    const val DINING = "DINING"
    const val WELLNESS = "WELLNESS"
    const val AVAILABLE = "AVAILABLE"
    const val PARTIAL_USED = "PARTIAL_USED"
    const val PRIVILEGES = "privileges"
    const val REVERSAL = "REVERSAL"
    const val CANCELLATION = "CANCELLATION"
    const val AVAILMENT = "AVAILMENT"
    const val AUTH_TOKEN = "Bearer "
    const val PRODUCT_NAME_SPA = "spa"
    const val VOUCHER_PRODUCT_CATEGORY = "productCategory"
    const val VOUCHER_TYPE = "type"
    const val VOUCHER_AUTHORIZATION = "Authorization"
    const val VOUCHER_LIMIT = "limit"
    const val GRAVTY_MEMBER_ID = "member_id"
    const val GRAVTY_MOBILE = "mobile"
    const val GRAVTY_EMAIL = "email"
    const val ERROR_RESPONSE_MESSAGE_OF_GIFT_CARD="Your gift card is expired. Please email reservations@ihcltata.com for further assistance."
    const val INVALID_ORDER ="Invalid order number or email address. Please check and try again."
    const val REDEEM_GIFT_CARD_ERROR_MESSAGE="Entered Gift Card is not allowed for Online Redemption."
    const val CARDNUMBER_OR_CARDPIN_EXPIRED=10001
    const val CARD_NUMBER_INCORRECT= 10004
    const val CARD_PIN_INCORRECT= 10086
    const val CARD_NUMBER_DE_ACTIVE_ERROR_MESSAGE="Your gift card is inactive. Please email reservations@ihcltata.com for further assistance."
    const val CARD_NUMBER_OR_CARDPIN_INCORRECT_ERROR_MESSAGE="Either the card number or PIN entered is incorrect. Please verify and try again."
    const val CARD_NUMBER_DE_ACTIVE_CODE=10027
    const val GC_ORDER_NOT_FOUND_ERROR_CODE = 5320
    const val SUCCESS="SUCCESS"
    const val GC_ORDER_STATUS_COMPLETE="Complete"
    const val GC_ORDER_STATUS_IN_VOICED="Invoiced"
    const val GC_ORDER_STATUS_IN_SHIPPED="Shipped"
    const val GC_ORDER_STATUS_IN_PROCESSING="Processing"
    const val GC_ORDER_STATUS_BUSINESS_APPROVED="Business Approved"
    const val E_GIFT_CARD_LABEL="E-Gift Card"
    const val PHYSICAL_GIFT_CARD_LABEL="Physical Order"
    const val GRAVTY_EXTERNAL_ID="external_id"
    const val GRAVTY_CARD_STATUS="ACTIVE"
    const val DEFAULT_LIMIT = "2"
    const val PRODUCT_NAME_LAUNDRY = "laundry"
    const val PRODUCT_NAME_BOOKING = "bookings"
    const val PRODUCT_NAME_WELLNESS = "wellness"
    const val PRODUCT_NAME_DINING = "dining"
    const val PRODUCT_NAME_BAR = "bar"
    const val PRODUCT_NAME_WASH = "wash"
    const val PRODUCT_NAME_FOOD = "food"
    const val PRODUCT_NAME_QMIN = "qmin"
    const val PRODUCT_NAME_BUSINESS_CENTRE = "business"
    const val PRODUCT_NAME_SALON = "salon"
    const val OTHERS = "OTHERS"
    const val CHAMBERS_ID_STATUS = "active"
    const val CPG_ID="CPG_1"
    const val HOTEL_BOOKING="Hotel_Booking"
     const val GRAVTY_TOKEN="GRAVTYTOKEN"
    const val CHAMBERS_TOKEN="CHAMBERSTOKEN"
    const val JWT_TOKEN="JWT"
     const val BOOKINGS="Booking"
    const val CPG_ERROR_MESSAGE="Data is not present in DB"
    const val FETCH_NEUCOINS_HEADERKEY="Authorization"
}