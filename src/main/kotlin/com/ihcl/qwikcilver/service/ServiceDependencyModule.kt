package com.ihcl.qwikcilver.service

import org.koin.dsl.module

val serviceModule = module {
    single {
        BalanceEnquiryService()
    }
    single {
        AuthorizationService()
    }
    single {
        PreauthService()
    }
    single {
        ReloadService()
    }
    single {
        PreauthCompleteService()
    }
    single {
        PreauthCompleteCancelService()
    }
    single {
        PreauthCancelService()
    }
    single {
        PreauthCompleteReversalService()
    }
    single {
        PreauthReversalService()
    }
    single {
        DeactivateService()
    }
    single {
        RedeemService()
    }
    single {
        WoohooAuthorizationService()
    }
    single {
        OrderGiftCardService()
    }
    single {
        OrderStatusService()
    }
    single {
        GCCategoriesService()
    }
    single {
        GCProductsService()
    }
    single {
        ReverseNeuCoinsService()
    }
    single {
        RedeemNeuCoinsService()
    }
    single {
        GetNeuCoinsService()
    }
    single {
        MemberEnrollmentService()
    }
    single {
        MemberLookupService()
    }
    single {
        BITCancellationVoucherReversalService()
    }
    single {
        MemberShipPlanService()
    }
    single {
        VoucherRedemptionService()
    }
    single {
        CancelRedeemService()
    }
    single {
        ReverseRedeemService()
    }
    single {
        ActivateGCService()
    }
    single {
        GCOrderStatusService()
    }
    single {
        AddOnCardService()
    }
    single {
        EpicurePrimaryCardService()
    }
    single {
        GravtyMemberCardsService()
    }
    single {
        FetchMemberShipsService()
    }
    single {
        ReloadAuthentication()
    }

}