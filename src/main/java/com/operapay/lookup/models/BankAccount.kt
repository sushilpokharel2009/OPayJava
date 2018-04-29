package com.operapay.lookup.models

/**
 * @author Perfect <root>@perfect.engineering>
 */
data class BankAccountLookupRequest(val bankCode: String, val accountNumber: String, val countryCode: String)

typealias BankAccountLookupResponse = String
