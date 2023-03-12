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
package tw.com.obtc.financialob.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.adapter.BlotterListAdapter;
import tw.com.obtc.financialob.blotter.BlotterFilter;
import tw.com.obtc.financialob.filter.WhereFilter;
import tw.com.obtc.financialob.utils.MyPreferences;

public class TemplatesListActivity extends BlotterActivity {

    public TemplatesListActivity() {
    }

    public TemplatesListActivity(int layoutId) {
        super(layoutId);
    }

    @Override
    protected void calculateTotals() {
        // do nothing
    }

    @Override
    protected Cursor createCursor() {
        String sortOrder;

        switch (MyPreferences.getTemplatesSortOrder(getBaseContext())) {
            case NAME:
                sortOrder = BlotterFilter.SORT_BY_TEMPLATE_NAME;
                break;

            case ACCOUNT:
                sortOrder = BlotterFilter.SORY_BY_ACCOUNT_NAME;
                break;

            default:
                sortOrder = BlotterFilter.SORT_NEWER_TO_OLDER;
                break;
        }

        return db.getAllTemplates(blotterFilter, sortOrder);
    }

    @Override
    protected ListAdapter createAdapter(Cursor cursor) {
        return new BlotterListAdapter(this, db, cursor) {
            @Override
            protected boolean isShowRunningBalance() {
                return false;
            }
        };
    }

    @Override
    protected void internalOnCreate(Bundle savedInstanceState) {
        super.internalOnCreate(savedInstanceState);
        // remove filter button and totals
        bFilter.setVisibility(View.GONE);
        if (showAllBlotterButtons) {
            bTemplate.setVisibility(View.GONE);
        }
        findViewById(R.id.total).setVisibility(View.GONE);
        internalOnCreateTemplates();
    }

    @Override
    protected boolean addTemplateToAddButton() {
        return false;
    }

    protected void internalOnCreateTemplates() {
        // change empty list message
        ((TextView) findViewById(android.R.id.empty)).setText(R.string.no_templates);
        // fix filter
        blotterFilter = new WhereFilter("templates");
        blotterFilter.eq(BlotterFilter.IS_TEMPLATE, String.valueOf(1));
        blotterFilter.eq(BlotterFilter.PARENT_ID, String.valueOf(0));
    }

}
