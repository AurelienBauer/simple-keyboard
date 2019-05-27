package rkr.simplekeyboard.inputmethod.keystroke;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;


public class KeystrokeManager {

    final private Context context;
    private FileOutputStream outputStream;
    private static final String FILENAME = "keystrokePatter.json";
    private JSONArray ja;
    private File file;
    private int newInput;
    private final Random rand;

    public KeystrokeManager(Context context) {
        this.context = context;
        rand = new Random();
        rand.setSeed(System.nanoTime());
    }

    public void onCreate() {
        try {
            //             System.out.println("Keystroke Mamager : onCreate.");
            file = new File(context.getFilesDir(), FILENAME); //create the file if it doesn't exist
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addJsonElem(JSONObject new_jo) {
        ja.put(new_jo);
    }
    //Called to inform the input method that text input has started in an editor.
    public void onStartInput() {
        //              System.out.println("Keystroke Mamager : onStartInput.");
        try {
            newInput = rand.nextInt();
            outputStream = new FileOutputStream(file, true);
            ja = new JSONArray();
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
                jo.put(newInput+"#", ja);


                String str = jo.toString();
                str = str.substring(0, str.length() - 1);
                // Concat two json object
                if (file.length() > 0) {
                    str = str.replaceFirst("\\{", ",");
                }

                outputStream.write(str.getBytes());
            }
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
