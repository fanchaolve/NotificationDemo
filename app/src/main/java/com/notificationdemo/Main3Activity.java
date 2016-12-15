package com.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Main3Activity extends AppCompatActivity {

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    private final static String TAG = Main3Activity.class.getSimpleName();

    private int mymsg;
    public int DEFAULT_NOTIFICATION_ID = 666;


    private MyService myService;

    private TextView tv_progresss;


    private MyBorderReceiver receiver;

    public class MyBorderReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            mymsg = intent.getIntExtra("msg", 2);
            //tv_progresss.setText("广播从服务端接收过来的消息是：" + mymsg);
            Message message = new Message();
            Bundle data = new Bundle();
            data.putInt("progress", mymsg);
            message.setData(data);
            message.what = 1;
            handler.sendMessage(message);

        }
    }

    /**
     * 发送最简单的通知,该通知的ID = 1
     */
    private void sendNotification() {
        //这里使用 NotificationCompat 而不是 Notification ,因为 Notification 需要 API 16 才能使用
        //NotificationCompat 存在于 V4 Support Library
        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.app_name)).setContentText("加油");


    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int progress = msg.getData().getInt("progress");
                    builder.setProgress(100,progress,false);
                    mNotificationManager.notify(DEFAULT_NOTIFICATION_ID,builder.build());
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        tv_progresss = (TextView) findViewById(R.id.tv_progresss);
        receiver = new MyBorderReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.android.myborder");
        this.registerReceiver(receiver, filter);
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        sendNotification();

    }

    @Override
    protected void onStart() {

        super.onStart();
        Intent intent = new Intent(Main3Activity.this, MyService.class);
        this.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        this.startService(intent);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            Log.i(TAG, "service have onServiceConnected!");
            MyService.MyBinder binder = (MyService.MyBinder) service;
            myService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "service have onServiceDisconnected!");

        }
    };
}
