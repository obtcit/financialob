package tw.com.obtc.financialob.export;

import tw.com.obtc.financialob.db.AbstractDbTest;
import tw.com.obtc.financialob.model.Account;
import tw.com.obtc.financialob.model.AccountType;
import tw.com.obtc.financialob.model.Currency;
import tw.com.obtc.financialob.test.CurrencyBuilder;

import static org.junit.Assert.assertNotNull;

public abstract class AbstractImportExportTest extends AbstractDbTest {

    protected Account createFirstAccount() {
        Currency c = createCurrency("SGD");
        Account a = new Account();
        a.title = "My Cash Account";
        a.type = AccountType.CASH.name();
        a.currency = c;
        a.totalAmount = 0;
        a.sortOrder = 100;
        a.note = "AAA\nBBB:CCC";
        db.saveAccount(a);
        assertNotNull(db.load(Account.class, a.id));
        return a;
    }

    protected Account createSecondAccount() {
        Currency c = createCurrency("CZK");
        Account a = new Account();
        a.title = "My Bank Account";
        a.type = AccountType.BANK.name();
        a.currency = c;
        a.totalAmount = 0;
        a.sortOrder = 50;
        db.saveAccount(a);
        assertNotNull(db.load(Account.class, a.id));
        return a;
    }

    private Currency createCurrency(String currency) {
        Currency c = CurrencyBuilder.withDb(db)
                .title("Singapore Dollar")
                .name(currency)
                .separators("''", "'.'")
                .symbol("S$")
                .create();
        assertNotNull(db.load(Currency.class, c.id));
        return c;
    }

}
