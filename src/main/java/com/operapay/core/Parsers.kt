package com.operapay.core

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import kotlin.reflect.KClass

/**
 *
 * @author Perfect <perfectm@opay.team>
 */

/**
 * Returns a Callback that Parses standard API response based on Key to Object
 */
internal fun <I: Any> parseResponse(key: String, modelClass: KClass<I>, callback: SuccessCallback<I>): SuccessCallback<String> {
    return SuccessCallback { result: String ->
        with(Gson()) {
            val element = JsonParser().parse(result) as JsonObject
            callback.invoke(fromJson(element[key], modelClass.java))
        }
    }
}

/**
 *
 *
 */
internal fun parseError(response: Map<String, Any>): OpayError {
    return OpayError((response["errors"] as List<LinkedTreeMap<String, Any>>)
                    .map { it["message"] as String })
}