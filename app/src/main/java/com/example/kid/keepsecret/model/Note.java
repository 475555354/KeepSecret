package com.example.kid.keepsecret.model;

import android.text.format.DateFormat;

import java.util.Date;
import java.util.UUID;

/**
 * Created by niuwa on 2016/4/28.
 */
public class Note {
    private String mId;
    private String title;
    private String content;
    private Date mDate;
    private String tagColor;

    public Note(){
        mId = UUID.randomUUID().toString();
        mDate = new Date();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getDate() {
        return (String)DateFormat.format("MM-dd", mDate);
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }
}
