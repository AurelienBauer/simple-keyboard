package rkr.simplekeyboard.inputmethod.latin.settings;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import rkr.simplekeyboard.inputmethod.R;
import rkr.simplekeyboard.inputmethod.classifier.ArffLoader;

public class StudySettingsFragment  extends SubScreenFragment {

    private static final int READ_REQUEST_CODE_TRAIN = 42;
    private static final int READ_REQUEST_CODE_TEST = 84;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1092;
    private final ArffLoader loader = ArffLoader.getInstance();
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.prefs_screen_study);
        request_read_external_storage_right();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                filePickerListener();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            }
        }
    }

    private void request_read_external_storage_right() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this.getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            filePickerListener();
        }
    }

    private void filePickerListener() {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT); //Intent to start openIntents File Manager
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("*/*");

        Preference filePickerTrain = (Preference) findPreference("pref_arff_loader_train");
        filePickerTrain.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivityForResult(Intent.createChooser(intent, "Training"), READ_REQUEST_CODE_TRAIN);
                return true;
            }
        });

        Preference filePickerTest = (Preference) findPreference("pref_arff_loader_test");
        filePickerTest.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivityForResult(Intent.createChooser(intent, "Testing"), READ_REQUEST_CODE_TEST);
                return true;
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                if (uri != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (requestCode == READ_REQUEST_CODE_TRAIN) {
                            loader.LoadArffFileForLearning(uri, this.getContext());
                        } else if (requestCode == READ_REQUEST_CODE_TEST) {
                            loader.LoadArffFileForTesting(uri, this.getContext());
                        }
                    }
                }
            }
        }
    }
}
