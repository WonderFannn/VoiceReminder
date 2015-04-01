package com.sail.voicereminder.db;

public class VoiceRemindRecord {
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

    
}
