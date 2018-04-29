package com.operapay.gateway.models

/**
 *
 * @author Perfect <perfectm@opay.team>
 */

typealias InstrumentType = String

val Account: InstrumentType = "account"
val Card: InstrumentType = "card"

sealed class GatewayChargeRequest(
        val publicKey: String,
        val currency: String,
        val countryCode: String,
        val amount: String,
        val reference: String,
        val instrumentType: InstrumentType,
        val tokenize: Boolean = false,
        val cardNumber: String? = null,
        val cardDateMonth: String? = null,
        val cardDateYear: String? = null,
        val cardCVC: String? = null,
        val senderAccountNumber: String? = null,
        val bankCode: String? = null
)

class GatewayCardChargeRequest(
        publicKey: String,
        currency: String,
        countryCode: String,
        amount: String,
        reference: String,
        tokenize: Boolean,
        cardNumber: String,
        cardDateMonth: String,
        cardDateYear: String,
        cardCVC: String)
    : GatewayChargeRequest(
        publicKey,
        currency,
        countryCode,
        amount,
        reference,
        Card,
        tokenize,
        cardNumber = cardNumber,
        cardDateMonth = cardDateMonth,
        cardDateYear = cardDateYear,
        cardCVC = cardCVC
    )

class GatewayAccountChargeRequest(
        publicKey: String,
        currency: String,
        countryCode: String,
        amount: String,
        reference: String,
        tokenize: Boolean,
        senderAccountNumber: String,
        bankCode: String)
    : GatewayChargeRequest(
        publicKey,
        countryCode,
        currency,
        amount,
        reference,
        Account,
        tokenize,
        senderAccountNumber = senderAccountNumber,
        bankCode = bankCode
    )

data class GatewayChargeResponse(
        val amount: String,
        val country: String,
        val currency: String,
        val reference: String,
        val token: String
)