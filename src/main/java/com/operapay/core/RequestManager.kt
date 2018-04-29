package com.operapay.core

import com.google.gson.Gson
import com.operapay.OPay
import okhttp3.*
import java.io.IOException


/**
 *
 * @author Perfect <perfectm@opay.team>
 */

/**
 * RequestManager handles sending request to the Opera Pay API.
 *
 * Should be used by many of the API classes.
 *
 */
interface RequestManager {
    /**
     * Make request of data through the REST API
     *
     */
    fun <T> postREST(path: String, data: Any, success: SuccessCallback<T>, error: ErrorCallback?)

    /**
     * Make request of data through graph api, use for API's that don't exist via REST
     *
     */
    fun <T> postGraphQL(data: String, success: SuccessCallback<T>, error: ErrorCallback?)
}

/**
 * Default implementation of a Request Manager.
 * Uses OKHttp underneath
 *
 */
internal class DefaultRequestManager: RequestManager {
    companion object {
//        const val TEST_BASE_URL = "https://bz-sandbox.opay-test.net"
        const val TEST_BASE_URL = "http://localhost:8080"
        const val BASE_URL = "https://operapay.com"
    }

    private val parser = Gson()
    private val client = OkHttpClient()

    private fun baseUrl() = when (OPay.testMode) {
        true -> TEST_BASE_URL
        else ->  BASE_URL
    }

    private fun api(path: String) = baseUrl() + "/api/$path"

    override fun <T> postREST(path: String, data: Any, success: SuccessCallback<T>, error: ErrorCallback?) {
        val jsonData = parser.toJson(data)
        val request = Request.Builder().url(api(path))
                .post(RequestBody.create(MediaType.parse("application/json"), jsonData))
                .build()

        submitRequest(request, error, success)
    }

    override fun <T> postGraphQL(data: String, success: SuccessCallback<T>, error: ErrorCallback?) {
        val jsonData = parser.toJson(mapOf("query" to data))
        println(jsonData)
        val request = Request.Builder().url(baseUrl() + "/graphql")
                .post(RequestBody.create(MediaType.parse("application/json"), jsonData))
                .header("Accept", "application/json")
                .build()
        submitRequest(request, error, success)
    }

    private fun <T> submitRequest(request: Request?, error: ErrorCallback?, success: SuccessCallback<T>) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                error?.invoke(e)
            }

            override fun onResponse(call: Call?, response: Response) {
                val responseBody = response.body()?.string()
                when {
                    response.isSuccessful -> {
                        val responseMap: Map<String, Any> = parser.fromJson(responseBody, AnyMapType)

                        if (responseMap.contains("errors")) {
                            error?.invoke(parseError(responseMap))
                        } else {
                            success.invoke(parser.toJson(responseMap["data"]) as T)
                        }
                    }
                    else -> {
                        error?.invoke(OpayError(responseBody ?: "Unknown error"))
                    }
                }
            }

        })
    }
}