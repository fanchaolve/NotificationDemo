package com.notificationdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);


    }

    public void update(View view) {

        Intent intent=new Intent(this, UpdateService.class);
        intent.putExtra("url","http://d.kanzhun.com/boss/5.2/app-c86-release.apk");
        startService(intent);

    }
}
