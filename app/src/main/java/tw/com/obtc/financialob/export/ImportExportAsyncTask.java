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
package tw.com.obtc.financialob.export;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.bus.GreenRobotBus_;
import tw.com.obtc.financialob.bus.RefreshCurrentTab;
import tw.com.obtc.financialob.db.DatabaseAdapter;
import tw.com.obtc.financialob.utils.MyPreferences;

import static tw.com.obtc.financialob.export.Export.uploadBackupFileToDropbox;
import static tw.com.obtc.financialob.export.Export.uploadBackupFileToGoogleDrive;

public abstract class ImportExportAsyncTask extends AsyncTask<String, String, Object> {

    protected final Activity context;
    protected final ProgressDialog dialog;
    private boolean showResultMessage = true;

    private ImportExportAsyncTaskListener listener;

    public ImportExportAsyncTask(Activity context, ProgressDialog dialog) {
        this.dialog = dialog;
        this.context = context;
    }

    public void setListener(ImportExportAsyncTaskListener listener) {
        this.listener = listener;
    }

    public void setShowResultMessage(boolean showResultMessage) {
        this.showResultMessage = showResultMessage;
    }

    @Override
    protected Object doInBackground(String... params) {
        DatabaseAdapter db = new DatabaseAdapter(context);
        db.open();
        try {
            return work(context, db, params);
        } catch (Exception ex) {
            Log.e("financialob", "Unable to do import/export", ex);
            return ex;
        } finally {
            db.close();
        }
    }

    protected abstract Object work(Context context, DatabaseAdapter db, String... params) throws Exception;

    protected abstract String getSuccessMessage(Object result);

    protected void doUploadToDropbox(Context context, String backupFileName) throws Exception {
        if (MyPreferences.isDropboxUploadBackups(context)) {
            doForceUploadToDropbox(context, backupFileName);
        }
    }

    protected void doForceUploadToDropbox(Context context, String backupFileName) throws Exception {
        publishProgress(context.getString(R.string.dropbox_uploading_file));
        uploadBackupFileToDropbox(context, backupFileName);
    }

    void doUploadToGoogleDrive(Context context, String backupFileName) throws Exception {
        if (MyPreferences.isGoogleDriveUploadBackups(context)) {
            doForceUploadToGoogleDrive(context, backupFileName);
        }
    }

    private void doForceUploadToGoogleDrive(Context context, String backupFileName) throws Exception {
        publishProgress(context.getString(R.string.google_drive_uploading_file));
        uploadBackupFileToGoogleDrive(context, backupFileName);
    }


    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        dialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(Object result) {
        dialog.dismiss();

        if (result instanceof ImportExportException) {
            ImportExportException exception = (ImportExportException) result;
            StringBuilder sb = new StringBuilder();
            if (exception.formatArgs != null){
                sb.append(context.getString(exception.errorResId, exception.formatArgs));
            } else {
                sb.append(context.getString(exception.errorResId));
            }

            if (exception.cause != null) {
                sb.append(" : ").append(exception.cause);
            }
            new AlertDialog.Builder(context)
                    .setTitle(R.string.fail)
                    .setMessage(sb.toString())
                    .setPositiveButton(R.string.ok, null)
                    .show();
            return;
        }

        if (result instanceof Exception)
            return;

        String message = getSuccessMessage(result);

        refreshMainActivity();
        if (listener != null) {
            listener.onCompleted(result);
        }

        if (showResultMessage) {
            Toast.makeText(context, context.getString(R.string.success, message), Toast.LENGTH_LONG).show();
        }
    }

    private void refreshMainActivity() {
        GreenRobotBus_.getInstance_(context).post(new RefreshCurrentTab());
    }

}

