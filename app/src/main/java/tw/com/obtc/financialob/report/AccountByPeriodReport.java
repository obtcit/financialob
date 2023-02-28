package tw.com.obtc.financialob.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.db.MyEntityManager;
import tw.com.obtc.financialob.db.DatabaseHelper.TransactionColumns;
import tw.com.obtc.financialob.graph.Report2DChart;
import tw.com.obtc.financialob.model.Account;
import tw.com.obtc.financialob.model.Currency;

import android.content.Context;

/**
 * 2D Chart Report to display monthly account results.
 *
 * @author Abdsandryk
 */
public class AccountByPeriodReport extends Report2DChart {

    public AccountByPeriodReport(Context context, MyEntityManager em, Calendar startPeriod, int periodLength, Currency currency) {
        super(context, em, startPeriod, periodLength, currency);
    }

    /* (non-Javadoc)
     * @see tw.com.obtc.financialob.graph.ReportGraphic2D#getFilterName()
     */
    @Override
    public String getFilterName() {
        if (filterIds.size() > 0) {
            long accountId = filterIds.get(currentFilterOrder);
            Account a = em.getAccount(accountId);
            if (a != null) {
                return a.title;
            } else {
                return context.getString(R.string.no_account);
            }
        } else {
            // no category
            return context.getString(R.string.no_account);
        }
    }

    /* (non-Javadoc)
     * @see tw.com.obtc.financialob.graph.ReportGraphic2D#setFilterIds()
     */
    @Override
    public void setFilterIds() {
        filterIds = new ArrayList<Long>();
        currentFilterOrder = 0;
        List<Account> accounts = em.getAllAccountsList();
        if (accounts.size() > 0) {
            Account a;
            for (int i = 0; i < accounts.size(); i++) {
                a = accounts.get(i);
                filterIds.add(a.id);
            }
        }
    }

    @Override
    protected void setColumnFilter() {
        columnFilter = TransactionColumns.from_account_id.name();
    }

    @Override
    public String getNoFilterMessage(Context context) {
        return context.getString(R.string.report_no_account);
    }

}
