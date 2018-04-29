package com.operapay.lookup;

import com.operapay.lookup.models.BankAccountLookupRequest;
import org.junit.Test;

/**
 *
 * @author Perfect <perfectm@opay.team>
 */
public class LookupTest {


    @Test
    public void testBankAccount() {
        Lookup lookup = new Lookup();
        lookup.bankAccount(new BankAccountLookupRequest(
                "056",
                "2100027901",
                "NG"
        ), System.out::println, Throwable::printStackTrace);
    }

}