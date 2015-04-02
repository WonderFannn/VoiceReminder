package com.sail.voicereminder.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.sail.voicereminder.R;
import com.sail.voicereminder.util.JsonParser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

public class AddNewReminderActivity extends Activity implements OnClickListener{
      
    
    private EditText editTextAddTitle;
    private TextView textViewContent;
    private ImageView imageViewAddRecord;
    private TextView textViewTimeMin;
    private TextView textViewTimeSec;
    private ProgressBar progressBarAddRate;
    private ImageView imageViewAddStop;
    private Button buttonAddReturn;
    private Button buttonAddSave;
    
    private boolean recording = false;
    private int Time = 0;
    private Handler mHandler;
    private Timer timer ;
    
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    // 录音文件名
    private String fileName;


    private void findViews() {
        editTextAddTitle = (EditText)findViewById( R.id.editTextAddTitle );
        textViewContent = (TextView)findViewById( R.id.textViewContent );
        imageViewAddRecord = (ImageView)findViewById( R.id.imageViewAddRecord );
        textViewTimeMin = (TextView)findViewById( R.id.textViewTimeMin );
        textViewTimeSec = (TextView)findViewById( R.id.textViewTimeSec );
        progressBarAddRate = (ProgressBar)findViewById( R.id.ProgressBarAddRate );
        imageViewAddStop = (ImageView)findViewById( R.id.imageViewAddStop );
        buttonAddReturn = (Button)findViewById( R.id.buttonAddReturn );
        buttonAddSave = (Button)findViewById( R.id.buttonAddSave );

        imageViewAddRecord.setOnClickListener(this);
        imageViewAddStop.setOnClickListener(this);
        buttonAddReturn.setOnClickListener( this );
        buttonAddSave.setOnClickListener( this );
        
    }
    int ret = 0; // 函数调用返回值 
    @Override
    public void onClick(View v) {
        if ( v == buttonAddReturn ) {
            
        } else if ( v == buttonAddSave ) {
            
        } else if ( v == imageViewAddRecord) {
            if (!recording) {
                imageViewAddRecord.setImageResource(R.drawable.image_pause);
                setTimerTaskStart();
                recording = true;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");       
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间       
                fileName = formatter.format(curDate);   
                editTextAddTitle.setText(fileName);
                textViewContent.setText(null);
                setParam();
                ret = mIat.startListening(recognizerListener);
            }else {
                imageViewAddRecord.setImageResource(R.drawable.image_record);
                setTimerTaskStop();
                recording = false;
            }
        } else if ( v == imageViewAddStop ) {
            
        }
    }

        
    private void setTimerTaskStop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void setTimerTaskStart(){
        timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override 
            public void run(){
                Message message = new Message();
                message.what=1; 
                mHandler.sendMessage(message);
            } 
        },0,1000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reminder);
        findViews();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Time++;
                int second = Time % 60;
                String secondString = second<10 ? "0"+second : "" +second;
                textViewTimeSec.setText(secondString);
                int minute = Time /60; 
                String minuteString = minute<10 ? "0"+minute : "" +minute;
                textViewTimeMin.setText(minuteString);
            }
        };
        
        //初始化科大讯飞 sdk
        SpeechUtility.createUtility(getBaseContext(), SpeechConstant.APPID +"=5518ec36"); 
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);

    }
    
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("科大讯飞相关", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.d("科大讯飞相关", "初始化失败，错误码： " + code);
            }
        }
    };
    
    /**
     * 参数设置
     * 
     * @param param
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        
        // 设置
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn"); 
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        // 设置语音前端点
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        // 设置语音后端点
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        // 设置音频保存路径
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()
                + "/voiceReminder/" + fileName +".pcm");
        // 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
        // 注：该参数暂时只对在线听写有效
        mIat.setParameter(SpeechConstant.ASR_DWA,"0");
    }
    /**
     * 听写监听器。
     */
    private RecognizerListener recognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语音+）需要提示用户开启语音+的录音权限。
        }

        @Override
        public void onEndOfSpeech() {
            imageViewAddRecord.setImageResource(R.drawable.image_record);
            setTimerTaskStop();
            recording = false;
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d("科大讯飞相关", results.getResultString());
            printResult(results);

            if (isLast) {
            }
        }

        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }

        @Override
        public void onVolumeChanged(int volume) {
            progressBarAddRate.setProgress(volume);
        }
    };
    // textview显示识别结果
    private void printResult(RecognizerResult results) {
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

        textViewContent.setText(resultBuffer.toString());
//        textViewContent.setSelection(textViewContent.length());
    }
}