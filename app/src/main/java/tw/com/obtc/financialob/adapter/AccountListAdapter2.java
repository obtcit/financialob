/*******************************************************************************
 * Copyright (c) 2010 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 *     Denis Solonenko - initial API and implementation
 ******************************************************************************/
package tw.com.obtc.financialob.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.model.Account;
import tw.com.obtc.financialob.model.AccountType;
import tw.com.obtc.financialob.model.CardIssuer;
import tw.com.obtc.financialob.datetime.DateUtils;
import tw.com.obtc.financialob.model.ElectronicPaymentType;
import tw.com.obtc.financialob.utils.MyPreferences;
import tw.com.obtc.financialob.utils.Utils;
import tw.com.obtc.orb.EntityManager;

import java.text.DateFormat;
import java.util.Date;

public class AccountListAdapter2 extends ResourceCursorAdapter {

    private final Utils u;
    private DateFormat df;
    private boolean isShowAccountLastTransactionDate;

    public AccountListAdapter2(Context context, Cursor c) {
        super(context, R.layout.account_list_item, c);
        this.u = new Utils(context);
        this.df = DateUtils.getShortDateFormat(context);
        this.isShowAccountLastTransactionDate = MyPreferences.isShowAccountLastTransactionDate(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = super.newView(context, cursor, parent);
        return AccountListItemHolder.create(view);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Account a = EntityManager.loadFromCursor(cursor, Account.class);
        AccountListItemHolder v = (AccountListItemHolder) view.getTag();

        v.centerView.setText(a.title);

        AccountType type = AccountType.valueOf(a.type);
        if (type.isCard && a.cardIssuer != null) {
            CardIssuer cardIssuer = CardIssuer.valueOf(a.cardIssuer);
            v.iconView.setImageResource(cardIssuer.iconId);
        } else if (type.isElectronic && a.cardIssuer != null) {
            ElectronicPaymentType paymentType = ElectronicPaymentType.valueOf(a.cardIssuer);
            v.iconView.setImageResource(paymentType.iconId);
        } else {
            v.iconView.setImageResource(type.iconId);
        }
        if (a.isActive) {
            v.iconView.getDrawable().mutate().setAlpha(0xFF);
            v.iconOverView.setVisibility(View.INVISIBLE);
        } else {
            v.iconView.getDrawable().mutate().setAlpha(0x77);
            v.iconOverView.setVisibility(View.VISIBLE);
        }

        StringBuilder sb = new StringBuilder();
        if (!Utils.isEmpty(a.issuer)) {
            sb.append(a.issuer);
        }
        if (!Utils.isEmpty(a.number)) {
            sb.append(" #").append(a.number);
        }
        if (sb.length() == 0) {
            sb.append(context.getString(type.titleId));
        }
        v.topView.setText(sb.toString());

        long date = a.creationDate;
        if (isShowAccountLastTransactionDate && a.lastTransactionDate > 0) {
            date = a.lastTransactionDate;
        }
        v.bottomView.setText(df.format(new Date(date)));

        long amount = a.totalAmount;
        if (type == AccountType.CREDIT_CARD && a.limitAmount != 0) {
            long limitAmount = Math.abs(a.limitAmount);
            long balance = limitAmount + amount;
            long balancePercentage = 10000 * balance / limitAmount;
            u.setAmountText(v.rightView, a.currency, amount, false);
            u.setAmountText(v.rightCenterView, a.currency, balance, false);
            v.rightView.setVisibility(View.VISIBLE);
            v.progressBar.setMax(10000);
            v.progressBar.setProgress((int) balancePercentage);
            v.progressBar.setVisibility(View.VISIBLE);
        } else {
            u.setAmountText(v.rightCenterView, a.currency, amount, false);
            v.rightView.setVisibility(View.GONE);
            v.progressBar.setVisibility(View.GONE);
        }
    }

    private static class AccountListItemHolder {
        ImageView iconView;
        ImageView iconOverView;
        TextView topView;
        TextView centerView;
        TextView bottomView;
        TextView rightCenterView;
        TextView rightView;
        ProgressBar progressBar;

        public static View create(View view) {
            AccountListItemHolder v = new AccountListItemHolder();
            v.iconView = view.findViewById(R.id.icon);
            v.iconOverView = view.findViewById(R.id.active_icon);
            v.topView = view.findViewById(R.id.top);
            v.centerView = view.findViewById(R.id.center);
            v.bottomView = view.findViewById(R.id.bottom);
            v.rightCenterView = view.findViewById(R.id.right_center);
            v.rightView = view.findViewById(R.id.right);
            v.rightView.setVisibility(View.GONE);
            v.progressBar = view.findViewById(R.id.progress);
            v.progressBar.setVisibility(View.GONE);
            view.setTag(v);
            return view;
        }

    }


}
