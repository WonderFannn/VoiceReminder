package com.sail.voicereminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class MainVoiceReminderActivity extends Activity {

    private ImageView ivMenu;
    private LinearLayout llClassifySelect;
    private TextView tvClassify;
    private ImageView ivSearch;
    private ImageView ivAddNewReminder;
    private ListView listView1;

    private void findViews() {
        ivMenu = (ImageView)findViewById( R.id.iv_menu );
        llClassifySelect = (LinearLayout)findViewById( R.id.ll_classify_select );
        tvClassify = (TextView)findViewById( R.id.tv_classify );
        ivSearch = (ImageView)findViewById( R.id.iv_search );
        ivAddNewReminder = (ImageView)findViewById( R.id.iv_add_new_reminder );
        listView1 = (ListView)findViewById( R.id.listView1 );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_voice_reminder);
        
        findViews();
       
        ivAddNewReminder.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {

                Intent addIntent = new Intent(this,AddNewReminderActivity.class);
                startActivity(addIntent);
                
            }
        });
        
    }

}
