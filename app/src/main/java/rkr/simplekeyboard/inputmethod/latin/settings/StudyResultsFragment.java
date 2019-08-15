package rkr.simplekeyboard.inputmethod.latin.settings;

import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceScreen;

import java.util.Date;
import java.util.HashMap;

import rkr.simplekeyboard.inputmethod.R;
import rkr.simplekeyboard.inputmethod.classifier.ArffLoader;
import weka.classifiers.Evaluation;

public class StudyResultsFragment extends SubScreenFragment {

    private PreferenceScreen pScreen;

    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);

        try {
            addPreferencesFromResource(R.xml.prefs_screen_study_results);
            pScreen = getPreferenceScreen();

            ArffLoader loader = ArffLoader.getInstance();

            HashMap<Date, Evaluation> evaluations = loader.getEvaluations();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                evaluations.forEach(this::BuildTextBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void BuildTextBox(Date time, Evaluation eval) {
        try {
           String str = eval.toSummaryString() + '\n' + eval.toClassDetailsString() + '\n' +
                   eval.toCumulativeMarginDistributionString() + '\n' + eval.toMatrixString();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                LongSummaryTextBoxPreference p = new LongSummaryTextBoxPreference(this.pScreen.getContext());
                p.setTitle(time.toString());
                p.setSummary(str);
                this.pScreen.addPreference(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
