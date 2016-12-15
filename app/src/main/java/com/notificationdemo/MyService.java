package com.notificationdemo;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.*;

/**
 * Created by Administrator on 2016/12/15.
 */

public class MyService extends Service {

    private int m = 1;
    Timer timer=new Timer();
    private MyBinder binder = new MyBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        timer.schedule(task, new Date(), 500);
        return binder;
    }


    class MyBinder extends Binder {

        public MyService getService() {
            return MyService.this;

        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);



    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            sendMsg();
        }
    };

    private void sendMsg() {

        Intent intent = new Intent("com.android.myborder");
        if (m < 100) {
            m++;
        }
        intent.putExtra("msg", m);
        sendBroadcast(intent);
    }
}
