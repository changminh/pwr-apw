package apw.classifiers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;
import apw.classifiers.c4_5.C4_5;
import apw.classifiers.c4_5.complex.ComplexSet;
import apw.classifiers.knn.KNN;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;

public abstract class ClassifierTest {

    /**
     * @param args
     */
    public static Samples[] divide(Samples s, double divideRate) {
        divideRate = Math.min(0.999, Math.max(0.001, divideRate));
        int size = Math.max((int) (s.size() * divideRate), 1);
        Random r = new Random(System.nanoTime());
        Samples newSamples = s.copyStructure();
        while (size-- > 0) {
            newSamples.add(s.remove(r.nextInt(s.size())));
        }
        return new Samples[]{s, newSamples};
    }

    public static double evaluateClassifier(Classifier classifier, Samples test) {
        int ok = 0;
        for (Sample sample : test) {
            Object result = classifier.classifySampleAsObject(sample);
            Object correct = sample.classAttributeInt();
            if (result.equals(correct)) {
                ok++;
            }
        }
        return (double) ok / (double) test.size();
    }

    public static void main(String[] args) {
        File f = new File("data/soybean.arff");
        Random r = new Random(System.nanoTime());
        System.out.println("Loading data.");
        ARFFLoader l;
        try {
            l = new ARFFLoader(f);
            Samples s = l.getSamples();
            Samples test = s.copyStructure();
            for (int i = 0; i < Math.min(s.size() / 3, 100); i++) {
                test.add(s.remove(r.nextInt(s.size())));
            }
            System.out.println("Dataset size: " + s.size() + ". Building classifier.");
            System.out.println("KNN_1 RATE:" + evaluateClassifier(new KNN(s), test));
            System.out.println("KNN_SIMPLE RATE:" + evaluateClassifier(new KNN(s, 4, KNN.SIMPLE_VOTING), test));
            System.out.println("KNN_DISTANCE RATE:" + evaluateClassifier(new KNN(s, 4, KNN.DISTANCE_BASED_VOTING), test));
            System.out.println("KNN_RANKING RATE:" + evaluateClassifier(new KNN(s, 4, KNN.RANKING_BASED_VOTING), test));
            System.out.println("C4_5 RATE:" + evaluateClassifier(new C4_5(s), test));
            System.out.println("C4_5_TRIMMED RATE:" + evaluateClassifier(new ComplexSet(s), test));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
