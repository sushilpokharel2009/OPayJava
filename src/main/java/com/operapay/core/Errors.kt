package com.operapay.core

import com.google.gson.Gson


/**
 *
 * @author Perfect <perfectm@opay.team>
 */

/**
 * Respresent any errors returned by the OPay API
 *
 */
class OpayError(errors: List<String>)
    : Exception(errors.joinToString()) {
    constructor(error: String): this(listOf(error))
}