/*
 * Copyright (c) 2012 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package tw.com.obtc.financialob.activity;

import android.content.Intent;
import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.filter.WhereFilter;
import tw.com.obtc.financialob.db.BudgetsTotalCalculator;
import tw.com.obtc.financialob.model.Budget;
import tw.com.obtc.financialob.model.Total;

import java.util.List;

public class BudgetListTotalsDetailsActivity extends AbstractTotalsDetailsActivity  {

    private WhereFilter filter = WhereFilter.empty();
    private BudgetsTotalCalculator calculator;
    
    public BudgetListTotalsDetailsActivity() {
        super(R.string.budget_total_in_currency);
    }

    @Override
    protected void internalOnCreate() {
        Intent intent = getIntent();
        if (intent != null) {
            filter = WhereFilter.fromIntent(intent);
        }
    }

    @Override
    protected void prepareInBackground() {
        List<Budget> budgets = db.getAllBudgets(filter);
        calculator = new BudgetsTotalCalculator(db, budgets);
        calculator.updateBudgets(null);
    }

    protected Total getTotalInHomeCurrency() {
        return calculator.calculateTotalInHomeCurrency();
    }

    protected Total[] getTotals() {
        return calculator.calculateTotals();
    }

}
