/*
 * Copyright (c) 2014 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package tw.com.obtc.financialob.export.dropbox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.backup.DatabaseImport;
import tw.com.obtc.financialob.db.DatabaseAdapter;
import tw.com.obtc.financialob.export.ImportExportAsyncTask;

/**
 * Created by IntelliJ IDEA.
 * User: Denis Solonenko
 * Date: 11/9/11 2:16 AM
 */
public class DropboxRestoreTask extends ImportExportAsyncTask {

    private final String backupFile;

    public DropboxRestoreTask(final Activity activity, ProgressDialog dialog, String backupFile) {
        super(activity, dialog);
        this.backupFile = backupFile;
    }

    @Override
    protected Object work(Context context, DatabaseAdapter db, String... params) throws Exception {
        Dropbox dropbox = new Dropbox(context);
        DatabaseImport.createFromDropboxBackup(context, db, dropbox, backupFile).importDatabase();
        return true;
    }

    @Override
    protected String getSuccessMessage(Object result) {
        return context.getString(R.string.restore_database_success);
    }

}
