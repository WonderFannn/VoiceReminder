package com.sail.voicereminder.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sail.voicereminder.R;
import com.sail.voicereminder.alarm.MyAlarmReceiver;
import com.sail.voicereminder.audio.AudioParam;
import com.sail.voicereminder.audio.AudioPlayer;
import com.sail.voicereminder.db.MyDBOperate;
import com.sail.voicereminder.db.VoiceRemindRecord;

public class ModifyReminderActivity<MainActivity> extends Activity implements OnClickListener, TextWatcher {
    VoiceRemindRecord record;
    
    private EditText editTextModifyTitle;
    private EditText editTextModifyContent;
    private TextView textViewModifyTimeMin;
    private TextView textViewModifyTimeSec;
    private ProgressBar progressBarModifyProgress;
    private ImageView imageViewModifyPlay;
    private ImageView imageViewModifyStop;
    private Button buttonModifyReturn;
    private Button buttonModifyDelete;
    private Button buttonModifySave;
    private Spinner spinnerClassify;
    private Button buttonModifySetting;
    
    // 音频播放相关
    private AudioPlayer audioPlayer;
    private boolean isPlaying = false;
    private Handler handler;
    private Timer timer;
    private int playTime = 0;
    private int recordMaxTime;
    final int TIMER_SECOND = 6130;
    
    // 数据库相关
    private MyDBOperate myDBOperate;
    
    // 闹钟相关
    private Calendar calender = null;  
    

    private void findAndInitViews() {
        editTextModifyTitle = (EditText) findViewById( R.id.editTextModifyTitle );
        editTextModifyContent = (EditText) findViewById( R.id.editTextModifyContent );
        textViewModifyTimeMin = (TextView) findViewById( R.id.textViewModifyTimeMin );
        textViewModifyTimeSec = (TextView) findViewById( R.id.textViewModifyTimeSec );
        progressBarModifyProgress = (ProgressBar) findViewById( R.id.progressBarModifyProgress );
        imageViewModifyPlay = (ImageView) findViewById( R.id.imageViewModifyPlay );
        imageViewModifyStop = (ImageView) findViewById( R.id.imageViewModifyStop );
        buttonModifyReturn = (Button) findViewById( R.id.buttonModifyReturn );
        buttonModifySetting = (Button) findViewById(R.id.buttonModifySetting);
        buttonModifyDelete = (Button) findViewById( R.id.buttonModifyDelete );
        buttonModifySave = (Button) findViewById( R.id.buttonModifySave );
        spinnerClassify = (Spinner) findViewById(R.id.spinnerClassify);
        
        editTextModifyTitle.setText(record.getTitle());
        editTextModifyTitle.addTextChangedListener(this);
        editTextModifyContent.setText(record.getContent());
        editTextModifyContent.addTextChangedListener(this);
        recordMaxTime = Integer.valueOf(record.getTime()).intValue();
        setTimeView(recordMaxTime);
        progressBarModifyProgress.setProgress(0);
        progressBarModifyProgress.setMax(recordMaxTime);
        String[] classifyStrings = getResources().getStringArray(R.array.classify);
        int classifyPosition;
        for (classifyPosition = 0; classifyPosition < classifyStrings.length; classifyPosition++) {
            if (record.getClassify().equals(classifyStrings[classifyPosition])) {
                break;
            }
        }
        spinnerClassify.setSelection(classifyPosition);
        imageViewModifyPlay.setOnClickListener( this );
        imageViewModifyStop.setOnClickListener( this );
        buttonModifyReturn.setOnClickListener( this );
        buttonModifySetting.setOnClickListener( this );
        buttonModifyDelete.setOnClickListener( this );
        buttonModifySave.setOnClickListener( this );
        Log.d("点击设置","不可点击");
        buttonModifySave.setClickable( false );
        calender = Calendar.getInstance();

    }
    
    private void setTimeView(int time) {
        int second = time % 60;
        String secondString = second<10 ? "0"+second : "" +second;
        textViewModifyTimeSec.setText(secondString);
        int minute = time /60; 
        String minuteString = minute<10 ? "0"+minute : "" +minute;
        textViewModifyTimeMin.setText(minuteString);
    }

