package tw.com.obtc.financialob.db;

import org.junit.Assert;
import org.junit.Test;

import tw.com.obtc.financialob.model.Account;
import tw.com.obtc.financialob.test.AccountBuilder;

public class AccountTest extends AbstractDbTest {

    @Test
    public void duplication_and_sort_order_ignoring() {
        Account a1 = AccountBuilder.createDefault(db);
        long dup1Id = db.duplicate(Account.class, a1.id);
        Account dup1 = db.getAccount(dup1Id);

        assertEquals(a1.title, dup1.title);
        assertEquals(a1.id + 1, dup1.id);
        Assert.assertFalse(db.updateEntitySortOrder(a1, 10));
    }

}
