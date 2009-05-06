package apw.classifiers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;

public class KNN extends Classifier {

    private ArrayList<Attribute> attributes;
    private int k;
    private LinkedList<Sample> toAdd = new LinkedList<Sample>();
    private LinkedList<Sample> samples = new LinkedList<Sample>();
    private Samples samples_desc;
    private int[] sizes;// table stores sizes of attributes
	/*
     * if attribute[i] is Numeric, size[i]=0
     * if attribute[i] is Nominal, size[i]=number of classes
     * */
    double[] max;//maximum values of attributes
    double[] min;//minimum values of attributes

    /**
     * @author Krzysztof Praszmo
     */
    /**
     * Creates new KNN Classifier based on given Samples with number of
     * neighbors equals 1
     *
     * @param samples -
     *            training examples
     * @see Classifier
     */
    public KNN(Samples samples) {
        super(samples);
        samples_desc = samples;
        this.attributes = samples.getAtts();
        this.k = 1;
        for (Sample sample : samples)
            toAdd.add(sample);
        init();
    }

    /**
     * Creates new KNN Classifier based on given Samples and number of neighbors
     *
     * @param samples -
     *            training examples
     * @param k -
     *            number of nearest neighbors
     * @see Classifier
     */
    public KNN(Samples samples, int k) {
        super(samples);
        samples_desc = samples;

        this.attributes = samples.getAtts();
        this.k = k;
        for (Sample sample : samples)
            toAdd.add(sample);
        init();
    }

    private void init() {
        max = new double[toAdd.get(0).toArray().length];
        min = new double[toAdd.get(0).toArray().length];
        Arrays.fill(max, -Double.MAX_VALUE);
        Arrays.fill(min, Double.MAX_VALUE);
        rebuild();
    }

    @Override
    public void rebuild() {
        while (!toAdd.isEmpty()) {
            Sample s = toAdd.pop();
            samples.add(s);
            Object[] vals = s.toArray();
            for (int i = 0; i < vals.length; i++) {
                max[i] = Math.max(max[i], (Double) vals[i]);
                min[i] = Math.min(min[i], (Double) vals[i]);
            }
        }

    }

    @Override
    public void addSample(Sample s) throws UnsupportedOperationException {
        toAdd.add(s);
    }

    @Override
    public void addSamples(Samples s) throws UnsupportedOperationException {
        toAdd.addAll(s);
    }

    @Override
    public double[] classifySample(Sample s) {
        LinkedList<KNNSortableSample> bests = new LinkedList<KNNSortableSample>();
        Object[] vals = s.toArray();
        double[] dvals = new double[vals.length];
        for (int i = 0; i < vals.length; i++)
            if (max[i] == min[1])
                dvals[i] = 0;
            else
                dvals[i] = ((Double) vals[i] - min[i]) / (max[i] - min[i]);

        for (Sample sample : samples) {
            double r = dif(sample, dvals);

            if (bests.size() < k)
                bests.add(new KNNSortableSample(r, sample));
            else if (bests.get(0).getRange() > r)
                bests.add(new KNNSortableSample(r, sample));
            if (bests.size() > k + 20) {
                Collections.sort(bests);
                while (bests.size() != k) {
                    bests.remove(0);
                }
            }

        }
        Collections.sort(bests);
        while (bests.size() != k) {
            bests.remove(0);
        }


        String[] keys = ((Nominal) samples_desc.getClassAttribute()).getSortedIKeys();
        double[] result = new double[keys.length];

        for (KNNSortableSample knnss : bests) {
            String key = (String) knnss.getSample().get(attributes.indexOf(samples_desc.getClassAttribute()));
            for (int i = 0; i < result.length; i++)
                if (keys[i].equals(key))
                    result[i] += 1.0;
        }

//        System.out.println("Classification result:");
//
//        for (int i = 0; i < result.length; i++)
//            System.out.println(keys[i] + " " + result[i]);

        return result;

    }

    private double dif(Sample s, double[] vals) {
        double result = 0;
        Object[] svals = s.toArray();
        double[] dvals = new double[svals.length];
        for (int i = 0; i < vals.length; i++)
            if (max[i] == min[1])
                dvals[i] = 0;
            else
                dvals[i] = ((Double) svals[i] - min[i]) / (max[i] - min[i]);
        for (int i = 0; i < vals.length; i++)
            result += Math.abs(dvals[i] - vals[i]);
        return result;
    }

    @Override
    public Classifier copy() {
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String[] args) {
        // TODO to be removed
        File f = new File("data/shuttle.arff");
        try {
            System.out.println("Loading data.");
            ARFFLoader l = new ARFFLoader(f);
            Samples s = l.getSamples();
            Random r = new Random(System.nanoTime());
//            LinkedList<Sample> test = new LinkedList<Sample>();
//            for (int i = 0; i < 20; i++)
//                test.add(s.remove(r.nextInt(s.size())));
//
            System.out.println("Dataset size: " + s.size() + ". Building classifier.");
            KNN myKNN = new KNN(s, 3);
//
//            for (Sample sample : test) {
//                System.out.println("class " + Arrays.toString(myKNN.classifySample(sample)));
//                System.out.println();
//                System.out.println("And the correct answer is: " + sample.classAttributeInt());
//                System.out.println();
//            }

            System.out.println("Testing classification rate.");
            System.out.println("Classification rate " + s.getCorrectClassificationRate(myKNN));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
