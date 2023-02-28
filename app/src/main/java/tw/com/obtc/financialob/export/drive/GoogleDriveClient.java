/*
 * Copyright (c) 2011 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 */

package tw.com.obtc.financialob.export.drive;

import android.content.Context;
import android.util.Log;

//import com.dropbox.core.util.IOUtil;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.drive.Drive;
//import com.google.android.gms.drive.DriveApi;
//import com.google.android.gms.drive.DriveContents;
//import com.google.android.gms.drive.DriveFile;
//import com.google.android.gms.drive.DriveFolder;
//import com.google.android.gms.drive.DriveId;
//import com.google.android.gms.drive.Metadata;
//import com.google.android.gms.drive.MetadataBuffer;
//import com.google.android.gms.drive.MetadataChangeSet;
//import com.google.android.gms.drive.query.Filters;
//import com.google.android.gms.drive.query.Query;
//import com.google.android.gms.drive.query.SearchableField;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
//import org.apache.commons.io.IOUtils;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Collections;
import java.util.List;
//import java.util.concurrent.TimeUnit;

import tw.com.obtc.financialob.R;
import tw.com.obtc.financialob.backup.DatabaseExport;
import tw.com.obtc.financialob.backup.DatabaseImport;
import tw.com.obtc.financialob.bus.GreenRobotBus;
import tw.com.obtc.financialob.db.DatabaseAdapter;
//import tw.com.obtc.financialob.export.Export;
import tw.com.obtc.financialob.export.ImportExportException;
import tw.com.obtc.financialob.utils.MyPreferences;

import tw.com.obtc.financialob.export.drive.GoogleDriveClientV3.ConnectionResult;

@EBean(scope = EBean.Scope.Singleton)
public class GoogleDriveClient {

    private final Context context;

    @Bean
    GreenRobotBus bus;

    @Bean
    DatabaseAdapter db;

//    private GoogleApiClient googleApiClient;
    private GoogleDriveClientV3 googleDriveClient;

    GoogleDriveClient(Context context) {
        this.context = context.getApplicationContext();
    }

    @AfterInject
    public void init() {
        bus.register(this);
    }

    private ConnectionResult connect() throws ImportExportException {
        ConnectionResult result = ConnectionResult.Success.INSTANCE;
        if (googleDriveClient == null) {
            String googleDriveAccount = MyPreferences.getGoogleDriveAccount(context);
            if (googleDriveAccount == null) {
                throw new ImportExportException(R.string.google_drive_account_required);
            }
            Log.i("nio","google api client init start");
//            googleApiClient = new GoogleApiClient.Builder(context)
//                    .addApi(Drive.API)
//                    .addScope(Drive.SCOPE_FILE)
//                    .setAccountName(googleDriveAccount)
////                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
////                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
//                    .build();

            GoogleDriveClientV3 client = new GoogleDriveClientV3(context);
            result = client.connect(googleDriveAccount);
            if (result == ConnectionResult.Success.INSTANCE) {
                googleDriveClient = client;
            }
            Log.i("nio","google api client init end");
        }
//        return googleApiClient.blockingConnect(1, TimeUnit.MINUTES);
        return result;
    }

