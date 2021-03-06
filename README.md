# OPAY Java 
This is a library for easy integration of [OPay](https://operapay.com) API's with your Java Application.
Using this library, you can easily accept payment in your Java Application.

> It is worth nothing that the Library was actually implemented using Kotlin but has been developed to be Java friendly.

## Table of Content
- [Installation](#installation)
- [Usage](#usage)
    - [Setting the Environment Configuration](#setting-the-environment-configuration)
    - 1 . [Charging Card for Payment](#1-charging-card-for-payment)
    - 2 . [Charging Bank Account for Payment](#2-charging-bank-account-for-payment)
    - 3 . [Performing Bank Account Name Lookup](#3-perform-bank-account-name-lookup)
    - 4 . [Performing Bank Disbursement](#4-performing-bank-disbursement)
- [Contribution](#contribution)
- [LICENSE](#license)

## Installation
Add the following lines to your app's build.gradle:

```groovy
repositories {
    jcenter()
}
 
dependencies {
    compile 'com.operapay:OpayJava:1.1.1'
}
```

## Usage
To have a better understanding of the OPay API and how to use this library, you should take a look at their 
API documentation.

### Setting the Environment Configuration
You can switch between the test and live environment by setting the boolean value on `OPay`
```java
import com.operapay.OPay;
 
// set this at a Global step before making any request using the library
OPay.setTestMode(true); // or false if testing live

```

### 1. Charging Card for Payment

To charge a card: First create a `GatewayChargeRequest` of type `GatewayCardChargeRequest`
passing in the required information and card details.

Then call `Gateway::create()` with the request object as shown below:

```java
import com.operapay.gateway.models.GatewayCardChargeRequest;
import com.operapay.gateway.models.GatewayChargeResponse;
import com.operapay.gateway.models.GatewayChargeRequest;
import com.operapay.gateway.Gateway;
import java.lang.Exception;

 
class App {
    
    public static void main(String[] args) {
        // create a Card Charge Request
        GatewayChargeRequest chargeRequest = new GatewayCardChargeRequest(
                    "public-key", // public-key
                    "NGN", // currency
                    "NG", // country
                    "100", // amount to charge
                    "random-unique-reference", // unique reference
                    false, // set to false for non-tokenize payment
                    "4539216396812806", // card number
                    "01", // card expiry month
                    "19", // card expiry year
                    "812" // card cvc
                );
        
        // Call create on Gateway
        Gateway gateway = new Gateway();
        gateway.create(chargeRequest, (GatewayChargeResponse chargeResponse) -> {
            System.out.println(chargeResponse.toString());
            /*
             *  amount="100"
                country="NG",
                currency="NGN,
                reference="random-unique-reference",
                token="token-generated-by-opay"
             */
         }, (Exception error) -> error.printStackTrace());
    }
}
```

Next, Call `Gateway::status()` with the token gotten from the `Gateway::create()` step to determine the status of your transaction 
as described in the code below:

```java
import com.operapay.gateway.models.GatewayStatusRequest;
import com.operapay.gateway.models.GatewayStatusResponse;
import com.operapay.gateway.models.GatewayStatus;
 
...
String token = chargeResponse.getToken();
 

GatewayStatusRequest statusRequest = new GatewayStatusRequest(token);
gateway.status(statusRequest, (GatewayStatusResponse statusResponse) -> {
    switch (statusResponse.getStatus()) {
        case GatewayStatus.StatusProcessing:
            // try calling gateway.status() again
            break;
        case GatewayStatus.Status3DSecure:
            // call gateway.threeDSecure() to get the 3d secure url
            break;
        case GatewayStatus.StatusInputPIN:
            // get users pin and send it using gateway.inputPIN()
            break;
        case GatewayStatus.StatusInputOTP:
            // get users OTP and send it using gateway.inputOTP()
            break;
        case GatewayStatus.StatusSuccessful:
            // transaction is successful Yay!. User would have been debited.
            break;
        case GatewayStatus.StatusFailed:
            // transaction failed for some reason.
            break;
    }
}, (Exception error) -> error.printStackTrace())

```

So keep checking status till you get a `StatusSuccessful` or `StatusFailed`.

> You can use `Gateway.commit()` to perform a commit step after calling `Gateway.create()`.

### 2. Charging Bank Account for Payment
This step is similar to the `1. Charging Card for Payment` step above. The only difference 
 is that instead of calling `gateway.create()` with a `GatewayCardChargeRequest`, you call it with 
 a `GatewayAccountChargeRequest` as shown below:
 
 ```java
 import com.operapay.gateway.models.GatewayAccountChargeRequest;
 import com.operapay.gateway.models.GatewayChargeResponse;
 import com.operapay.gateway.models.GatewayChargeRequest;
 import com.operapay.gateway.Gateway;
 import java.lang.Exception;
 
 ...
 
GatewayChargeRequest acccountChargeRequest = new GatewayAccountChargeRequest(
        "public-key",
        "NGN",
        "NG",
        "100",
        "random-unique-reference",
        false, // false for non-tokenized payments
        "1234567890", // account number
        "123" // bank Code
);

 
// Call create on Gateway
Gateway gateway = new Gateway();
gateway.create(acccountChargeRequest, (GatewayChargeResponse chargeResponse) -> {
    System.out.println(chargeResponse.toString());
    /*
     *  amount="100"
        country="NG",
        currency="NGN,
        reference="random-unique-reference",
        token="token-generated-by-opay"
     */
 }, (Exception error) -> error.printStackTrace());
```

Then you can continue with the step as specified in the `Charging Card for Payment` step above.

### 3. Perform Bank Account Name Lookup
To perform a Bank Account Name Lookup, you would use the `Lookup::bankAccount()` method as shown in the code below:

```java
import com.operapay.lookup.Lookup;
import com.operapay.lookup.models.BankAccountLookupRequest;
 
public class App {
 
    public void testBankAccount() {
        Lookup lookup = new Lookup();
        lookup.bankAccount(new BankAccountLookupRequest(
                "056", // bank Code
                "123456789", // account number
                "NG" // country code of bank
            ), 
            (String accountName) -> System.out.println(accountName), 
            Throwable::printStackTrace
        );
    }
}
```

### 4. Performing Bank Disbursement
This involves using their Order API and the `Order::submit()` method.

```java
import com.operapay.core.Authorizer;
import com.operapay.core.KeyPair;
import com.operapay.core.PaymentType;
import com.operapay.core.RequestManager;
import com.operapay.core.ServiceType;
import com.operapay.order.models.OrderRequest;

...
 
// Keypair is required to get accessToken for disbursement
// NOTE: keep your private-key safe :)
KeyPair keys = new KeyPair("public-key", "private-key");
Authorizer authorizer = new Authorizer();
 
// First you need to get an AccessToken
authorizer.getAccessToken(keys, accessToken -> {
    OrderRequest bankDisbursmentOrder = new OrderRequest.Builder(
            "orderId-1", // unique order id
            "NG", // country code
            "NGN", // currency  code
            "5000", // amount to disburse
            ServiceType.BANK, // for bank disbursement
            PaymentType.COINS // if disbursing from your merchant account balance
    )
            .setRecipientAccount("1234567890") // recipients bank Account
            .setRecipientBankCode("232150029") // recipients bank code
            .build();
 
    // Secondly, you submit order request
    Order order = new Order();
    order.submit(accessToken, bankDisbursmentOrder, orderId -> {
        System.out.println(orderId); // you can use this order id to query status of order
    }, Throwable::printStackTrace);
}, Throwable::printStackTrace);
```

Then you can now check for order status using the `Order::status()` methods.

```java
import com.operapay.order.models.OrderStatusResponse;
 
...
order.status(accessToken, orderId, (OrderStatusResponse orderStatus) -> {
    // check the status of order
    System.out.println(orderStatus);
}, Throwable::printStackTrace);
```

## Contribution
Your contribution would be more than welcome. 
Before making a contribution, please create an issue addressing the contribution and then you can 
submit an PR.

## LICENSE
MIT 🙂
