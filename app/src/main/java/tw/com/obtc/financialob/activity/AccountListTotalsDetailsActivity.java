/*
 * Copyright (c) 2012 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package tw.com.obtc.financialob.activity;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.model.Total;

/**
 * Created by IntelliJ IDEA.
 * User: denis.solonenko
 * Date: 3/15/12 16:40 PM
 */
public class AccountListTotalsDetailsActivity extends AbstractTotalsDetailsActivity  {

    public AccountListTotalsDetailsActivity() {
        super(R.string.account_total_in_currency);
    }

    protected Total getTotalInHomeCurrency() {
        return db.getAccountsTotalInHomeCurrency();
    }

    protected Total[] getTotals() {
        return db.getAccountsTotal();
    }

}
