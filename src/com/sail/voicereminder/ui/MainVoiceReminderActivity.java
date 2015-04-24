package com.sail.voicereminder.ui;

import com.sail.voicereminder.R;
import com.sail.voicereminder.adapter.MyRecordAdapter;
import com.sail.voicereminder.db.VoiceRemindRecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;


public class MainVoiceReminderActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemSelectedListener {

    private Spinner spinnerClassify;
    private ImageView ivSearch;
    private ImageView ivAddNewReminder;
    private ListView lvRecord;
    private MyRecordAdapter myRecordAdapter;
    
    private void findViews() {
        spinnerClassify = (Spinner)findViewById( R.id.spinnerClassify );
        ivSearch = (ImageView)findViewById( R.id.iv_search );
        ivAddNewReminder = (ImageView)findViewById( R.id.iv_add_new_reminder );
        lvRecord = (ListView)findViewById( R.id.listView1 );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_voice_reminder);
        
        findViews();
        //设置按钮监听
        ivAddNewReminder.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        
        spinnerClassify.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v == ivAddNewReminder){
            Intent addIntent = new Intent(MainVoiceReminderActivity.this, AddNewReminderActivity.class);
            startActivity(addIntent);
            finish();
        }else if (v == ivSearch) {
            Intent addIntent = new Intent(MainVoiceReminderActivity.this, SearchReminderActivity.class);
            startActivity(addIntent);
            finish();
        }
    } 

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VoiceRemindRecord vrr = myRecordAdapter.getRecord(position);
        Intent modIntent = new Intent(MainVoiceReminderActivity.this, ModifyReminderActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("record", vrr);
        modIntent.putExtras(mBundle);
        startActivity(modIntent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String classifyString = parent.getItemAtPosition(position).toString();
        //设置 listview 适配器
        myRecordAdapter = new MyRecordAdapter(this, classifyString);
        lvRecord.setAdapter(myRecordAdapter);
        //设置 item 监听
        lvRecord.setOnItemClickListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }

}
