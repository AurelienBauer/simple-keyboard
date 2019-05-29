package rkr.simplekeyboard.inputmethod.keystroke;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

class KeystrokeSensors implements SensorEventListener {

    private final Sensor linear_acceleration;

    KeystrokeSensors(Context context){
        final SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        linear_acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void GetLinearAcceleration() {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        System.out.println(event.getClass());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
