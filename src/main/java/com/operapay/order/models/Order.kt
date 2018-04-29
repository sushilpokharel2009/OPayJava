package com.operapay.order.models

import com.google.gson.annotations.SerializedName
import com.operapay.core.CashbackType
import com.operapay.core.PaymentType
import com.operapay.core.ServiceType
import com.sun.org.apache.xpath.internal.operations.Bool
import java.lang.IllegalArgumentException

/**
 *
 * @author Perfect <perfectm@opay.team>
 */

/**
 *
 * @param orderId the merchant transaction ID, required
 * @param countryCode ISO ALPHA-2 country code, required
 * @param currencyISO ISO 4217 currency code, required
 * @param paymentAmount Payment amount, required
 * @param serviceType Service types, required, possible values: airtime, data, bank, electricity, tv, water, coins
 * @param paymentType Payment method, required, possible values: coins, token
 * @param savedInstrumentId required if paymentType is token
 * @param recipientPhoneNumber required if serviceType is airtime, must have internationalize prefix
 * @param recipientAccount Recipient account number, required for services except airtime
 * @param recipientBankCode Recipient bank code, required for bank serviceType, 3 digit string
 * @param customerPhone Customer phone number, optional
 * @param customerEmail Customer email, optional
 * @param callbackUrl Callback URL, optional, this URL will be posted with result data once the transaction is done
 */
data class OrderRequest(
        val orderId: String,
        val countryCode: String,
        val currencyISO: String,
        val paymentAmount: String,
        val serviceType: ServiceType,
        val paymentType: PaymentType,
        val savedInstrumentId: String? = null,
        val recipientPhoneNumber: String? = null,
        val recipientAccount: String? = null,
        val recipientBankCode: String? = null,
        val customerPhone: String? = null,
        val customerEmail: String? = null,
        val callbackUrl: String? = null
    ) {

    init {
        if (paymentType == PaymentType.TOKEN && savedInstrumentId == null) {
            throw IllegalArgumentException("'savedInstrumentId' is required for Token Payment orders")
        }

        if (serviceType == ServiceType.AIRTIME && recipientPhoneNumber?.isBlank() != false) {
            throw IllegalArgumentException("recipientPhoneNumber is required for Airtime Service orders")
        }

        if (serviceType != ServiceType.AIRTIME && recipientAccount?.isBlank() != false) {
            throw IllegalArgumentException("recipientAccount is required for none Airtime Service order")
        }

        if (serviceType == ServiceType.BANK && recipientBankCode?.isBlank() != false) {
            throw IllegalArgumentException("recipientBankCode is required for Bank Service orders")
        }
    }

    // Required for jvm compatibility
    class Builder constructor(
            private val orderId: String,
            private val countryCode: String,
            private val currencyISO: String,
            private val paymentAmount: String,
            private val serviceType: ServiceType,
            private val paymentType: PaymentType
    ) {

        private var savedInstrumentId: String? = null
        private var recipientPhoneNumber: String? = null
        private var recipientAccount: String? = null
        private var recipientBankCode: String? = null
        private var customerPhone: String? = null
        private var customerEmail: String? = null
        private var callbackUrl: String? = null

        fun setSavedInstrumentId(value: String) = apply { savedInstrumentId = value }
        fun setRecipientPhoneNumber(value: String) = apply { recipientPhoneNumber = value }
        fun setRecipientAccount(value: String) = apply { recipientAccount = value }
        fun setRecipientBankCode(value: String) = apply { recipientBankCode = value }
        fun setCustomerPhone(value: String) = apply { customerPhone = value }
        fun setCustomerEmail(value: String) = apply { customerEmail = value }
        fun setCallbackUrl(value: String) = apply { callbackUrl = value }

        fun build(): OrderRequest {
            return OrderRequest(
                    orderId,
                    countryCode,
                    currencyISO,
                    paymentAmount,
                    serviceType,
                    paymentType,
                    savedInstrumentId,
                    recipientPhoneNumber,
                    recipientAccount,
                    recipientBankCode,
                    customerPhone,
                    customerEmail,
                    callbackUrl
            )
        }
    }
}

typealias OrderResponse = String // order Id

data class OrderStatusResponse(
    val type: String,
    val failed: Boolean,
    val closed: Boolean,
    @SerializedName("failure_reason") val failureReason: String,
    @SerializedName("close_reason") val closeReason: String,
    val cashback: CashbackType
)