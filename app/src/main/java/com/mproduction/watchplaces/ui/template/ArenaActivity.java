package com.mproduction.watchplaces.ui.template;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.mproduction.watchplaces.R;
import com.mproduction.watchplaces.utils.Utils;

import java.lang.ref.WeakReference;

public class ArenaActivity extends AppCompatActivity {

    protected void initViews() {
        uiHandler = new UIHandler(this);
        uiHandler.sendEmptyMessageDelayed(UIHandler.MSG_NEXT_TICK, getNextTickDelay());

    }

    public String triggerPath = null;
    private ChildEventListener listener = null;

    @Override
    public void onResume() {
        super.onResume();
        loadContents();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (triggerPath != null && listener != null) {
            listener = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHandler.removeMessages(UIHandler.MSG_NEXT_TICK);
    }

    protected void loadContents() {

    }

    protected void updateContents() {

    }

    public void updateTickTimer() {

    }

    protected static class UIHandler extends Handler {
        public static final int MSG_SHOW_PROGRESS = 1000;
        public static final int MSG_HIDE_PROGRESS = 1001;
        public static final int MSG_UPDATE_CONTENTS = 1002;
        public static final int MSG_SHOW_INVALID = 1003;
        public static final int MSG_SHOW_FAIL = 1004;
        public static final int MSG_PHOTO_FAILURE = 1005;
        public static final int MSG_INIT_PLAYER = 1006;
        public static final int MSG_HIDE_PLAYER = 1007;
        public static final int MSG_UPDATE_OVERLAY = 1008;
        public static final int MSG_PHOTO_PROMPT = 1009;
        public static final int MSG_VIDEO_PROMPT = 1010;
        public static final int MSG_INVALID_PARAM = 1011;
        public static final int MSG_FINISH = 1012;
        public static final int MSG_NEXT_TICK = 1013;
        public static final int MSG_UPDATE_TABS = 1014;
        public static final int MSG_LOCATION_UPDATE = 1015;

        private WeakReference<ArenaActivity> obj;

        public UIHandler(ArenaActivity object) {
            obj = new WeakReference<ArenaActivity>(object);
        }

        @Override
        public void handleMessage(Message msg) {
            ArenaActivity object = obj.get();
            switch (msg.what) {
                case MSG_SHOW_PROGRESS:
                    object.showProgressDialog();
                    break;
                case MSG_HIDE_PROGRESS:
                    object.hideProgressDialog();
                    break;
                case MSG_UPDATE_CONTENTS:
                    object.updateContents();
                    break;
                case MSG_NEXT_TICK:
                    object.nextTick();
                    break;
                case MSG_UPDATE_TABS:
                    object.updateTabs();
                    break;
                case MSG_FINISH:
                    object.finish();
                    break;
                default:
                    object.handleMessage(msg.what, msg.obj);
                    break;
            }
        }
    }

    protected UIHandler uiHandler;
    protected ProgressDialog progressDialog;

    protected void showProgressDialog() {
        //dataModel.inProgress = true;
        if(progressDialog != null && progressDialog.isShowing())
        {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(this, R.style.ProgressStyle);
        Utils.showProgressDialog(progressDialog);
    }

    protected void hideProgressDialog() {
        //dataModel.inProgress = false;

        Utils.hideProgressDialog(progressDialog);
    }

    protected void handleMessage(int message, Object object) {}

    protected void nextTick() {
        updateTickTimer();
        uiHandler.sendEmptyMessageDelayed(UIHandler.MSG_NEXT_TICK, getNextTickDelay());
    }

    private long getNextTickDelay() {
        return 1000 - (System.currentTimeMillis() % 1000);
    }

    protected void updateTabs() {
    }
}
