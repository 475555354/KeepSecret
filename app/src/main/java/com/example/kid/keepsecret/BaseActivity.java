package com.example.kid.keepsecret;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niuwa on 2016/5/11.
 */
public class BaseActivity extends AppCompatActivity {

    public static List<Activity> mActivities = new ArrayList<Activity>();
    public void addActivit(Activity activity){
        mActivities.add(activity);
    }
    public void removeActivity(Activity activity){
        mActivities.remove(activity);
    }
    public void finishAll(){
        for (Activity activity : mActivities){
            if (!activity.isFinishing())
                activity.finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

}
