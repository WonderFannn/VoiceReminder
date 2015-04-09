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

public class FindRecordAdapter extends BaseAdapter {
    private View view;
    private TextView tvId;
    private TextView tvTitle;
    private TextView tvContent;
    
    private Context context;
    private List<VoiceRemindRecord> records;
    private MyDBOperate myDBOperate;
    
    public FindRecordAdapter(Context context, String condition)
    {
        this.context = context;
        myDBOperate = new MyDBOperate(context);
        records = myDBOperate.findByKey(condition);
    }
    @Override
    public int getCount() {
        return records.size();
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

        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.cell_record, null);
            tvId = (TextView) view.findViewById(R.id.textViewNumber);
            tvTitle = (TextView) view.findViewById(R.id.textViewTitle);
            tvContent = (TextView) view.findViewById(R.id.textViewContent);


        }
        tvId.setText("" + records.get(position).getId());
        tvTitle.setText(records.get(position).getTitle());
        tvContent.setText(records.get(position).getContent());
    
        return view;
    }
    public VoiceRemindRecord getRecord(int position) {
        return records.get(position);
    }
}
