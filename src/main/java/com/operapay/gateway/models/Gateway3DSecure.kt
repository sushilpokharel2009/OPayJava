package com.operapay.gateway.models

/**
 *
 * @author Perfect <perfectm@opay.team>
 */
data class Gateway3DSecureRequest(val token: String)

/**
 * Response is the 3D Secure URL
 *
 */
typealias Gateway3DSecureResponse = String