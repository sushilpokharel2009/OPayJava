package com.operapay.lookup

import com.operapay.core.*
import com.operapay.lookup.models.BankAccountLookupRequest
import com.operapay.lookup.models.BankAccountLookupResponse

/**
 * Class to perform Lookups on the OPay platform
 *
 * @author Perfect <perfectm@opay.team>
 */
class Lookup(private val rm: RequestManager = DefaultRequestManager()) {
    companion object {
        const val PATH_BANK_ACCOUNT = "/lookup/bank-account"
    }

    /**
     * Perform a bankAccount lookup
     *
     */
    fun bankAccount(request: BankAccountLookupRequest, success: SuccessCallback<BankAccountLookupResponse>, error: ErrorCallback) {
        rm.postREST(
            Lookup.PATH_BANK_ACCOUNT,
            request,
            parseResponse("lookupBankAccount", BankAccountLookupResponse::class, success),
            error
        )
    }
}