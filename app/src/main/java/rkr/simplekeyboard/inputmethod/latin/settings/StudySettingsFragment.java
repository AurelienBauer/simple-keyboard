package rkr.simplekeyboard.inputmethod.latin.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;

import rkr.simplekeyboard.inputmethod.R;
import rkr.simplekeyboard.inputmethod.classifier.ArffLoader;

public class StudySettingsFragment  extends SubScreenFragment {

    private static final int READ_REQUEST_CODE = 42;
    private final ArffLoader loader = new ArffLoader();
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.prefs_screen_study);

        filePickerListener();
    }

    private void filePickerListener() {
        Preference filePicker = (Preference) findPreference("pref_arff_loader");
        filePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent to start openIntents File Manager
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Pick arff file"), READ_REQUEST_CODE);
                return true;
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                if (uri != null) {
                    loader.LoadArffFile(uri);
                    Log.i("INFO", "Uri: " + uri.toString());

                }
            }
        }
    }
}
