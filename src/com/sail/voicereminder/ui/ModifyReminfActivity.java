package com.sail.voicereminder.ui;

import com.sail.voicereminder.db.VoiceRemindRecord;

import android.app.Activity;
import android.os.Bundle;

public class ModifyReminfActivity extends Activity {
    VoiceRemindRecord record;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        
        record = getIntent().getParcelableExtra("record");
        
    }
}
