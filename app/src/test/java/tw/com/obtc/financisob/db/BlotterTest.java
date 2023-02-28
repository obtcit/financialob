package tw.com.obtc.financialob.db;

import android.database.Cursor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import tw.com.obtc.financialob.blotter.BlotterFilter;
import tw.com.obtc.financialob.filter.WhereFilter;
import tw.com.obtc.financialob.model.Account;
import tw.com.obtc.financialob.model.Payee;
import tw.com.obtc.financialob.model.Transaction;
import tw.com.obtc.financialob.test.AccountBuilder;
import tw.com.obtc.financialob.test.DateTime;
import tw.com.obtc.financialob.test.TransactionBuilder;

import static org.junit.Assert.*;

public class BlotterTest extends AbstractDbTest {

    Account a1;

    DateTime dt = DateTime.fromTimestamp(System.currentTimeMillis());

    @Override
    public void setUp() throws Exception {
        super.setUp();
        a1 = AccountBuilder.createDefault(db);
    }

    @Test
    public void should_sort_transactions_in_the_blotter_with_the_same_datetime_according_to_filter() {
        //given
        Transaction t1 = TransactionBuilder.withDb(db).account(a1).amount(1000).dateTime(dt).create();
        Transaction t2 = TransactionBuilder.withDb(db).account(a1).amount(2000).dateTime(dt).create();
        Transaction t3 = TransactionBuilder.withDb(db).account(a1).amount(3000).dateTime(dt).create();
        //when sorted oldest-to-newest
        assertBlotter(getBlotter(WhereFilter.empty().asc(BlotterFilter.DATETIME)), t1, t2, t3);
        //when sorted newest-to-oldest
        assertBlotter(getBlotter(WhereFilter.empty().desc(BlotterFilter.DATETIME)), t3, t2, t1);
        //when sorted newest-to-oldest by default
        assertBlotter(getBlotter(WhereFilter.empty()), t3, t2, t1);
    }

    @Test
    public void should_filter_blotter_by_payee() {
        //given
        Transaction t1 = TransactionBuilder.withDb(db).account(a1).amount(1000).payee("P1").dateTime(dt).create();
        Transaction t2 = TransactionBuilder.withDb(db).account(a1).amount(2000).dateTime(dt).create();
        Payee p = db.findEntityByTitle(Payee.class, "P1");
        //then
        assertBlotter(getBlotter(WhereFilter.empty().eq(BlotterFilter.PAYEE_ID, String.valueOf(p.id))), t1);
        assertBlotter(getBlotter(WhereFilter.empty().isNull(BlotterFilter.PAYEE_ID)), t2);
    }

    private void assertBlotter(List<Transaction> blotter, Transaction... transactions) {
        assertEquals(transactions.length, blotter.size());
        for (int i = 0; i < transactions.length; i++) {
            assertEquals("Pos " + i, transactions[i].id, blotter.get(i).id);
        }
    }

    private List<Transaction> getBlotter(WhereFilter filter) {
        Cursor c = db.getBlotter(filter);
        try {
            List<Transaction> list = new ArrayList<Transaction>();
            while (c.moveToNext()) {
                list.add(Transaction.fromBlotterCursor(c));
            }
            return list;
        } finally {
            c.close();
        }
    }

}
