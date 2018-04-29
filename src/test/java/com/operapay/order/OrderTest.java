package com.operapay.order;

import com.operapay.OPay;
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
        OrderRequest request = new OrderRequest.Builder(
                "orderId-1",
                "NG",
                "NGN",
                "5000",
                ServiceType.BANK, // for bank disbursement
                PaymentType.COINS // if paying from your merchant account balance
            )
            .setRecipientAccount("1234567890")
            .setRecipientBankCode("053")
            .build();

        Order order = new Order();
        order.submit(request, result -> {}, error -> {});
    }
}