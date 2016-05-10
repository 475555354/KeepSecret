package com.example.kid.keepsecret;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * Created by niuwa on 2016/5/10.
 */
public class DoubleClickExitHelper {
    private Activity mActivity;

    private Handler mHandler;
    private boolean isOnKeyBacking;
    private Toast mBackToast;

    public DoubleClickExitHelper(Activity activity) {
        mActivity = activity;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isOnKeyBacking) {
            mHandler.removeCallbacks(onBackTimeRunnable);
            if (mBackToast != null)
                mBackToast.cancel();
            mActivity.finish();
            System.exit(0);
            return true;
        } else {
            isOnKeyBacking = true;
            if (mBackToast == null)
                mBackToast = Toast.makeText(mActivity,
                        R.string.double_click_exit_text, Toast.LENGTH_LONG);
            mBackToast.show();
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }

    private Runnable onBackTimeRunnable = new Runnable() {
        @Override
        public void run() {
            isOnKeyBacking = false;
            if (mBackToast != null)
                mBackToast.cancel();
        }
    };
}
