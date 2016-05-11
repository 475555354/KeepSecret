package com.example.kid.keepsecret;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kid.keepsecret.DB.NoteDB;
import com.example.kid.keepsecret.model.Note;

import java.util.ArrayList;

/**
 * Created by niuwa on 2016/5/11.
 */
public class SearchResultsActivity extends BaseActivity {

    ListView mListView;

    ArrayList<Note> mNotes;
    NoteDB mNoteDB;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addActivit(this);
        setContentView(R.layout.activity_search_results);
        handleIntent(getIntent());
        initView();
    }

    private void initView(){
        mListView = (ListView) findViewById(R.id.search_results_list);
        mListView.setAdapter(new ItemAdapter(this, R.layout.item_note, mNotes));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uuid = mNotes.get(position).getId();
                Intent i = new Intent(SearchResultsActivity.this, NoteActivity.class);
                i.putExtra(NoteActivity.UUID_TAG, uuid);
                startActivity(i);
            }
        });
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            mNoteDB = NoteDB.getInstance(this);
            mNotes = mNoteDB.loadNote(query);
            Log.d("123456", query);
        }
    }
}
