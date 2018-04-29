package com.operapay.gateway.models

/**
 *
 * @author Perfect <perfectm@opay.team>
 */
data class GatewayCommitRequest(
        val currency: String,
        val privateKey: String,
        val amount: String,
        val countryCode: String,
        val token: String
)

typealias GatewayCommitResponse = Boolean