package tw.com.obtc.financialob.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tw.com.obtc.financialob.db.DatabaseAdapter;
import tw.com.obtc.financialob.model.Account;
import tw.com.obtc.financialob.model.Category;
import tw.com.obtc.financialob.model.Currency;
import tw.com.obtc.financialob.model.MyLocation;
import tw.com.obtc.financialob.model.Payee;
import tw.com.obtc.financialob.model.Project;
import tw.com.obtc.financialob.model.Transaction;
import tw.com.obtc.financialob.model.TransactionAttribute;
import tw.com.obtc.financialob.model.TransactionStatus;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 2/13/11 8:52 PM
 */
public class TransactionBuilder {

    private final DatabaseAdapter db;
    private final Transaction t = new Transaction();
    private List<TransactionAttribute> attributes;

    public static TransactionBuilder withDb(DatabaseAdapter db) {
        return new TransactionBuilder(db);
    }

    private TransactionBuilder(DatabaseAdapter db) {
        this.db = db;
        this.t.splits = new LinkedList<Transaction>();
    }

    public TransactionBuilder account(Account a) {
        t.fromAccountId = a.id;
        return this;
    }

    public TransactionBuilder amount(long amount) {
        t.fromAmount = amount;
        return this;
    }

    public TransactionBuilder originalAmount(Currency originalCurrency, long originalAmount) {
        t.originalCurrencyId = originalCurrency.id;
        t.originalFromAmount = originalAmount;
        return this;
    }

    public TransactionBuilder payee(String payee) {
        t.payeeId = db.findOrInsertEntityByTitle(Payee.class, payee).getId();
        return this;
    }

    public TransactionBuilder location(String location) {
        t.locationId = db.findOrInsertEntityByTitle(MyLocation.class, location).getId();
        return this;
    }

    public TransactionBuilder project(String project) {
        t.projectId = db.findOrInsertEntityByTitle(Project.class, project).getId();
        return this;
    }

    public TransactionBuilder note(String note) {
        t.note = note;
        return this;
    }

    public TransactionBuilder withStatus(TransactionStatus status) {
        t.status = status;
        return this;
    }

    public TransactionBuilder category(Category c) {
        t.categoryId = c.id;
        return this;
    }

    public TransactionBuilder ccPayment() {
        t.isCCardPayment = 1;
        return this;
    }

    public TransactionBuilder dateTime(DateTime dateTime) {
        t.dateTime = dateTime.asLong();
        return this;
    }

    public TransactionBuilder makeTemplate() {
        t.setAsTemplate();
        return this;
    }

    public TransactionBuilder scheduleOnce(DateTime dateTime) {
        t.dateTime = dateTime.asLong();
        t.setAsScheduled();
        return this;
    }

    public TransactionBuilder scheduleRecur(String pattern) {
        t.recurrence = pattern;
        t.setAsScheduled();
        return this;
    }

    public TransactionBuilder withSplit(Category category, long amount) {
        return withSplit(category, amount, null, null, null);
    }

    public TransactionBuilder withSplit(Category category, Project project, long amount) {
        return withSplit(category, amount, null, project, null);
    }

    public TransactionBuilder withSplit(Category category, long amount, String note) {
        return withSplit(category, amount, note, null, null);
    }

    public TransactionBuilder withSplit(Category category, long amount, String note, Project p, TransactionAttribute a) {
        Transaction split = new Transaction();
        split.categoryId = category.id;
        split.fromAmount = amount;
        split.note = note;
        if (p != null) {
            split.projectId = p.id;
        }
        if (a != null) {
            Map<Long, String> map = new HashMap<Long, String>();
            map.put(a.attributeId, a.value);
            split.categoryAttributes = map;
        }
        t.splits.add(split);
        t.categoryId = Category.SPLIT_CATEGORY_ID;
        return this;
    }

    public TransactionBuilder withTransferSplit(Account toAccount, long fromAmount, long toAmount) {
        return withTransferSplit(toAccount, fromAmount, toAmount, null);
    }

    public TransactionBuilder withTransferSplit(Account toAccount, long fromAmount, long toAmount, String note) {
        Transaction split = new Transaction();
        split.toAccountId = toAccount.id;
        split.fromAmount = fromAmount;
        split.toAmount = toAmount;
        split.note = note;
        t.splits.add(split);
        t.categoryId = Category.SPLIT_CATEGORY_ID;
        return this;
    }

    public TransactionBuilder withAttributes(TransactionAttribute...attributes) {
        this.attributes = Arrays.asList(attributes);
        return this;
    }

    public Transaction create() {
        t.id = db.insertOrUpdate(t, attributes);
        return t;
    }
}
