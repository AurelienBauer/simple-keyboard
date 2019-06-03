package rkr.simplekeyboard.inputmethod.keystroke;

import android.view.MotionEvent;

import org.json.JSONObject;

public class KeyboardInput implements KeystrokeActionListener {

    private final KeystrokeManager ksManager;
    private static KeyboardInput instance = null;

    public KeyboardInput(KeystrokeManager ksManger) {
        this.ksManager = ksManger;
    }

    private long press_key_time = 0;
    private long release_key_time = 0;

    private int current_Key_press = 0;
    private int current_x_pos = 0;
    private int current_y_pos = 0;
    private float current_pressure = 0;

    private JSONObject jo;

    public static KeyboardInput getInstance() {
        return instance;
    }

    public static KeyboardInput getInstance(KeystrokeManager ksManager)
    {
        if (instance == null)
            instance = new KeyboardInput(ksManager);
        return instance;
    }

    @Override
    public void onPressKey(int primaryCode, int repeatCount, boolean isSinglePointer) {
        current_Key_press = primaryCode;
        jo = new JSONObject();
        press_key_time = System.nanoTime();

        try {
            jo.put("NoKeyPressDelay",(release_key_time == 0) ?
                    (press_key_time - release_key_time) / 1e6 : null) // null if no key press before.
                    .put("primaryCode", primaryCode)
                    .put("PressureOnPress", current_pressure)
                    .put("XOnPress", current_x_pos)
                    .put("YOnPress", current_y_pos)
                    .put("LinearAccelerationOnPress", ksManager.sensors.GetLinearAcceleration())
                    .put("RotationVectorOnPress", ksManager.sensors.GetRotationVector());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReleaseKey(int primaryCode, boolean withSliding) {
        release_key_time = System.nanoTime();
        try {
            jo.put("KeyPressDelay", (release_key_time - press_key_time) / 1e6)
                    .put("PressureOnRelease", current_pressure)
                    .put("XOnRelease", current_x_pos)
                    .put("YOnRelease", current_y_pos)
                    .put("LinearAccelerationOnRelease", ksManager.sensors.GetLinearAcceleration())
                    .put("RotationVectorOnRelease", ksManager.sensors.GetRotationVector())
                    .put("vectorCoord", "X="+ (current_x_pos - jo.getInt("XOnPress"))+
                    ";Y="+ (current_y_pos - jo.getInt("YOnPress")));
            ksManager.addJsonElem(jo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCodeInput(int primaryCode, int x, int y, boolean isKeyRepeat) {
        /*try {
            jo.put("primaryCode", primaryCode)
                    .put("x", x)
                    .put("y", y);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        current_x_pos = (int)event.getX();
        current_y_pos = (int)event.getY();
        current_pressure = event.getPressure();
        /*System.out.println("PointerCount = "+event.getPointerCount());
        System.out.println("Pressure = "+event.getPressure(0));
        System.out.println("Y = "+y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
        }*/
    }

}