    @Override
    public void onClick(View v) {
        if ( v == buttonModifyReturn ) {
            Intent mainIntent = new Intent(ModifyReminderActivity.this,MainVoiceReminderActivity.class);
            startActivity(mainIntent);
            finish();
        } else if (v == buttonModifySetting) {
            calender.setTimeInMillis(System.currentTimeMillis());  
            int hour = calender.get(Calendar.HOUR_OF_DAY);  
            int minute = calender.get(Calendar.MINUTE);  
            new TimePickerDialog(ModifyReminderActivity.this,new TimePickerDialog.OnTimeSetListener(){  
                
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,  
                        int minute) {  
                    calender.setTimeInMillis(System.currentTimeMillis());  
                    calender.set(Calendar.HOUR_OF_DAY, hourOfDay);  
                    calender.set(Calendar.MINUTE, minute);  
                    calender.set(Calendar.SECOND, 0);  
                    calender.set(Calendar.MILLISECOND, 0);  
                    Intent intent = new Intent(ModifyReminderActivity.this,MyAlarmReceiver.class);  
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("record", record);
                    intent.putExtras(mBundle);
                    PendingIntent pi = PendingIntent.getBroadcast(ModifyReminderActivity.this, 0, intent, 0);  
                    AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);  
                    am.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pi);//设置闹钟  
//                    am.setRepeating(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), (10*1000), pi);//重复设置  
                }
                  
            },hour,minute,true).show();
            
            
        } else if ( v == buttonModifyDelete ) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ModifyReminderActivity.this);  
            alertDialog.setTitle("删除确认").setIcon(R.drawable.ic_launcher).setMessage("确定要删除该条记录?");
            alertDialog.setPositiveButton("确定",  new android.content.DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myDBOperate.delete(record.getId());
                    File f = new File(record.getFile());
                    if (f.exists()) {
                        f.delete();
                    }
                    Intent mainIntent = new Intent(ModifyReminderActivity.this,MainVoiceReminderActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }).setNegativeButton("取消",  new android.content.DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   
                }
            });
            AlertDialog dialog = alertDialog.create();  
            dialog.show();  
            
            
        } else if ( v == buttonModifySave ) {
            record.setTitle(editTextModifyTitle.getText().toString());
            record.setContent(editTextModifyContent.getText().toString());
            record.setClassify(spinnerClassify.getSelectedItem().toString());
            myDBOperate.updata(record);
            buttonModifySave.setClickable(false);
            buttonModifySave.setBackgroundColor(0xff888888);
        } else if ( v == imageViewModifyPlay ) {
            if (!isPlaying) {
                audioPlayer.play();
                imageViewModifyPlay.setImageResource(R.drawable.image_pause);
                setTimerTaskStart();
            } else {
                audioPlayer.pause();
                imageViewModifyPlay.setImageResource(R.drawable.image_play);
                setTimerTaskStop();
            }
        } else if ( v == imageViewModifyStop) {
            audioPlayer.stop();
            setTimerTaskStop();
        }
        
    }
    
    // 关闭定时器
    private void setTimerTaskStop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    // 定时器开始函数,每秒发一次消息
    private void setTimerTaskStart(){
        timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override 
            public void run(){
                Message message = new Message();
                message.what = TIMER_SECOND; 
                handler.sendMessage(message);
            } 
        },0,1000);
    }

    public void initLogic() {
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                case AudioPlayer.STATE_MSG_ID:
                    // showState((Integer)msg.obj);
                    break;
                case TIMER_SECOND:
                    if (playTime <= recordMaxTime) {
                        playTime++;
                        setTimeView(playTime);
                        progressBarModifyProgress.setProgress(playTime);
                    } else {
                        audioPlayer.stop();
                        setTimerTaskStop();
                        imageViewModifyPlay.setImageResource(R.drawable.image_play);
                        progressBarModifyProgress.setProgress(0);
                        isPlaying = false;
                        playTime = 0;
                    }
                    break;

                }
            }

        };

        audioPlayer = new AudioPlayer(handler);

        // 获取音频参数
        AudioParam audioParam = getAudioParam();
        audioPlayer.setAudioParam(audioParam);

        // 获取音频数据
        byte[] data = getPCMData();
        audioPlayer.setDataSource(data);

        // 音频源就绪
        audioPlayer.prepare();

        if (data == null) {
            Log.v("音频相关", "文件不存在");
        }
    }
    /*
     * 获得PCM音频数据参数
     */
        @SuppressWarnings("deprecation")
        public AudioParam getAudioParam()
        {
                
            AudioParam audioParam = new AudioParam();
            audioParam.setmFrequency(8000) ;
            audioParam.setmChannel(AudioFormat.CHANNEL_CONFIGURATION_STEREO); 
            audioParam.setmSampBit(AudioFormat.ENCODING_PCM_16BIT);
            
            return audioParam;
        }
        
        
        
    /*
     * 获得PCM音频数据
     */
    @SuppressWarnings({ "resource", "unused" })
    public byte[] getPCMData(){
            
            String  filePath  = record.getFile();
            File file = new File(filePath);
            if (file == null){
                    return null;
            }
            
            FileInputStream inStream;
                try {
                        inStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return null;
                }
                
            byte[] data_pack = null;
            if (inStream != null){
                    long size = file.length();
                    
                    data_pack = new byte[(int) size];
                    try {
                                inStream.read(data_pack);
                        } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                        }

            }
            
            return data_pack;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_reminder);
        
        record = (VoiceRemindRecord) getIntent().getParcelableExtra("record");
        myDBOperate = new MyDBOperate(getApplicationContext());
        findAndInitViews();
        initLogic();
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.release();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent mainIntent = new Intent(ModifyReminderActivity.this,MainVoiceReminderActivity.class);
            startActivity(mainIntent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("textchange","文本改变执行");
        buttonModifySave.setClickable(true);
        buttonModifySave.setBackgroundColor(0xffbbff00);
    }
}
