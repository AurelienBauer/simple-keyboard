package rkr.simplekeyboard.inputmethod.classifier;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ArffLoader {
    private static ArffLoader instance = new ArffLoader();
    private Instances train = null;
    private Instances structure = null;
    private HashMap<Date, Evaluation> evaluations = new HashMap<>();
    final private RandomForest classifier = new RandomForest();
    private  Evaluation eval = null;

    public static ArffLoader getInstance() {
        return instance;
    }

    private ArffLoader() {};

    public void LoadArffFileForLearning(Uri path, Context context) {
        try {
            if (path.getPath() != null) {
                String _path = path.getPath().substring(14);

                DataSource source = new DataSource(_path);
                this.structure = source.getStructure();
                this.train = source.getDataSet();
                if (train.classIndex() == -1) {
                    train.setClassIndex(train.numAttributes() - 1);
                }
                classifier.setBatchSize(String.valueOf(100));
                classifier.setNumIterations(100);
                classifier.setNumExecutionSlots(1);
                classifier.setNumDecimalPlaces(2);
                classifier.setMaxDepth(0);
                classifier.setSeed(1);
                classifier.buildClassifier(this.train);
                this.eval = new Evaluation(this.train);
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
                    ProcessAnEvaluation(test);
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

    private void ProcessAnEvaluation(Instances test) throws Exception {
        eval.evaluateModel(classifier, test);
        this.evaluations.put(Calendar.getInstance().getTime(), eval);
        System.out.println(eval.toSummaryString());
        System.out.println(eval.toClassDetailsString());
       // System.out.println(eval.toCumulativeMarginDistributionString());
        System.out.println(eval.toMatrixString());
    }

    public void JsonToInstance(JSONObject jo) {
        if (this.structure != null) {
            int attributesNbr = train.numAttributes();
            if (train.classIndex() == -1) {
                attributesNbr--;
            }
            try {
                Instance newdata = new DenseInstance(attributesNbr);
                newdata.setDataset(this.train);
                // newdata.setValue(0, (int) jo.get("primaryCode"));
                // newdata.setValue(1, (int) jo.get("XOnPress"));
                // newdata.setValue(2, (int) jo.get("XOnRelease"));
                // newdata.setValue(3, (int) jo.get("YOnPress"));
                // newdata.setValue(4, (int) jo.get("YOnRelease"));
                // newdata.setValue(1, (double) jo.get("NoKeyPressDelay"));
                // newdata.setValue(2, (double) jo.get("KeyPressDelay"));
                // newdata.setValue(5, (double) jo.get("UpUpTime"));
                // newdata.setValue(6, (double) jo.get("DownDownTime"));
                // newdata.setValue(7, (double) jo.get("LatencyTime"));
//
                newdata.setValue(0, (double) ((JSONObject)jo.get("AverageRotationVectorOnPress")).get("x"));
                newdata.setValue(1, (double) ((JSONObject)jo.get("AverageRotationVectorOnPress")).get("y"));
                newdata.setValue(2, (double) ((JSONObject)jo.get("AverageRotationVectorOnPress")).get("z"));
                newdata.setValue(3, (double) ((JSONObject)jo.get("AverageRotationVectorOnRelease")).get("x"));
                newdata.setValue(4, (double) ((JSONObject)jo.get("AverageRotationVectorOnRelease")).get("y"));
                newdata.setValue(5, (double) ((JSONObject)jo.get("AverageRotationVectorOnRelease")).get("z"));
                // newdata.setValue(14, (double) ((JSONObject)jo.get("AverageLinearAccelerationOnPress")).get("x"));
                // newdata.setValue(15, (double) ((JSONObject)jo.get("AverageLinearAccelerationOnPress")).get("y"));
                // newdata.setValue(16, (double) ((JSONObject)jo.get("AverageLinearAccelerationOnPress")).get("z"));
                // newdata.setValue(17, (double) ((JSONObject)jo.get("AverageLinearAccelerationOnRelease")).get("x"));
                // newdata.setValue(18, (double) ((JSONObject)jo.get("AverageLinearAccelerationOnRelease")).get("y"));
                // newdata.setValue(19, (double) ((JSONObject)jo.get("AverageLinearAccelerationOnRelease")).get("z"));
//
                // newdata.setValue(20, (double) ((JSONObject)jo.get("StdDevRotationVectorOnPress")).get("x"));
                // newdata.setValue(21, (double) ((JSONObject)jo.get("StdDevRotationVectorOnPress")).get("y"));
                // newdata.setValue(22, (double) ((JSONObject)jo.get("StdDevRotationVectorOnPress")).get("z"));
                // newdata.setValue(23, (double) ((JSONObject)jo.get("StdDevRotationVectorOnRelease")).get("x"));
                // newdata.setValue(24, (double) ((JSONObject)jo.get("StdDevRotationVectorOnRelease")).get("y"));
                // newdata.setValue(25, (double) ((JSONObject)jo.get("StdDevRotationVectorOnRelease")).get("z"));
                // newdata.setValue(26, (double) ((JSONObject)jo.get("StdDevLinearAccelerationOnPress")).get("x"));
                // newdata.setValue(27, (double) ((JSONObject)jo.get("StdDevLinearAccelerationOnPress")).get("y"));
                // newdata.setValue(28, (double) ((JSONObject)jo.get("StdDevLinearAccelerationOnPress")).get("z"));
                // newdata.setValue(29, (double) ((JSONObject)jo.get("StdDevLinearAccelerationOnRelease")).get("x"));
                // newdata.setValue(30, (double) ((JSONObject)jo.get("StdDevLinearAccelerationOnRelease")).get("y"));
                // newdata.setValue(31, (double) ((JSONObject)jo.get("StdDevLinearAccelerationOnRelease")).get("z"));

                newdata.setValue(6, (int) jo.get("vectorCoordX"));
                newdata.setValue(7, (int) jo.get("vectorCoordY"));
                newdata.setValue(8,1);


                this.eval.evaluateModelOnce(classifier, newdata);
                System.out.println(eval.toSummaryString());
                System.out.println(eval.toClassDetailsString());
                System.out.println(eval.toMatrixString());
                // System.out.println(eval.toCumulativeMarginDistributionString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<Date, Evaluation> getEvaluations() {
        return evaluations;
    }
}
