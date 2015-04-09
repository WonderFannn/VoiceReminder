package com.sail.voicereminder.ui;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.sail.voicereminder.R;
import com.sail.voicereminder.adapter.FindRecordAdapter;
import com.sail.voicereminder.db.VoiceRemindRecord;
import com.sail.voicereminder.util.JsonParser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchReminderActivity extends Activity implements OnClickListener, OnItemClickListener {
    
    private EditText etSearchKey;
    private ImageView ivVoiceSearch;
    private ImageView ivSearch;
    private TextView tvPrompt;
    private ListView listView1;
    private ImageView ivBack;
    
    private RecognizerDialog iatDialog;
    private RecognizerDialogListener recognizerDialogListener;
    private InitListener mInitListener;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();// 用HashMap存储听写结果
    
    private FindRecordAdapter findRecordAdapter;

    private void initViews() {
        etSearchKey = (EditText)findViewById( R.id.et_searchKey );
        ivVoiceSearch = (ImageView)findViewById( R.id.iv_voiceSearch );
        ivSearch = (ImageView)findViewById( R.id.iv_search );
        tvPrompt = (TextView)findViewById( R.id.tv_prompt );
        listView1 = (ListView)findViewById( R.id.listView1 );
        ivBack = (ImageView) findViewById(R.id.iv_back);
        
        ivBack.setOnClickListener(this);
        ivVoiceSearch.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        listView1.setOnItemClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search_reminder);
        
        SpeechUtility.createUtility(getBaseContext(), SpeechConstant.APPID +"=5518ec36"); 
        mInitListener = new InitListener() {
            @Override
            public void onInit(int arg0) {
            }
        };
        recognizerDialogListener = new RecognizerDialogListener() {
            @Override
            public void onResult(RecognizerResult result, boolean arg1) {
                String condition = returnResult(result);
                setListView(condition);
            }
            
            @Override
            public void onError(SpeechError arg0) {
                
            }
        };
        initViews();
    }

    @Override
    public void onClick(View v) {
        if (v == ivSearch) {
            String condition = etSearchKey.getText().toString();
            setListView(condition);
        }else if (v == ivVoiceSearch) {
            iatDialog = new RecognizerDialog(this,mInitListener); 
            iatDialog.setParameter(SpeechConstant.DOMAIN, "iat"); 
            iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn"); 
            iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin ");
            iatDialog.setListener(recognizerDialogListener); 
            iatDialog.show();
        }else if (v == ivBack) {
            Intent mainIntent = new Intent(SearchReminderActivity.this,MainVoiceReminderActivity.class);
            startActivity(mainIntent);
            finish();
        }
        
    }
    
    private void setListView(String condition) {
        if(condition != null && condition != ""){
            findRecordAdapter = new FindRecordAdapter(getApplicationContext(), condition);
            if (findRecordAdapter.getCount() == 0) {
                tvPrompt.setText(condition + "相关的内容未找到");
            } else{
                listView1.setAdapter(findRecordAdapter);
                tvPrompt.setVisibility(View.GONE);
            }
        }else {
            tvPrompt.setText("请输入或者说出正确的搜索内容");
        }
    }

    private String returnResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        return resultBuffer.toString();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VoiceRemindRecord vrr = findRecordAdapter.getRecord(position);
        Intent modIntent = new Intent(SearchReminderActivity.this, ModifyReminderActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("record", vrr);
        modIntent.putExtras(mBundle);
        startActivity(modIntent);
        finish();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent mainIntent = new Intent(SearchReminderActivity.this,MainVoiceReminderActivity.class);
            startActivity(mainIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
        
    }
}
