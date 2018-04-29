package com.operapay.core

/**
 * Perform Authorization to get Access Codes and Access Token.
 *
 * @author Perfect <perfectm@opay.team>
 */
class Authorizer(private val rm: RequestManager = DefaultRequestManager()) {

    fun getAccessCode(publicKey: String, successCallback: SuccessCallback<AccessCode>, error: ErrorCallback) {
        rm.postGraphQL(
            "query { accessCode(publicKey: \"$publicKey\") }",
            parseResponse("accessCode", AccessCode::class, successCallback),
            error
        )
    }

    fun getAccessToken(accessCode: String, privateKey: String, successCallback: SuccessCallback<AccessToken>, error: ErrorCallback) {
        rm.postGraphQL(
            "query { accessToken(accessCode: \"$accessCode\", privateKey: \"$privateKey\") { expires_at value } }",
            parseResponse("accessToken", AccessToken::class, successCallback),
            error
        )
    }

    /**
     * Quick function to get AccessToken
     * It fetches the Access Code and Swaps it for the AccessToken
     *
     */
    fun getAccessToken(keys: KeyPair, successCallback: SuccessCallback<AccessToken>, error: ErrorCallback) {
        getAccessCode(
            keys.publicKey,
            SuccessCallback { accessCode ->
                getAccessToken(accessCode, keys.privateKey, successCallback, error)
            },
            error
        )
    }
}