package com.sail.voicereminder.adapter;


import java.util.List;

import com.sail.voicereminder.R;
import com.sail.voicereminder.db.MyDBOperate;
import com.sail.voicereminder.db.VoiceRemindRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyRecordAdapter extends BaseAdapter {
    View view;
    TextView tvId;
    TextView tvTitle;
    TextView tvContent;
    
    private Context context;
    private List<VoiceRemindRecord> records;
    private MyDBOperate myDBOperate;
    public MyRecordAdapter(Context context)
    {
        this.context = context;
        myDBOperate = new MyDBOperate(context);
        records = myDBOperate.findAll();
    }
    @Override
    public int getCount() {
        return (1 > records.size() )? 1 : records.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(records.size() == 0){
            view = inflater.inflate(R.layout.cell_record_empty, null);
        }else {
            
            if (convertView != null) {
                view = convertView;
            }else {
                view = inflater.inflate(R.layout.cell_record, null);
                tvId = (TextView) view.findViewById(R.id.textViewNumber);
                tvTitle = (TextView) view.findViewById(R.id.textViewTitle);
                tvContent = (TextView) view.findViewById(R.id.textViewContent);
                
                tvId.setText(""+records.get(position).getId());
                tvTitle.setText(records.get(position).getTitle());
                tvContent.setText(records.get(position).getContent());
                
            }
        }
    
        return view;
    }


}
