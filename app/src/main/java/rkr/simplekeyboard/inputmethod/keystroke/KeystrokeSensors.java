package rkr.simplekeyboard.inputmethod.keystroke;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Stack;


public class KeystrokeSensors implements SensorEventListener {

    private Stack<Float> acc_value_x = new Stack<>();
    private Stack<Float> acc_value_y = new Stack<>();
    private Stack<Float> acc_value_z = new Stack<>();

    private Stack<Float> rot_value_x = new Stack<>();
    private Stack<Float> rot_value_y = new Stack<>();
    private Stack<Float> rot_value_z = new Stack<>();

    KeystrokeSensors(Context context){
        final SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        final Sensor linear_acceleration = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final Sensor rotation_vector = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sensorManager.registerListener(this, linear_acceleration, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, rotation_vector, SensorManager.SENSOR_DELAY_UI);
    }

    JSONObject GetLinearAcceleration() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("x", new JSONArray(acc_value_x.toArray()))
        .put("y", new JSONArray(acc_value_y.toArray()))
        .put("z", new JSONArray(acc_value_z.toArray()));
        return jo;
    }

    JSONObject GetRotationVector() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("x", new JSONArray(rot_value_x.toArray()))
        .put("y", new JSONArray(rot_value_y.toArray()))
        .put("z", new JSONArray(rot_value_z.toArray()));
        return jo;
    }

    JSONObject GetAverageAcceleration() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("x", ComputeAverage(acc_value_x.toArray()))
                .put("y", ComputeAverage(acc_value_y.toArray()))
                .put("z", ComputeAverage(acc_value_z.toArray()));
        return jo;
    }

    JSONObject GetAverageRotationVector() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("x", ComputeAverage(rot_value_x.toArray()))
                .put("y", ComputeAverage(rot_value_y.toArray()))
                .put("z", ComputeAverage(rot_value_z.toArray()));
        return jo;
    }

    JSONObject GetStdDevAcceleration() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("x", ComputeStdDev(rot_value_x.toArray()))
                .put("y", ComputeStdDev(rot_value_y.toArray()))
                .put("z", ComputeStdDev(rot_value_z.toArray()));
        return jo;
    }

    JSONObject GetStdDevRotationVector() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("x", ComputeStdDev(rot_value_x.toArray()))
                .put("y", ComputeStdDev(rot_value_y.toArray()))
                .put("z", ComputeStdDev(rot_value_z.toArray()));
        return jo;
    }

    private static void PushInListLimited(Stack<Float> stack, Float value) {
        if (stack.size() > 5) {
            stack.remove(0);
        }
        stack.push(value);
    }

    private static float ComputeAverage(Object[] array) {
        float sum = 0;

        for (Object elem: array) {
            sum += (Float) elem;
        }

        if (array.length > 0)
            return sum / array.length;
        return 0;
    }

    private static double ComputeStdDev(Object[] array) {
        float average = ComputeAverage(array);
        float sum = 0;

        for (Object elem: array) {
            sum += Math.pow((Float) elem - average, 2);
        }

        if (array.length > 0)
            return Math.sqrt(sum / array.length);
        return 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {

            case (Sensor.TYPE_LINEAR_ACCELERATION):
                PushInListLimited(acc_value_x, event.values[0]);
                PushInListLimited(acc_value_y, event.values[1]);
                PushInListLimited(acc_value_z, event.values[2]);
                break;

            case (Sensor.TYPE_ROTATION_VECTOR):
                PushInListLimited(rot_value_x, event.values[0]);
                PushInListLimited(rot_value_y, event.values[1]);
                PushInListLimited(rot_value_z, event.values[2]);
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}