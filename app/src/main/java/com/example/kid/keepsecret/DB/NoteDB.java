package com.example.kid.keepsecret.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kid.keepsecret.model.Note;

import java.util.ArrayList;

/**
 * Created by niuwa on 2016/4/29.
 */
public class NoteDB {

    public static final String DB_NAME = "keep_secret";
    public static final String TABLE_NAME = "note_table";
    public static final int VERSION = 1;

    private static NoteDB sNoteDB;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Note note;

    private NoteDB(Context context){
        NoteOpenHelper openHelper = new NoteOpenHelper(context, DB_NAME, null, VERSION);
        db = openHelper.getWritableDatabase();
    }

    public static NoteDB getInstance(Context context){
        if (sNoteDB == null)
            sNoteDB = new NoteDB(context);
        return sNoteDB;
    }

    public void saveNote(Note note){
        if (note != null){
            ContentValues contentValues = new ContentValues();
            contentValues.put("uuid", note.getId());
            contentValues.put("content", note.getContent());
            contentValues.put("date", note.getDate());
            contentValues.put("tag_color", note.getTagColor());
            db.insert(TABLE_NAME, null, contentValues);
        }
    }

    public ArrayList<Note> loadNote(){
        ArrayList<Note> list = new ArrayList<Note>();
        cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            do {
                setNote();
                list.add(note);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public ArrayList<Note> loadNote(String content){
        ArrayList<Note> list = new ArrayList<Note>();
        String sb = new StringBuilder().append("%").append(content).append("%").toString();
        Log.d("123456", sb);
        cursor = db.query(TABLE_NAME, null, "content like ?", new String[]{sb}, null, null, null);
        if (cursor.moveToFirst()){
            do {
                setNote();
                list.add(note);
            }while (cursor.moveToNext());
        }
        return list;
    }

    public Note loadNoteById(String uuid){
        cursor = db.query(TABLE_NAME, null, "uuid = ?",
                new String[]{uuid}, null, null, null);
        if (cursor.moveToFirst()){
            setNote();
            return note;
        }else {
            return null;
        }
    }


    public void updateNote(Note note){
        ContentValues values = new ContentValues();
        values.put("content", note.getContent());
        values.put("date", note.getDate());
        values.put("tag_color", note.getTagColor());
        db.update(TABLE_NAME, values, "uuid = ?", new String[]{note.getId()});
    }

    public void deleteNoteById(String uuid){
        db.delete(TABLE_NAME, "uuid = ?", new String[]{uuid});
    }

    private void setNote(){
        note = new Note();
        note.setId(cursor.getString(cursor.getColumnIndex("uuid")));
        note.setContent(cursor.getString(cursor.getColumnIndex("content")));
        note.setDate(cursor.getLong(cursor.getColumnIndex("date")));
        note.setTagColor(cursor.getString(cursor.getColumnIndex("tag_color")));
    }
}
