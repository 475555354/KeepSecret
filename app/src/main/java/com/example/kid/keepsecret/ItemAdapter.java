package com.example.kid.keepsecret;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kid.keepsecret.model.Note;

import java.util.ArrayList;

/**
 * Created by niuwa on 2016/4/28.
 */
public class ItemAdapter extends ArrayAdapter<Note>{

    private int resourceId;

    public ItemAdapter(Context context, int layoutResourceId,ArrayList<Note> notes){
        super(context, layoutResourceId, notes);
        resourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View v;
        if (convertView == null){
            v = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }else {
            v = convertView;
        }

        TextView title = (TextView)v.findViewById(R.id.item_title);
        TextView time = (TextView)v.findViewById(R.id.item_time);
        ImageView tag = (ImageView)v.findViewById(R.id.item_tag);

        Note note = getItem(position);

        title.setText(note.getContent());
        time.setText(note.getDate());
        //tag.setDrawingCacheBackgroundColor(Color.parseColor(note.getTagColor()));
        if (note.getTagColor() != null){
            tag.setColorFilter(Color.parseColor(note.getTagColor()));
        }else {
            tag.setColorFilter(null);
        }

        return v;
    }
}
