package com.nuntteuniachim.otm.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

// 어플 진입시 로딩페이지

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(1500); //1.5초 동안 sleep 후 MainActivity로 이동
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
