package tw.com.obtc.financialob.model.rates;

import tw.com.obtc.financialob.db.AbstractDbTest;
import tw.com.obtc.financialob.rates.ExchangeRate;
import tw.com.obtc.financialob.test.DateTime;

public abstract class AssertExchangeRate extends AbstractDbTest {

    public static void assertRate(DateTime date, double rate, ExchangeRate r) {
        assertEquals(rate, r.rate, 0.00001d);
        assertEquals(date.atMidnight().asLong(), r.date);
    }

}
