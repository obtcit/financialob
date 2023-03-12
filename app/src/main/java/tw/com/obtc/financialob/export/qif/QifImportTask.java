/*
 * Copyright (c) 2011 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package tw.com.obtc.financialob.export.qif;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.db.DatabaseAdapter;
import tw.com.obtc.financialob.export.ImportExportAsyncTask;
import tw.com.obtc.financialob.export.ImportExportException;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 11/7/11 10:45 PM
 */
public class QifImportTask extends ImportExportAsyncTask {

    private final QifImportOptions options;

    public QifImportTask(final Activity activity, ProgressDialog dialog, QifImportOptions options) {
        super(activity, dialog);
        this.options = options;
    }

    @Override
    protected Object work(Context context, DatabaseAdapter db, String... params) throws Exception {
        try {
            QifImport qifImport = new QifImport(context, db, options);
            qifImport.importDatabase();
            return null;
        } catch (Exception e) {
            Log.e("financialob", "Qif import error", e);
            throw new ImportExportException(R.string.qif_import_error);
        }
    }

    @Override
    protected String getSuccessMessage(Object result) {
        return context.getString(R.string.qif_import_success);
    }

}
