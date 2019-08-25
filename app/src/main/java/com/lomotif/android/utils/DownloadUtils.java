package com.lomotif.android.utils;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.lomotif.android.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by Teck Ken on 12-Sep-17.
 */

public class DownloadUtils {
    private Context mContext;
    private DownloadManager downloadManager;
    private String url;
    private String title;
    private String description;
    private long downloadReference;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0));
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                assert manager != null;
                Cursor cursor = manager.query(query);
                if (cursor.moveToFirst()) {
                    if (cursor.getCount() > 0) {
                        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            if (downloadReference == referenceId) {
                                checkStatus(referenceId);
                            }
                        }
                    }
                }
            }
        }
    };

    public DownloadUtils(Context mContext, DownloadManager downloadManager, String url, String title, String description) {
        this.mContext = mContext;
        this.downloadManager = downloadManager;
        this.url = url;
        this.title = title;
        this.description = description;
        mContext.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void unRegisteredReceiver(){
        mContext.unregisterReceiver(broadcastReceiver);
    }

    public void initialized() {
        new TedPermission(mContext)
                .setDeniedMessage(mContext.getString(R.string.permission_file_storage_denied_msg))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setGotoSettingButton(true)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        downloadFile(url, title, description);
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                }).check();
    }

    /**
     * download the file when user click on the download icon
     *
     * @return download reference id
     */
    private long downloadFile(String url, String title, String description) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription(description);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }

    /**
     * cancel the download
     *
     * @param downloadReference is the download reference id, it can get from the hashmap of the list
     */
    private void cancelDownload(long downloadReference) {
        downloadManager.remove(downloadReference);
        System.out.println("Canceled");
    }

    /**
     * this part i copy from the internet, it can cater most of the download status
     *
     * @param downloadReferenceId is the id that from download manager
     */
    private void checkStatus(long downloadReferenceId) {
        DownloadManager.Query myDownloadQuery = new DownloadManager.Query();
        //set the query filter to our previously Enqueued download
        myDownloadQuery.setFilterById(downloadReferenceId);

        //Query the download manager about downloads that have been requested.
        Cursor cursor = downloadManager.query(myDownloadQuery);
        //column for status
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            //column for reason code if the download failed or paused
            int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = cursor.getInt(columnReason);
            //get the download filename
            int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
            String fileUri = cursor.getString(filenameIndex);

            int fileMime = cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE);
            String type = cursor.getString(fileMime);
            System.out.println("testing type ---->" + type);

            String statusText = "";
            String reasonText = "";

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    statusText = "STATUS_FAILED";
                    switch (reason) {
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            reasonText = "ERROR_CANNOT_RESUME";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            reasonText = "ERROR_DEVICE_NOT_FOUND";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            reasonText = "ERROR_FILE_ALREADY_EXISTS";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            reasonText = "ERROR_FILE_ERROR";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            reasonText = "ERROR_HTTP_DATA_ERROR";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            reasonText = "ERROR_INSUFFICIENT_SPACE";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            reasonText = "ERROR_TOO_MANY_REDIRECTS";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            reasonText = "ERROR_UNKNOWN";
                            break;
                    }
                    break;
                case DownloadManager.STATUS_PAUSED:
                    statusText = "STATUS_PAUSED";
                    switch (reason) {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            reasonText = "PAUSED_QUEUED_FOR_WIFI";
                            break;
                        case DownloadManager.PAUSED_UNKNOWN:
                            reasonText = "PAUSED_UNKNOWN";
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            reasonText = "PAUSED_WAITING_FOR_NETWORK";
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            reasonText = "PAUSED_WAITING_TO_RETRY";
                            break;
                    }
                    break;
                case DownloadManager.STATUS_PENDING:
                    statusText = "STATUS_PENDING";
                    break;
                case DownloadManager.STATUS_RUNNING:
                    statusText = "STATUS_RUNNING";
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    statusText = "STATUS_SUCCESSFUL";
                    reasonText = "Filename:\n" + fileUri;
                    break;
            }

            //if the status is success then show the push notification
            //else it will show toast to let user know download failed
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                try {
                    downloadManager.openDownloadedFile(downloadReferenceId);
                    // Show selected color in a toast message
                    Toast.makeText(
                            mContext,
                            "Download Completed", Toast.LENGTH_SHORT
                    ).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(mContext, statusText + "\n" + reasonText,Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(mContext, statusText + "\n" + reasonText,Toast.LENGTH_SHORT).show();
        }
    }
}