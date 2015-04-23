package com.sail.voicereminder.ui;

import com.sail.voicereminder.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable(){    
            public void run() {    
                Intent mainIntent = new Intent(getApplicationContext(), MainVoiceReminderActivity.class);
                startActivity(mainIntent);
                finish();
            }    
         }, 2000);   
    }
}
