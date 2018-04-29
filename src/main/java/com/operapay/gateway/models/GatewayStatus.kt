@file:JvmName("GatewayStatus")

package com.operapay.gateway.models

/**
 *
 * @author Perfect <perfectm@opay.team>
 */


const val StatusProcessing = "processing"
const val Status3DSecure = "3dsecure"
const val StatusInputOTP = "input-otp"
const val StatusInputPIN = "input-pin"
const val StatusSuccessful = "successful"
const val StatusFailed = "failed"

data class GatewayStatusRequest(val token: String)

data class GatewayStatusResponse(val status: String, val token: String, val reference: String)