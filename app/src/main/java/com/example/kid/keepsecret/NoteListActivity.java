package com.example.kid.keepsecret;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kid.keepsecret.DB.NoteDB;
import com.example.kid.keepsecret.DB.NoteOpenHelper;
import com.example.kid.keepsecret.model.Note;
import com.jungly.gridpasswordview.GridPasswordView;
import com.jungly.gridpasswordview.PasswordType;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView noteList;
    private FloatingActionButton mActionButton;
    private LinearLayout addItemLine;
    private LinearLayout setPassLine;
    private PopupWindow popupWindow;
    private View popView;
    private TextView popPassText;
    private GridPasswordView popPassGrid;
    private Button popExitBtn;

    private ArrayList<Note> mNotes;
    private NoteOpenHelper mOpenHelper;
    private NoteDB mNoteDB;
    private ItemAdapter mItemAdapter;

    private boolean isFirst = true;
    private String password;
    private int passFlag = 1;

    private DoubleClickExitHelper mDoubleClickExitHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        initDB();
        initView();
        if (loadPsw() != null){
            drawerLayout.post(new Runnable() {
                @Override
                public void run() {
                    openPop(passFlag);
                }
            });
        }
    }

    private void initView(){
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);

        //init actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //init main layout view
        mActionButton = (FloatingActionButton) findViewById(R.id.fab);
        noteList = (ListView)findViewById(R.id.note_list);

        mActionButton.setOnClickListener(this);
        mItemAdapter = new ItemAdapter(this, R.layout.item_note, mNotes);
        noteList.setAdapter(mItemAdapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uuid = mNotes.get(position).getId();
                Log.d("123456", uuid);
                Intent i = new Intent(NoteListActivity.this, NoteActivity.class);
                i.putExtra(NoteActivity.UUID_TAG, uuid);
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


        //init drawer layout view
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        addItemLine = (LinearLayout)findViewById(R.id.drawer_item_add);
        setPassLine = (LinearLayout)findViewById(R.id.drawer_item_set);

        addItemLine.setOnClickListener(this);
        setPassLine.setOnClickListener(this);

        drawerLayout.setDrawerShadow(R.mipmap.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                null,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer
        ){
            public void onDrawerClosed(View view){
                super.onDrawerClosed(view);
            }
            public void onDrawerOpened(View view){
                super.onDrawerOpened(view);
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);


        //init popup window view


        //popPassGrid.setPasswordType(PasswordType.NUMBER);
    }

    private void initDB(){
        mOpenHelper = new NoteOpenHelper(this, "Note_DB", null, 1);
        mOpenHelper.getWritableDatabase();
        mNoteDB = NoteDB.getInstance(this);
        mNotes = mNoteDB.loadNote();
    }

    private void addItem(){
        Intent i = new Intent(NoteListActivity.this, NoteActivity.class);
        startActivity(i);
    }

    private void openPop(int flag){

        popView = LayoutInflater.from(NoteListActivity.this).inflate(R.layout.popup_pass, null);
        popView.setFocusable(true);
        popView.setFocusableInTouchMode(true);

        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);


        popPassText = (TextView)popView.findViewById(R.id.pop_pass_text);
        popPassGrid = (GridPasswordView)popView.findViewById(R.id.pop_pass_grid);
        popExitBtn = (Button) popView.findViewById(R.id.pop_exit_btn);

        //unknown reason, could'nt force to show the soft input
        popPassGrid.post(new Runnable() {
            @Override
            public void run() {

                popPassGrid.setFocusable(true);
                //popPassGrid.setFocusableInTouchMode(true);
                popPassGrid.requestFocus();
                Log.d("123456", "notelisetactivity");
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(popPassGrid, InputMethodManager.SHOW_IMPLICIT);
            }
        });



        if (flag == 1){
            popPassText.setText(R.string.enter_pass);
            popPassGrid.setOnPasswordChangedListener(
                    new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onTextChanged(String psw) {
                    if (psw.length() == 4){
                        popPassGrid.clearPassword();
                        if (!psw.equals(loadPsw())){
                            Toast.makeText(NoteListActivity.this,
                                    R.string.error_hint, Toast.LENGTH_LONG).show();
                            finish();
                        }else {
                            popupWindow.dismiss();
                        }
                    }
                }

                @Override
                public void onInputFinish(String psw) {
                }
            });

            popExitBtn.setOnClickListener(this);

            //does not work
//            popupWindow.getContentView().setOnKeyListener(new View.OnKeyListener() {
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK){
//                        Log.d("123456", "finish");
//                        finish();
//                    }
//                    return false;
//                }
//            });

        }
        if (flag == 2){
            popPassText.setText(R.string.enter_new_pass);
            popPassGrid.setPasswordType(PasswordType.NUMBER);
            popPassGrid.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onTextChanged(String psw) {
                    if (psw.length() == 4 && isFirst){
                        popPassGrid.clearPassword();
                        popPassText.setText(R.string.re_enter_pass);
                        isFirst = false;
                        password = psw;
                    }else if (psw.length() == 4 && !isFirst){
                        popPassText.setText(R.string.enter_pass);
                        if (psw.equals(password)){
                            Toast.makeText(NoteListActivity.this,
                                    R.string.set_pass_successfully,Toast.LENGTH_SHORT).show();
                            isFirst = true;
                            savePsw(psw);
                            popupWindow.dismiss();
                        }else {
                            Toast.makeText(NoteListActivity.this,
                                    R.string.set_pass_failed, Toast.LENGTH_SHORT).show();
                            popPassGrid.clearPassword();
                            isFirst = true;
                            popupWindow.dismiss();
                        }
                    }
                }

                @Override
                public void onInputFinish(String psw) {
                }
            });

            popExitBtn.setVisibility(View.GONE);

            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        }

        popupWindow.showAtLocation(drawerLayout, Gravity.CENTER, 0 , 0);
    }

    private void savePsw(String password){
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("psw", password);
        editor.commit();
    }

    private String loadPsw(){
        SharedPreferences prefs = getSharedPreferences("data", MODE_PRIVATE);
        return prefs.getString("psw", null);
    }

    private void resetList(){
        mNotes.clear();
        for (Note n : mNoteDB.loadNote()){
            mNotes.add(n);
        }
        mItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fab:
                addItem();
                break;
            case R.id.drawer_item_add:
                addItem();
                break;
            case R.id.drawer_item_set:
                drawerLayout.closeDrawers();
                passFlag = 2;
                openPop(passFlag);
                break;
            case R.id.pop_exit_btn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
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

        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;

        //noinspection SimplifiableIfStatement
        if (item.getItemId() == R.id.action_settings) {
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


}
