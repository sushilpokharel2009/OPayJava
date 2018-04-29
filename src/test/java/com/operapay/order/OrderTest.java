package com.operapay.order;

import com.operapay.core.Authorizer;
import com.operapay.core.KeyPair;
import com.operapay.core.PaymentType;
import com.operapay.core.RequestManager;
import com.operapay.core.ServiceType;
import com.operapay.order.models.OrderRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Perfect <root>@perfect.engineering>
 */
@RunWith(MockitoJUnitRunner.class)
public class OrderTest {

    private Order order;

    @Mock
    RequestManager mockRM;

    @Before
    public void setUp() {
        order = new Order(mockRM);
    }

    @Test
    public void testSubmit_Works() {
        KeyPair keys = new KeyPair("public-key", "private-key");
        Authorizer authorizer = new Authorizer();
        authorizer.getAccessToken(keys, accessToken -> {
            OrderRequest request = new OrderRequest.Builder(
                    "orderId-1",
                    "NG",
                    "NGN",
                    "5000",
                    ServiceType.BANK, // for bank disbursement
                    PaymentType.COINS // if paying from your merchant account balance
            )
                    .setRecipientAccount("1234567890")
                    .setRecipientBankCode("232150029")
                    .build();

            order.submit(accessToken, request, orderId -> {
                System.out.println(orderId);
            }, Throwable::printStackTrace);
        }, Throwable::printStackTrace);

    }

    @Test
    public void testStatus_Works() {
        KeyPair keys = new KeyPair("public-key", "private-key");
        Authorizer authorizer = new Authorizer();
        authorizer.getAccessToken(keys, accessToken -> {
            String randomOrderId = "5ae61a6087771940312fc642";

            order.status(accessToken, randomOrderId, System.out::println, Throwable::printStackTrace);
        }, Throwable::printStackTrace);
    }
}