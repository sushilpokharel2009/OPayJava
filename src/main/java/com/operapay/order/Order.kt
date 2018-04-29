package com.operapay.order

import com.operapay.core.*
import com.operapay.order.models.OrderRequest
import com.operapay.order.models.OrderResponse



/**
 *
 * @author Perfect <perfectm@opay.team>
 */
class Order(private val rm: RequestManager = DefaultRequestManager()) {

    fun submit(order: OrderRequest, success: SuccessCallback<OrderResponse>, error: ErrorCallback) {

        val query = "mutation { order( orderConfig: { " +
                onlyIfExists("orderId", order.orderId, true) +
                onlyIfExists("countryCode", order.countryCode) +
                onlyIfExists("currencyISO", order.currencyISO) +
                onlyIfExists("paymentAmount", order.paymentAmount) +
                onlyIfExists("serviceType", order.serviceType.value, noQuotes = true) +
                onlyIfExists("paymentType", order.paymentType.value) +
                onlyIfExists("savedInstrumentId", order.savedInstrumentId) +
                onlyIfExists("recipientPhoneNumber", order.recipientPhoneNumber) +
                onlyIfExists("recipientAccount", order.recipientAccount) +
                onlyIfExists("recipientBankCode", order.recipientBankCode) +
                onlyIfExists("customerPhone", order.customerPhone) +
                onlyIfExists("customerEmail", order.customerEmail) +
                onlyIfExists("callbackUrl", order.callbackUrl) +
                " } ) }"

        rm.postGraphQL(
            query,
            parseResponse("order", OrderResponse::class, success),
            error
        )
    }

    // quick helper for the postGraphQL query
    private fun onlyIfExists(key: String, value: String?, isFirst: Boolean = false, noQuotes: Boolean = false): String {
        val leadingChar = if (isFirst) "" else ","
        val quotes = if (noQuotes) "" else "\""
        return if (value != null && value.isNotBlank()) "$leadingChar $key: $quotes$value$quotes" else ""
    }
}