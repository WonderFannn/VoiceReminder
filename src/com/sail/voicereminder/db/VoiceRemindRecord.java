package com.sail.voicereminder.db;


import android.os.Parcel;
import android.os.Parcelable;

public class VoiceRemindRecord implements Parcelable{
    private int _id;
    private String title;
    private String time;
    private String file;
    private String content;
    
    public VoiceRemindRecord(int id,String titleString, String timeString , String fileString, String contentString){
        _id = id;
        title = titleString;
        time = timeString;
        file = fileString;
        content = contentString;
    }
    
    public void setId(int id) {
        _id = id;
    }
    public int getId() {
        return _id;
    }
    
    public void setTitle(String tString) {
        title = tString;
    }
    public String getTitle() {
        return title;
    }
    
    public void setTime(String tString) {
        time = tString;
    }
    public String getTime() {
        return time;
    }
    
    public void setFile(String tString) {
        file = tString;
    }
    public String getFile() {
        return file;
    }
    
    public void setContent(String tString) {
        content = tString;
    }
    public String getContent() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
          dest.writeInt(_id);
          dest.writeString(title);
          dest.writeString(time);
          dest.writeString(file);
          dest.writeString(content);
          
    }

    public static final Parcelable.Creator<VoiceRemindRecord> CREATOR = new Parcelable.Creator<VoiceRemindRecord>() {
        public VoiceRemindRecord createFromParcel(Parcel in) {
            return new VoiceRemindRecord(in);
        }

        public VoiceRemindRecord[] newArray(int size) {
            return new VoiceRemindRecord[size];
        }
    };

    public VoiceRemindRecord (Parcel source){
          /*
           * Reconstruct from the Parcel. Keep same order as in writeToParcel()
           */
          _id = source.readInt();
          title = source.readString();
          time = source.readString();
          file = source.readString();
          content = source.readString();
    }   
    
}
