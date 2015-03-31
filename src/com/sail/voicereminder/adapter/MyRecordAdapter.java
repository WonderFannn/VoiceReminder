package com.sail.voicereminder.adapter;

import java.util.zip.Inflater;

import com.sail.voicereminder.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MyRecordAdapter extends BaseAdapter {
    View view;
    private Context context;
    public MyRecordAdapter(Context context)
    {
        this.context = context;
    }
    @Override
    public int getCount() {
        return 1;
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
        if(getCount() == 1){
            view = inflater.inflate(R.layout.cell_record_empty, null);
        }else{
            view = inflater.inflate(R.layout.cell_record, null);
        }
        return view;
    }


}
