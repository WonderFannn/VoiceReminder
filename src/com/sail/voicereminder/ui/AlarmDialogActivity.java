package com.sail.voicereminder.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;

import com.sail.voicereminder.R;
import com.sail.voicereminder.audio.AudioParam;
import com.sail.voicereminder.audio.AudioPlayer;
import com.sail.voicereminder.db.VoiceRemindRecord;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class AlarmDialogActivity extends Activity implements OnClickListener {
    private ImageView imageViewPlay;
    private Button buttonEnter;
    private Button buttonQuit;

    private VoiceRemindRecord record;
    boolean isPlaying = false;
    private AudioPlayer audioPlayer;
    private Handler handler;
    private Timer timer;
    private int playTime = 0;
    private int recordMaxTime;
    final int TIMER_SECOND = 6130;
    

    private void findViews() {
        imageViewPlay = (ImageView)findViewById( R.id.imageViewPlay );
        buttonEnter = (Button)findViewById( R.id.buttonEnter );
        buttonQuit = (Button)findViewById( R.id.buttonQuit );

        imageViewPlay .setOnClickListener(this);
        buttonEnter.setOnClickListener( this );
        buttonQuit.setOnClickListener( this );
    }


    @Override
    public void onClick(View v) {
        if ( v == buttonEnter ) {
            Intent mainIntent = new Intent(getApplicationContext(), MainVoiceReminderActivity.class);
            startActivity(mainIntent);
            finish();
        } else if ( v == buttonQuit ) {
            finish();
        }else if (v == imageViewPlay) {

            if (!isPlaying) {
                audioPlayer.play();
                imageViewPlay.setImageResource(R.drawable.image_pause);
            } else {
                audioPlayer.stop();
                imageViewPlay.setImageResource(R.drawable.image_play);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_dialog);
//        requestWindowFeature(Window.FEATURE_LEFT_ICON);  
        record = (VoiceRemindRecord) getIntent().getParcelableExtra("record");
        initLogic();
        findViews();
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
                    } else {
                        audioPlayer.stop();
                        imageViewPlay.setImageResource(R.drawable.image_play);
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
}
