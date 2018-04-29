package com.operapay.gateway;

import com.operapay.core.RequestManager;
import com.operapay.gateway.models.GatewayAccountChargeRequest;
import com.operapay.gateway.models.GatewayCardChargeRequest;
import com.operapay.gateway.models.GatewayChargeRequest;
import com.operapay.gateway.models.GatewayStatus;
import com.operapay.gateway.models.GatewayStatusResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Random;

/**
 *
 *
 * @author Perfect <perfectm@opay.team>
 */

@RunWith(MockitoJUnitRunner.class)
public class GatewayTest {

    private Gateway gateway;

    @Mock
    private RequestManager mockRequestManager;

    @Before
    public void setup() {
        gateway = new Gateway(mockRequestManager);
    }

    @Test
    public void test_Gateway_Create_Works() {
        GatewayChargeRequest request = new GatewayCardChargeRequest(
            "",
            "NGN",
            "NG",
            "100",
            "random-"+ new Random().nextDouble(),
            false,
            "4539216396812806",
            "01",
            "19",
            "812"
        );

//        when(mockRequestManager.postREST(anyString(), anyString(), any(), any())).then(new Answer<Object>() {
//            @Override
//            public Object answer(InvocationOnMock invocation) throws Throwable {
//
//            }
//        })

        gateway.create(request, result -> System.out.println(result.toString()), Throwable::printStackTrace);
    }

    @Test
    public void test_Gateway_Status_Works() {
        GatewayStatusResponse statusResponse = new GatewayStatusResponse("", "", "");
        switch (statusResponse.getStatus()) {
            case GatewayStatus.StatusProcessing:
                // try calling gateway.status() again
                break;
            case GatewayStatus.Status3DSecure:
                // call gateway.threeDSecure() to get the 3d secure url (Gateway3DSecureResponse)
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
    }

    @Test
    public void test_Gateway_Account_Charge_Works() {
        GatewayChargeRequest acccountChargeRequest = new GatewayAccountChargeRequest(
                "public-key",
                "NGN",
                "NG",
                "100",
                "random-unique-reference",
                false,
                "1234567890", // account number
                "123"// bank Code
        );
    }
}
