package com.notificationdemo;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.ScrollView;

import java.io.File;

/**
 * Created by Administrator on 2016/12/15.
 */

public class UpdateService extends Service {


    private DownloadCompleteReceiver receiver;


    public UpdateService() {

    }

    /**
     * 安卓系统下载类
     **/
    private DownloadManager manager;

    /**
     * 初始化下载器
     **/
    private void initDownManager(String url) {
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(
                Uri.parse(url));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 下载时，通知栏显示途中
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
//            down.setTitle("下载");
//            down.setDescription("更新");

        }

        // 显示下载界面
        down.setVisibleInDownloadsUi(true);


        // 设置下载后文件存放的位置
        down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "leshi.apk");

        File file = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File dirFile = new File(file, "leshi.apk");
        if (dirFile.exists()) {
            dirFile.delete();

        }

        // 将下载请求放入队列
        manager.enqueue(down);

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 调用下载

        String url = intent.getStringExtra("url");
        initDownManager(url);

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销下载广播
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                //自动安装apk
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    Uri uriForDownloadedFile = manager.getUriForDownloadedFile(downId);
                    Log.d("kodulf", "uri=" + uriForDownloadedFile);
                    installApkNew(uriForDownloadedFile);
                }
            }
            //停止服务并关闭广播
            UpdateService.this.stopSelf();
        }

        //安装apk
        protected void installApkNew(Uri uri) {
            Intent intent = new Intent();
            //执行动作
            intent.setAction(Intent.ACTION_VIEW);
            //执行的数据类型
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            //不加下面这句话是可以的，查考的里面说如果不加上这句的话在apk安装完成之后点击单开会崩溃
            // android.os.Process.killProcess(android.os.Process.myPid());
            startActivity(intent);
        }
    }
}
