package com.fast.common.utils.updater;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;

import java.io.File;

public class ApkInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            long localDownloadId = UpdaterUtils.getLocalDownloadId(context);
            if (downloadApkId == localDownloadId) {
                Logger.get().d("download complete. downloadId is " + downloadApkId);
                installApk(context, downloadApkId);
            }
        } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
            //处理 如果还未完成下载，用户点击Notification
            Intent viewDownloadIntent = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            viewDownloadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(viewDownloadIntent);
        }
    }

//    private static void installApk(Context context, long downloadApkId) {
//
//        Intent install = new Intent(Intent.ACTION_VIEW);
//        DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
//        if (downloadFileUri != null) {
//            Logger.get().d("file location " + downloadFileUri.toString());
//            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
//            install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(install);
//        } else {
//            Logger.get().d("download failed");
//        }
//    }

    // 安装Apk
    private void installApk(Context context, long downloadApkId) {
        LogUtils.e("升级",context.getPackageName() + ".provider");
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                File apkFile = context.getExternalFilesDir("Download/update.apk");
                //对Android 版本判断
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // context.getPackageName() + ".fileprovider"  是配置中的authorities
                    //Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", apkFile);
                    Uri contentUri = FileProvider.getUriForFile(context, AppUtils.getAppPackageName()+".fileprovider", apkFile);
                    install.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                }
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                //Toasts.show(context,"下载失败");
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }

    }
}
