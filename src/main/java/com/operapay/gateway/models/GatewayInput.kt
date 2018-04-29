package com.operapay.gateway.models

/**
 *
 * @author Perfect <perfectm@opay.team>
 */
data class GatewayInputPINRequest(val token: String, val pin: String)

data class GatewayInputPINResponse(val status: String, val token: String, val reference: String)

data class GatewayInputOTPRequest(val token: String, val otp: String)

data class GatewayInputOTPResponse(val status: String, val token: String, val reference: String)