    public void disconnect() {
//        if (googleApiClient != null) {
//            googleApiClient.disconnect();
//        }
        googleDriveClient.disconnect();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void doBackup(DoDriveBackup event) {
//        DatabaseExport export = new DatabaseExport(context, db.db(), true);
        try {
//            String targetFolder = getDriveFolderName();
//            ConnectionResult connectionResult = connect();
//            if (connectionResult.isSuccess()) {
//                DriveFolder folder = getDriveFolder(targetFolder);
//                String fileName = export.generateFilename();
//                byte[] bytes = export.generateBackupBytes();
//                Status status = createFile(folder, fileName, bytes);
//                if (status.isSuccess()) {
//                    handleSuccess(fileName);
            DatabaseExport export = new DatabaseExport(context, db.db(), true);
            File file = export.exportToFile();
            ConnectionResult result = connect();
            if (result == ConnectionResult.Success.INSTANCE) {
                if (uploadFile(file)) {
                    handleSuccess(file.getName());
                } else {
//                    handleFailure(status);
                    handleFailure("upload fail");
                }
            } else {
//                handleConnectionResult(connectionResult);
                handleConnectionResult(result);
            }
        } catch (Exception e) {
            handleError(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void listFiles(DoDriveListFiles event) {
        try {
//            String targetFolder = getDriveFolderName();
            ConnectionResult connectionResult = connect();
//            if (connectionResult.isSuccess()) {
//                DriveFolder folder = getDriveFolder(targetFolder);
//                Query query = new Query.Builder()
//                        .addFilter(Filters.and(
//                                Filters.eq(SearchableField.MIME_TYPE, Export.BACKUP_MIME_TYPE),
//                                Filters.eq(SearchableField.TRASHED, false)
//                        ))
//                        .build();
//                DriveApi.MetadataBufferResult metadataBufferResult = folder.queryChildren(googleApiClient, query).await();
//                if (metadataBufferResult.getStatus().isSuccess()) {
//                    List<DriveFileInfo> driveFiles = fetchFiles(metadataBufferResult);
//                    handleSuccess(driveFiles);
//                } else {
//                    handleFailure(metadataBufferResult.getStatus());
//                }
            if (connectionResult == ConnectionResult.Success.INSTANCE) {
                handleSuccess(googleDriveClient.listFiles());
            } else {
                handleConnectionResult(connectionResult);
            }
        } catch (Exception e) {
            handleError(e);
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void doRestore(DoDriveRestore event) {
        try {
            ConnectionResult connectionResult = connect();
//            if (connectionResult.isSuccess()) {
//                DriveFile file = Drive.DriveApi.getFile(googleApiClient, event.selectedDriveFile.driveId);
//                DriveApi.DriveContentsResult contentsResult = file.open(googleApiClient, DriveFile.MODE_READ_ONLY, null).await();
//                if (contentsResult.getStatus().isSuccess()) {
//                    DriveContents contents = contentsResult.getDriveContents();
//                    try {
//                        DatabaseImport.createFromGoogleDriveBackup(context, db, contents).importDatabase();
//                        bus.post(new DriveRestoreSuccess());
//                    } finally {
//                        contents.discard(googleApiClient);
//                    }
//                } else {
//                    handleFailure(contentsResult.getStatus());
//                }
            if (connectionResult == ConnectionResult.Success.INSTANCE) {
                File cache = googleDriveClient.downloadFile(event.selectedDriveFile.driveId);
                if (cache == null) {
                    handleFailure("Failed on downloading file to cache");
                }
                FileInputStream stream = new FileInputStream(cache);
                DatabaseImport.createFromGoogleDriveBackup(context, db, stream).importDatabase();
                bus.post(new DriveRestoreSuccess());
            } else {
                handleConnectionResult(connectionResult);
            }
        } catch (Exception e) {
            handleError(e);
        }
    }

//    private List<DriveFileInfo> fetchFiles(DriveApi.MetadataBufferResult metadataBufferResult) {
//        List<DriveFileInfo> files = new ArrayList<DriveFileInfo>();
//        MetadataBuffer metadataBuffer = metadataBufferResult.getMetadataBuffer();
//        if (metadataBuffer == null) return files;
//        try {
//            for (Metadata metadata : metadataBuffer) {
//                if (metadata == null) continue;
//                String title = metadata.getTitle();
//                if (!title.endsWith(".backup")) continue;
//                files.add(new DriveFileInfo(metadata.getDriveId(), title, metadata.getCreatedDate()));
//            }
//        } finally {
//            metadataBuffer.close();
//        }
//        Collections.sort(files);
//        return files;
//    }

    private String getDriveFolderName() throws ImportExportException {
        String folder = MyPreferences.getBackupFolder(context);
        // check the backup folder registered on preferences
        if (folder == null || folder.equals("")) {
            throw new ImportExportException(R.string.gdocs_folder_not_configured);
        }
        return folder;
    }

//    private DriveFolder getDriveFolder(String targetFolder) throws IOException, ImportExportException {
//        DriveFolder folder = getOrCreateDriveFolder(targetFolder);
//        if (folder == null) {
//            throw new ImportExportException(R.string.gdocs_folder_not_found);
//        }
//        return folder;
//    }

//    private DriveFolder getOrCreateDriveFolder(String targetFolder) throws IOException {
//        Query query = new Query.Builder().addFilter(Filters.and(
//                Filters.eq(SearchableField.TRASHED, false),
//                Filters.eq(SearchableField.TITLE, targetFolder),
//                Filters.eq(SearchableField.MIME_TYPE, "application/vnd.google-apps.folder")
//        )).build();
//        DriveApi.MetadataBufferResult result = Drive.DriveApi.query(googleApiClient, query).await();
//        if (result.getStatus().isSuccess()) {
//            DriveId driveId = fetchDriveId(result);
//            if (driveId != null) {
//                return Drive.DriveApi.getFolder(googleApiClient, driveId);
//            }
//        }
//        return createDriveFolder(targetFolder);
//    }

//    private DriveFolder createDriveFolder(String targetFolder) {
//        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
//                .setTitle(targetFolder).build();
//        DriveFolder.DriveFolderResult result = Drive.DriveApi.getRootFolder(googleApiClient).createFolder(googleApiClient, changeSet).await();
//        if (result.getStatus().isSuccess()) {
//            return result.getDriveFolder();
//        } else {
//            return null;
//        }
//    }

//    private DriveId fetchDriveId(DriveApi.MetadataBufferResult result) {
//        MetadataBuffer buffer = result.getMetadataBuffer();
//        try {
//            for (Metadata metadata : buffer) {
//                if (metadata == null) continue;
//                return metadata.getDriveId();
//            }
//        } finally {
//            buffer.close();
//        }
//        return null;
//    }

//    private Status createFile(DriveFolder folder, String fileName, byte[] bytes) throws IOException {
//        MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
//                .setTitle(fileName)
//                .setMimeType(Export.BACKUP_MIME_TYPE).build();
//        // Create a file in the root folder
//        DriveApi.DriveContentsResult contentsResult = Drive.DriveApi.newDriveContents(googleApiClient).await();
//        Status contentsResultStatus = contentsResult.getStatus();
//        if (contentsResultStatus.isSuccess()) {
//            DriveContents contents = contentsResult.getDriveContents();
//            contents.getOutputStream().write(bytes);
//            DriveFolder.DriveFileResult fileResult = folder.createFile(googleApiClient, changeSet, contents).await();
//            return fileResult.getStatus();
//        } else {
//            return contentsResultStatus;
//        }
//    }

    private void handleConnectionResult(ConnectionResult connectionResult) {
        bus.post(new DriveConnectionFailed(connectionResult));
    }

    private void handleError(Exception e) {
        if (e instanceof ImportExportException) {
            ImportExportException importExportException = (ImportExportException) e;
            bus.post(new DriveBackupError(context.getString(importExportException.errorResId)));
        } else {
            Log.i("nio",e.getMessage());
            bus.post(new DriveBackupError(e.getMessage()));
        }
    }

//    private void handleFailure(Status status) {
//        bus.post(new DriveBackupFailure(status));
//    }
    private void handleFailure(String message) {
        bus.post(new DriveBackupError(message));
    }

    private void handleSuccess(String fileName) {
        bus.post(new DriveBackupSuccess(fileName));
    }

    private void handleSuccess(List<DriveFileInfo> files) {
//        bus.post(new DriveFileList(files));
        if (files == null || files.size() == 0) {
            bus.post(new DriveBackupError("No backup files"));
        } else {
            bus.post(new DriveFileList(files));
        }
    }


    public boolean uploadFile(File file) throws ImportExportException {
        try {
            String targetFolder = getDriveFolderName();
            ConnectionResult connectionResult = connect();
//            if (connectionResult.isSuccess()) {
//                DriveFolder folder = getDriveFolder(targetFolder);
//                InputStream is = new FileInputStream(file);
//                try {
//                    byte[] bytes = IOUtils.toByteArray(is);
//                    createFile(folder, file.getName(), bytes);
//                } finally {
//                    IOUtil.closeInput(is);
//                }
//            }
            if (connectionResult == ConnectionResult.Success.INSTANCE) {
                return googleDriveClient.uploadFile(targetFolder, file);
            }
        } catch (Exception e) {
            throw new ImportExportException(R.string.google_drive_connection_failed, e);
        }
        return false;
    }

    /*@Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }*/
}
