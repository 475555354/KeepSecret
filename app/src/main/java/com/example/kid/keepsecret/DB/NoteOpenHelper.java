package com.example.kid.keepsecret.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by niuwa on 2016/4/29.
 */
public class NoteOpenHelper extends SQLiteOpenHelper {


    private final String CREATE_NOTE_TABLE =
            "create table note_table("
            + "id integer primary key autoincrement,"
            + "uuid text,"
            + "content text,"
            + "date integer,"
            + "tag_color text)";

    public NoteOpenHelper(Context context, String name, CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
