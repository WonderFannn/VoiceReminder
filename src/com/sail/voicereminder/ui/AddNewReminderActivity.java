package com.sail.voicereminder.ui;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.sail.voicereminder.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AddNewReminderActivity extends Activity{
      
    private RecognizerListener mRecoListener = new RecognizerListener(){    
   
      public void onResult(RecognizerResult results, boolean isLast) {    
                  Log.d("Result:",results.getResultString ());}    

          public void onError(SpeechError error) {    
          error.getPlainDescription(true); 
          }    
          public void onBeginOfSpeech() {}    
          public void onVolumeChanged(int volume){}    
          public void onEndOfSpeech() {}    
          public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}    
      };    
      InitListener mInitListener =  new InitListener() {
        
        @Override
        public void onInit(int arg0) {
            // TODO Auto-generated method stub
            
        }
    };
    RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        
        @Override
        public void onResult(RecognizerResult arg0, boolean arg1) {
            // TODO Auto-generated method stub
            Log.d("Result:",arg0.getResultString ());
        }
        
        @Override
        public void onError(SpeechError arg0) {
            // TODO Auto-generated method stub
            
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reminder);
        SpeechUtility.createUtility(getBaseContext(), SpeechConstant.APPID +"=5518ec36"); 
       
        RecognizerDialog  iatDialog = new RecognizerDialog(this,mInitListener);  
        iatDialog.setParameter(SpeechConstant.DOMAIN, "iat");    
        iatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");    
        iatDialog.setParameter(SpeechConstant.ACCENT, "mandarin ");    
        
        iatDialog.setListener(mRecognizerDialogListener);  
      
        iatDialog.show();   
       
    }
    
}
