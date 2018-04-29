package com.operapay.gateway

import com.operapay.core.*
import com.operapay.gateway.models.*

/**
 * Gateway class for making requests against the API gateway.
 *
 * @author Perfect <perfectm@opay.team>
 */
class Gateway(private val rm: RequestManager = DefaultRequestManager()) {
    companion object {
        private const val PATH_GATEWAY_CREATE = "/gateway/create"
        private const val PATH_GATEWAY_COMMIT = "/gateway/commit"
        private const val PATH_GATEWAY_STATUS = "/gateway/status"
        private const val PATH_GATEWAY_3DSECURE = "/gateway/3dsecure"
        private const val PATH_GATEWAY_INPUTOTP = "/gateway/input-otp"
        private const val PATH_GATEWAY_INPUTPIN = "/gateway/input-pin"
    }

    fun create(request: GatewayChargeRequest, success: SuccessCallback<GatewayChargeResponse>, error: ErrorCallback) {
        rm.postREST(
            PATH_GATEWAY_CREATE,
            WithInput(request),
            parseResponse("gatewayCreate", GatewayChargeResponse::class, success),
            error
        )
    }

    fun commit(request: GatewayCommitResponse, success: SuccessCallback<GatewayCommitResponse>, error: ErrorCallback) {
        rm.postREST(
            PATH_GATEWAY_COMMIT,
            WithInput(request),
            parseResponse("gatewayCommit", GatewayCommitResponse::class, success),
            error
        )
    }

    fun status(request: GatewayStatusRequest, success: SuccessCallback<GatewayStatusResponse>, error: ErrorCallback) {
        rm.postREST(
            PATH_GATEWAY_STATUS,
            request,
            parseResponse("gatewayStatus", GatewayStatusResponse::class, success),
            error
        )
    }

    fun threeDSecure(request: Gateway3DSecureRequest, success: SuccessCallback<Gateway3DSecureResponse>, error: ErrorCallback) {
        rm.postREST(
            PATH_GATEWAY_3DSECURE,
            request,
            parseResponse("gateway3DSecure", Gateway3DSecureResponse::class, success),
            error
        )
    }

    fun inputOTP(request: GatewayInputOTPRequest, success: SuccessCallback<GatewayInputOTPResponse>, error: ErrorCallback) {
        rm.postREST(
            PATH_GATEWAY_INPUTOTP,
            request,
            parseResponse("gatewayInputOTP", GatewayInputOTPResponse::class, success),
            error
        )
    }

    fun inputPIN(request: GatewayInputPINRequest, success: SuccessCallback<GatewayInputPINResponse>, error: ErrorCallback) {
        rm.postREST(
                PATH_GATEWAY_INPUTPIN,
                request,
                parseResponse("gatewayInputPIN", GatewayInputPINResponse::class, success),
                error
        )
    }
}