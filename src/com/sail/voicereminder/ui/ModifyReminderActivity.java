package com.sail.voicereminder.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.sail.voicereminder.R;
import com.sail.voicereminder.audio.AudioParam;
import com.sail.voicereminder.audio.AudioPlayer;
import com.sail.voicereminder.db.VoiceRemindRecord;

import android.app.Activity;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ModifyReminderActivity extends Activity implements OnClickListener {
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
    
    // 音频播放相关
    private AudioPlayer audioPlayer;
    private boolean isPlaying = false;
    private Handler handler;

    private void findAndInitViews() {
        editTextModifyTitle = (EditText)findViewById( R.id.editTextModifyTitle );
        editTextModifyContent = (EditText)findViewById( R.id.editTextModifyContent );
        textViewModifyTimeMin = (TextView)findViewById( R.id.textViewModifyTimeMin );
        textViewModifyTimeSec = (TextView)findViewById( R.id.textViewModifyTimeSec );
        progressBarModifyProgress = (ProgressBar)findViewById( R.id.progressBarModifyProgress );
        imageViewModifyPlay = (ImageView)findViewById( R.id.imageViewModifyPlay );
        imageViewModifyStop = (ImageView)findViewById( R.id.imageViewModifyStop );
        buttonModifyReturn = (Button)findViewById( R.id.buttonModifyReturn );
        buttonModifyDelete = (Button)findViewById( R.id.buttonModifyDelete );
        buttonModifySave = (Button)findViewById( R.id.buttonModifySave );
        
        editTextModifyTitle.setText(record.getTitle());
        editTextModifyContent.setText(record.getContent());
        setTimeView(Integer.valueOf(record.getTime()).intValue());
        imageViewModifyPlay.setOnClickListener( this );
        imageViewModifyStop.setOnClickListener( this );
        buttonModifyReturn.setOnClickListener( this );
        buttonModifyDelete.setOnClickListener( this );
        buttonModifySave.setOnClickListener( this );
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
            
        } else if ( v == buttonModifyDelete ) {
            
        } else if ( v == buttonModifySave ) {
            
        } else if ( v == imageViewModifyPlay ) {
            audioPlayer.play();
        } else if ( v == imageViewModifyStop) {
            audioPlayer.stop();
        }
        
    }
    public void initLogic()
    {
            handler = new Handler()
            {

                        @Override
                        public void handleMessage(Message msg) {
                                switch(msg.what)
                                {
                                case AudioPlayer.STATE_MSG_ID:
//                                        showState((Integer)msg.obj);
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
                Log.v("音频相关","文件不存在");
            }
    }
    /*
     * 获得PCM音频数据参数
     */
        public AudioParam getAudioParam()
        {
                
            AudioParam audioParam = new AudioParam();
            audioParam.setmFrequency(16000) ;
            audioParam.setmChannel(AudioFormat.CHANNEL_CONFIGURATION_STEREO); 
            audioParam.setmSampBit(AudioFormat.ENCODING_PCM_16BIT);
            
            return audioParam;
        }
        
        
        
    /*
     * 获得PCM音频数据
     */
    @SuppressWarnings("resource")
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
        findAndInitViews();
        initLogic();
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioPlayer.release();
    }
}
