package com.notificationdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private NotificationManager mNotificationManager;

    public int DEFAULT_NOTIFICATION_ID = 666;
    public static final String NOTIFICATION_TAG = "test";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);


        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                sendNotification();
                break;
            case R.id.btn2:
                sendNotificationWithTag();
                break;
            case R.id.btn3:
                sendNotificationPendint();
                break;
            case R.id.btn4:
                sendNotificationPendintTaskStack();
                break;
            case R.id.btn5:
               Intent intent=new Intent(this,Main4Activity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 发送最简单的通知,该通知的ID = 1
     */
    private void sendNotification() {
        //这里使用 NotificationCompat 而不是 Notification ,因为 Notification 需要 API 16 才能使用
        //NotificationCompat 存在于 V4 Support Library
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(getString(R.string.app_name)).setContentText("加油");
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, builder.build());

    }

    /**
     * 使用notify(String tag, int id, Notification notification)方法发送通知
     * 移除对应通知需使用 cancel(String tag, int id)
     */
    private void sendNotificationWithTag() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification With Tag")
                .setContentText("Hi,My id is 1,tag is " + NOTIFICATION_TAG);
        mNotificationManager.notify(NOTIFICATION_TAG, DEFAULT_NOTIFICATION_ID, builder.build());

    }


    private void sendNotificationPendint() {

        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, ResultActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification With Tag")
                .setContentText("Hi,My id is 1,tag is ").setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_SOUND);

        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID++, builder.build());
    }

    private void sendNotificationPendintTaskStack() {
        Intent resultIntent = new Intent(this, ResultActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // 添加返回栈
        stackBuilder.addParentStack(ResultActivity.class);
        // 添加Intent到栈顶
        stackBuilder.addNextIntent(resultIntent);
        // 创建包含返回栈的pendingIntent
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(resultPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification With Tag")
                .setContentText("Hi,My id is 1,tag is ").setDefaults(Notification.DEFAULT_SOUND);
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID++, builder.build());
    }

}
