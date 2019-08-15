package rkr.simplekeyboard.inputmethod.keystroke;

import android.view.MotionEvent;

import org.json.JSONObject;

import rkr.simplekeyboard.inputmethod.classifier.ArffLoader;


public class KeyboardInput implements KeystrokeActionListener {

    private KeystrokeManager ksManager;
    private static KeyboardInput instance = null;

    public KeyboardInput(KeystrokeManager ksManger) {
        this.ksManager = ksManger;
    }

    private long press_key_time = 0;
    private long release_key_time = 0;
    private double old_press_key_delay = -1;
    private int current_x_pos = 0;
    private int current_y_pos = 0;
    private float current_pressure = 0;
    private final ArffLoader arffLoader = ArffLoader.getInstance();

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
        jo = new JSONObject();
        press_key_time = System.nanoTime();
        try {
            jo
                    .put("NoKeyPressDelay",(release_key_time == 0) ?
                    null : (press_key_time - release_key_time) / 1e6) // null if no key press before.
                    .put("primaryCode", primaryCode)
                    .put("PressureOnPress", current_pressure)
                    .put("XOnPress", current_x_pos)
                    .put("YOnPress", current_y_pos)
                    .put("LinearAccelerationOnPress", ksManager.sensors.GetLinearAcceleration())
                    .put("RotationVectorOnPress", ksManager.sensors.GetRotationVector())
                    .put("AverageLinearAccelerationOnPress", ksManager.sensors.GetAverageAcceleration())
                    .put("AverageRotationVectorOnPress", ksManager.sensors.GetAverageRotationVector())
                    .put("StdDevLinearAccelerationOnPress", ksManager.sensors.GetStdDevAcceleration())
                    .put("StdDevRotationVectorOnPress", ksManager.sensors.GetStdDevRotationVector());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReleaseKey(int primaryCode, boolean withSliding) {
        release_key_time = System.nanoTime();
        try {
            jo
                    .put("KeyPressDelay", (release_key_time - press_key_time) / 1e6)
                    .put("PressureOnRelease", current_pressure)
                    .put("XOnRelease", current_x_pos)
                    .put("YOnRelease", current_y_pos)
                    .put("LinearAccelerationOnRelease", ksManager.sensors.GetLinearAcceleration())
                    .put("RotationVectorOnRelease", ksManager.sensors.GetRotationVector())
                    .put("AverageLinearAccelerationOnRelease", ksManager.sensors.GetAverageAcceleration())
                    .put("AverageRotationVectorOnRelease", ksManager.sensors.GetAverageRotationVector())
                    .put("StdDevLinearAccelerationOnRelease", ksManager.sensors.GetStdDevAcceleration())
                    .put("StdDevRotationVectorOnRelease", ksManager.sensors.GetStdDevRotationVector())
                    .put("vectorCoord", "X="+ (current_x_pos -
                            jo.getInt("XOnPress"))+
                    ";Y="+ (current_y_pos - jo.getInt("YOnPress")))
                    .put("vectorCoordX", current_x_pos - jo.getInt("XOnPress"))
                    .put("vectorCoordY", current_y_pos - jo.getInt("YOnPress"))
                    .put("UpUpTime", jo.getDouble("NoKeyPressDelay") +
                            jo.getDouble("KeyPressDelay"));
            if (old_press_key_delay != -1) {
                jo
                        .put("DownDownTime", old_press_key_delay + jo.getDouble("NoKeyPressDelay"))
                        .put("LatencyTime", old_press_key_delay +
                                jo.getDouble("NoKeyPressDelay") +
                                jo.getDouble("KeyPressDelay"));
            }
            arffLoader.JsonToInstance(jo);
            ksManager.addJsonElem(jo);
            old_press_key_delay = (release_key_time - press_key_time) / 1e6;
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
    }

    public void setKsManager(KeystrokeManager ksManager) {
        this.ksManager = ksManager;
    }

}
