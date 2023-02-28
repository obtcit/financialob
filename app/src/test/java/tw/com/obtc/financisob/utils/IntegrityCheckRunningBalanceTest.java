package tw.com.obtc.financialob.utils;

import org.junit.Test;

import tw.com.obtc.financialob.db.AbstractDbTest;
import tw.com.obtc.financialob.model.Account;
import tw.com.obtc.financialob.test.AccountBuilder;
import tw.com.obtc.financialob.test.TransactionBuilder;

public class IntegrityCheckRunningBalanceTest extends AbstractDbTest {

    Account a1;
    Account a2;
    IntegrityCheckRunningBalance integrity;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        a1 = AccountBuilder.createDefault(db);
        a2 = AccountBuilder.createDefault(db);
        integrity = new IntegrityCheckRunningBalance(getContext(), db);
    }

    @Test
    public void should_detect_that_running_balance_is_broken() {
        TransactionBuilder.withDb(db).account(a1).amount(1000).create();
        TransactionBuilder.withDb(db).account(a1).amount(2000).create();
        TransactionBuilder.withDb(db).account(a2).amount(-100).create();
        assertEquals(IntegrityCheck.Level.OK, integrity.check().level);

        breakRunningBalanceForAccount(a1);
        assertEquals(IntegrityCheck.Level.ERROR, integrity.check().level);

        db.rebuildRunningBalanceForAccount(a1);
        assertEquals(IntegrityCheck.Level.OK, integrity.check().level);

        breakRunningBalance();
        assertEquals(IntegrityCheck.Level.ERROR, integrity.check().level);

        db.rebuildRunningBalances();
        assertEquals(IntegrityCheck.Level.OK, integrity.check().level);
    }

    private void breakRunningBalanceForAccount(Account a) {
        db.db().execSQL("delete from running_balance where account_id=?", new String[]{String.valueOf(a.id)});
    }

    private void breakRunningBalance() {
        db.db().execSQL("delete from running_balance");
    }

}
