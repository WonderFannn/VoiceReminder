package com.sail.voicereminder.audio;

public class AudioParam {

    int mFrequency;                                        // 采样率
    
    int mChannel;                                        // 声道
    
    int mSampBit;                                        // 采样精度
    
    public void setmFrequency(int mFrequency) {
        this.mFrequency = mFrequency;
    }
    
    public void setmChannel(int mChannel) {
        this.mChannel = mChannel;
    }
    
    public void setmSampBit(int mSampBit) {
        this.mSampBit = mSampBit;
    }
    
}
