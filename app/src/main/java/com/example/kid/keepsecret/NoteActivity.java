package com.example.kid.keepsecret;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.kid.keepsecret.DB.NoteDB;
import com.example.kid.keepsecret.model.Note;

/**
 * Created by niuwa on 2016/4/29.
 */
public class NoteActivity extends AppCompatActivity {

    public static final String uuidTag = "UUID_TAG";


    private EditText mEditText;

    private NoteDB mNoteDB;
    private Note mNote;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mNoteDB = NoteDB.getInstance(this);

        Intent i = getIntent();
        String uuid = i.getStringExtra(uuidTag);
        if (uuid != null){
            mNote = mNoteDB.loadNoteById(uuid);
        }else {
            mNote = new Note();
        }

        mEditText = (EditText)findViewById(R.id.edit_text);
        mEditText.setText(mNote.getContent());
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setContent(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
        if (mNote.getContent() != null){
            if (mNoteDB.loadNoteById(mNote.getId()) != null){
                mNoteDB.updateNote(mNote.getId(), mNote.getContent());
            }else {
                mNoteDB.saveNote(mNote);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.note_delete:
                String uuid = mNote.getId();
                if (uuid != null){
                    mNoteDB.deleteNoteById(uuid);
                    mNote.setContent(null);
                    finish();
                }
                return true;
            case R.id.note_other:
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
