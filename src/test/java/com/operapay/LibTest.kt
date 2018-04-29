package com.operapay

import com.operapay.core.ErrorCallback
import com.operapay.core.SuccessCallback
import com.operapay.gateway.Gateway
import com.operapay.gateway.models.*
import org.junit.Test
import java.lang.Exception
import java.util.*

/**
 * More Or Less a functional test
 * You could also understand some usages of the library.
 *
 * For now just run the main method :)
 *
 * @author Perfect <perfectm@opay.team>
 */
class LibTest {

    class ErrorHandler: ErrorCallback {
        override fun invoke(error: Exception?) {
            error?.printStackTrace()
        }
    }

    @Test
    fun testRunMain() {
        OPay.testMode = false
        mainPayment()
    }

    fun mainPayment() {
        val errorHandler = ErrorHandler()
        val gateway = Gateway()

        val otpChargeRequest = GatewayCardChargeRequest(
                "public-key",
                "NGN",
                "NG",
                "100",
                "random-${Random().nextDouble()}",
                false,
                "4539216396812806",
                "01",
                "19",
                "812"
        )

        val pinChargeRequest = GatewayCardChargeRequest(
                "public-key",
                "NGN",
                "NG",
                "50",
                "random-pin-${Random().nextDouble()}",
                false,
                "5540183733002653",
                "09",
                "19",
                "789"
        )

        val secureChargeRequest = GatewayCardChargeRequest(
                "public-key",
                "NGN",
                "NG",
                "50",
                "random-secure-${Random().nextDouble()}",
                false,
                "5529280874911661",
                "09",
                "19",
                "789"
        )

        gateway.create(otpChargeRequest, SuccessCallback { result: GatewayChargeResponse ->
            println(result)
            checkStatus(result.token, gateway, errorHandler)
        }, errorHandler)

        gateway.create(pinChargeRequest, SuccessCallback { result: GatewayChargeResponse ->
            println(result)
            checkStatus(result.token, gateway, errorHandler)
        }, errorHandler)

        gateway.create(secureChargeRequest, SuccessCallback { result: GatewayChargeResponse ->
            println(result)
            checkStatus(result.token, gateway, errorHandler)
        }, errorHandler)
    }

    private fun checkStatus(token: String, gateway: Gateway, errorHandler: ErrorHandler) {
        val statusRequest = GatewayStatusRequest(token)

        gateway.status(statusRequest, SuccessCallback { status: GatewayStatusResponse ->
            println(status)
            when (status.status) {
                StatusInputOTP -> {
                    gateway.inputOTP(GatewayInputOTPRequest(token, "12345"), SuccessCallback { otpResponse ->
                        println(otpResponse)
                        checkStatus(token, gateway, errorHandler)
                    }, errorHandler)
                }
                StatusInputPIN -> {
                    gateway.inputPIN(GatewayInputPINRequest(token, "9890"), SuccessCallback { pinResponse ->
                        println(pinResponse)
                        checkStatus(token, gateway, errorHandler)
                    }, errorHandler)
                }
                Status3DSecure -> {
                    gateway.threeDSecure(Gateway3DSecureRequest(token), SuccessCallback { response ->
                        println(response)
//                    checkStatus(token, gateway, errorHandler)
                    }, errorHandler)
                }
                StatusProcessing -> checkStatus(token, gateway, errorHandler)
                StatusSuccessful -> {
                    println("Transaction is successful")
                }
            }
        }, errorHandler)
    }
}