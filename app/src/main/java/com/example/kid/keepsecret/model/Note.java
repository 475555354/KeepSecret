package com.example.kid.keepsecret.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by niuwa on 2016/4/28.
 */
public class Note implements Parcelable {
    private String mId;
    private String title;
    private String content;
    private long mDate;
    private String tagColor;

    public Note(){
        mId = UUID.randomUUID().toString();
        mDate = new Date().getTime();
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

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public String getTagColor() {
        return tagColor;
    }

    public void setTagColor(String tagColor) {
        this.tagColor = tagColor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mId);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeLong(this.mDate);
        dest.writeString(this.tagColor);
    }

    protected Note(Parcel in) {
        this.mId = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.mDate = in.readLong();
        this.tagColor = in.readString();
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
