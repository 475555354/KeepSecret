package com.example.kid.keepsecret;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kid.keepsecret.DB.NoteDB;
import com.example.kid.keepsecret.DB.NoteOpenHelper;
import com.example.kid.keepsecret.model.Note;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private ListView drawerList;
    private ListView noteList;
    private FloatingActionButton mActionButton;

    private ArrayList<Note> mNotes;
    private NoteOpenHelper mOpenHelper;
    private NoteDB mNoteDB;
    private ItemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setContentView(R.layout.activity_main);
        initDB();
        initView();
    }

    private void initView(){
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer
        );
        drawerLayout.addDrawerListener(mDrawerToggle);

        drawerList = (ListView)findViewById(R.id.drawer);

        noteList = (ListView)findViewById(R.id.note_list);
        mItemAdapter = new ItemAdapter(this, R.layout.note_item, mNotes);
        noteList.setAdapter(mItemAdapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uuid = mNotes.get(position).getId();
                Log.d("123456", uuid);
                Intent i = new Intent(NoteListActivity.this, NoteActivity.class);
                i.putExtra(NoteActivity.uuidTag, uuid);
                startActivity(i);
            }
        });
        noteList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        noteList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_delete, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.note_delete:
                        for (int i = mItemAdapter.getCount(); i >= 0; i--){
                            if (noteList.isItemChecked(i)){
                                String uuid = mNotes.get(i).getId();
                                mNoteDB.deleteNoteById(uuid);
                            }
                        }
                        mode.finish();
                        resetList();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });

        mActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NoteListActivity.this, NoteActivity.class);
                startActivity(i);
            }
        });
    }

    private void initDB(){
        mOpenHelper = new NoteOpenHelper(this, "Note_DB", null, 1);
        mOpenHelper.getWritableDatabase();
        mNoteDB = NoteDB.getInstance(this);
        mNotes = mNoteDB.loadNote();
    }

    @Override
    public void onResume(){
        super.onResume();
        resetList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void resetList(){
        mNotes.clear();
        for (Note n : mNoteDB.loadNote()){
            mNotes.add(n);
        }
        mItemAdapter.notifyDataSetChanged();
    }
}
