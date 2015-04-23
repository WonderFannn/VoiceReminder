package com.sail.voicereminder.alarm;

import com.sail.voicereminder.db.VoiceRemindRecord;
import com.sail.voicereminder.ui.AlarmDialogActivity;
import com.sail.voicereminder.ui.MainVoiceReminderActivity;
import com.sail.voicereminder.ui.ModifyReminderActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class MyAlarmReceiver extends BroadcastReceiver {

    private VoiceRemindRecord record;
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "闹钟时间到", Toast.LENGTH_LONG).show();  
        record = (VoiceRemindRecord) intent.getParcelableExtra("record");
        Intent modIntent = new Intent(context, AlarmDialogActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("record", record);
        modIntent.putExtras(mBundle);
        
        modIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        context.startActivity(modIntent);
    }

}
