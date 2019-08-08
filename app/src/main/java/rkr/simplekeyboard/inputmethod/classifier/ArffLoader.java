package rkr.simplekeyboard.inputmethod.classifier;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ArffLoader {
    private Instances train = null;
    final private String[] options = {
           // "-P", "100",
            "-I", "10",
            //"-num-slots",  "1",
            "-K", "0",
            //"-M", "1.0",
            //"-V", "0.001",
            "-S", "1"
    };
    final private RandomForest classifier = new RandomForest();

    public void LoadArffFileForLearning(Uri path, Context context) {
        try {
            if (path.getPath() != null) {
                String _path = path.getPath().substring(14);

                DataSource source = new DataSource(_path);
                this.train = source.getDataSet();
                if (train.classIndex() == -1) {
                    train.setClassIndex(train.numAttributes() - 1);
                }
                classifier.setOptions(options);
                classifier.buildClassifier(this.train);
                Toast.makeText(context, "Load for learning.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Oups: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void LoadArffFileForTesting(Uri path, Context context) {
        try {
            if (this.train != null) {
                if (path.getPath() != null) {
                    String _path = path.getPath().substring(14);

                    DataSource source = new DataSource(_path);
                    Instances test = source.getDataSet();

                    if (test.classIndex() == -1) {
                        test.setClassIndex(test.numAttributes() - 1);
                    }
                    Evaluation eval = new Evaluation(this.train);
                    eval.evaluateModel(classifier, test);
                    System.out.println(eval.toSummaryString());
                    System.out.println(eval.toMatrixString());
                    Toast.makeText(context, "Load for testing.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "The classifier has learn nothing yet.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Oups: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
