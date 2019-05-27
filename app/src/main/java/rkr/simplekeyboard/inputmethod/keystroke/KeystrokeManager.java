package rkr.simplekeyboard.inputmethod.keystroke;

import java.io.File;
import java.io.FileOutputStream;
import android.content.Context;

import org.json.JSONObject;


public class KeystrokeManager {

    final private Context context;
    private FileOutputStream outputStream;
    private static final String FILENAME = "keystrokePatter.json";
    public JSONObject jo;

    public KeystrokeManager(Context context) {
        this.context = context;
    }

    public void onCreate() {
        System.out.println("Keystroke Mamager : onCreate.");
        File file = new File(context.getFilesDir(), FILENAME);
    }

    //Called to inform the input method that text input has started in an editor.
    public void onStartInput() {
        System.out.println("Keystroke Mamager : onStartInput.");
        try {
            outputStream = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            jo = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    public void newEntry(final String pattern) {
        try {
            outputStream.write(pattern.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void onDestroy() {
        System.out.println("Keystroke Mamager : onDestroy.");
    }

    public void onFinishInput() {
        System.out.println("Keystroke Mamager : onFinishInput.");
        try {
            outputStream.write(jo.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
