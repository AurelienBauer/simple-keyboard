package rkr.simplekeyboard.inputmethod.keystroke;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.Random;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;


public class KeystrokeManager {

    final private Context context;
    private static final String FILENAME = "keystrokePatter.json";
    private JSONArray ja = null;
    private File file;
    private int newInput = -1;
    private int orientation = -1;
    private final Random rand;
    KeystrokeSensors sensors;

    public KeystrokeManager(Context context) {
        this.context = context;
        rand = new Random();
        rand.setSeed(System.nanoTime());
        sensors = new KeystrokeSensors(context);
    }

    public void onCreate() {
        try {
            //             System.out.println("Keystroke Mamager : onCreate.");
            file = new File(context.getFilesDir(), FILENAME); //create the file if it doesn't exist
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addJsonElem(JSONObject new_jo) {
        orientation = context.getResources().getConfiguration().orientation;
        ja.put(new_jo);
    }
    //Called to inform the input method that text input has started in an editor.
    public void onStartInput() {
        //              System.out.println("Keystroke Mamager : onStartInput.");
        try {
            if (ja == null) {
                newInput = rand.nextInt();
                ja = new JSONArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        //          System.out.println("Keystroke Mamager : onDestroy.");
    }

    public void onFinishInput() {
        //          System.out.println("Keystroke Mamager : onFinishInput.");
        try {
            if (ja.length() > 0) {
                JSONObject jo = new JSONObject();
                jo.put(newInput + "#" + orientation, ja);
                String str = jo.toString();
                RandomAccessFile raf = new RandomAccessFile(file,"rwd");

                // Concat two json object (string format)
                if (file.length() > 0) {
                    raf.setLength(raf.length()-1);
                    str = str.replaceFirst("\\{", ",");
                }

                FileOutputStream outputStream = new FileOutputStream(file, true);
                outputStream.write(str.getBytes());
                outputStream.close();
                ja = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
