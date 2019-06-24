package rkr.simplekeyboard.inputmethod.touchservice;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class TouchManager extends IntentService implements View.OnTouchListener {

    private String TAG = this.getClass().getSimpleName();
    private WindowManager mWindowManager;
    private LinearLayout touchLayout;

    public TouchManager() {
        super("SwipeScrollCatcherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        System.out.println("capasse");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();
        // create linear layout
        touchLayout = new LinearLayout(this);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        touchLayout.setLayoutParams(lp);

        touchLayout.setOnTouchListener(this);

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG ,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, // this window won't ever get key input focus
                PixelFormat.TRANSLUCENT);
        mParams.gravity = Gravity.START | Gravity.TOP;
        Log.i(TAG, "add View");

        mWindowManager.addView(touchLayout, mParams);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
