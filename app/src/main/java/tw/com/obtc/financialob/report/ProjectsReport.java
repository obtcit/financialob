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
package tw.com.obtc.financialob.report;

import android.content.Context;
import tw.com.obtc.financialob.activity.BlotterActivity;
import tw.com.obtc.financialob.activity.SplitsBlotterActivity;
import tw.com.obtc.financialob.blotter.BlotterFilter;
import tw.com.obtc.financialob.filter.WhereFilter;
import tw.com.obtc.financialob.filter.Criteria;
import tw.com.obtc.financialob.db.DatabaseAdapter;
import tw.com.obtc.financialob.model.Currency;

import static tw.com.obtc.financialob.db.DatabaseHelper.V_REPORT_PROJECTS;

public class ProjectsReport extends Report {

	public ProjectsReport(Context context, Currency currency) {
		super(ReportType.BY_PROJECT, context, currency);
	}

	@Override
	public ReportData getReport(DatabaseAdapter db, WhereFilter filter) {
        cleanupFilter(filter);
		return queryReport(db, V_REPORT_PROJECTS, filter);
	}

	@Override
	public Criteria getCriteriaForId(DatabaseAdapter db, long id) {
		return Criteria.eq(BlotterFilter.PROJECT_ID, String.valueOf(id));
	}

    @Override
    protected Class<? extends BlotterActivity> getBlotterActivityClass() {
        return SplitsBlotterActivity.class;
    }

}
