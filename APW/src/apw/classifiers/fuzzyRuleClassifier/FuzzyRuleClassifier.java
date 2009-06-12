/*
 *  Copyright (c) 2009, Wrocław University of Technology
 *  All rights reserved.
 *  Redistribution  and use in source  and binary  forms,  with or
 *  without modification,  are permitted provided that the follow-
 *  ing conditions are met:
 *
 *   • Redistributions of source code  must retain the above copy-
 *     right  notice, this list  of conditions and  the  following
 *     disclaimer.
 *   • Redistributions  in binary  form must  reproduce the  above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the  documentation and/or other mate provided
 *     with the distribution.
 *   • Neither  the name of the  Wrocław University of  Technology
 *     nor the names of its contributors may be used to endorse or
 *     promote products derived from this  software without speci-
 *     fic prior  written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRI-
 *  BUTORS "AS IS" AND ANY  EXPRESS OR IMPLIED WARRANTIES, INCLUD-
 *  ING, BUT NOT  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTA-
 *  BILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 *  NO EVENT SHALL THE COPYRIGHT HOLDER OR  CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT,  INCIDENTAL, SPECIAL,  EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCURE-
 *  MENT OF SUBSTITUTE  GOODS OR SERVICES;  LOSS OF USE,  DATA, OR
 *  PROFITS; OR BUSINESS  INTERRUPTION) HOWEVER  CAUSED AND ON ANY
 *  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 *  TORT (INCLUDING  NEGLIGENCE  OR  OTHERWISE) ARISING IN ANY WAY
 *  OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSI-
 *  BILITY OF SUCH DAMAGE.
 */
package apw.classifiers.fuzzyRuleClassifier;

import apw.classifiers.Classifier;
import apw.classifiers.RuleClassifier;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *
 * @author Przemek Woś
 */
public class FuzzyRuleClassifier extends RuleClassifier {

    protected Samples samples = null;
    protected String[] options;
    protected Genom bestResult = null;
    protected int defaultNumber = 12;
    protected int rulesPerClass = 5;
    protected double mutationProb = 0.05;
    protected double crossProb = 0.2;

    private static class Utils {

        public static int getOptionPos(char flag, String[] options) {
            return getOptionPos("" + flag, options);
        }

        public static int getOptionPos(String flag, String[] options) {
            if (options == null) {
                return -1;
            }

            for (int i = 0; i < options.length; i++) {
                if ((options[i].length() > 0) && (options[i].charAt(0) == '-')) {
                    // Check if it is a negative number
                    try {
                        Double.valueOf(options[i]);
                    } catch (NumberFormatException e) {
                        // found?
                        if (options[i].equals("-" + flag)) {
                            return i;
                        }
                        // did we reach "--"
                        if (options[i].charAt(1) == '-') {
                            return -1;
                        }
                    }
                }
            }

            return -1;
        }

        public static String getOption(char flag, String[] options)
                throws Exception {

            return getOption("" + flag, options);
        }

        public static String getOption(String flag, String[] options)
                throws Exception {

            String newString;
            int i = getOptionPos(flag, options);

            if (i > -1) {
                if (options[i].equals("-" + flag)) {
                    if (i + 1 == options.length) {
                        throw new Exception("No value given for -" + flag + " option.");
                    }
                    options[i] = "";
                    newString = new String(options[i + 1]);
                    options[i + 1] = "";
                    return newString;
                }
                if (options[i].charAt(1) == '-') {
                    return "";
                }
            }

            return "";
        }
    }

    public void setOptions(String[] options) throws Exception {
        String data = Utils.getOption('o', options);

        if (data.length() != 0) {
            defaultNumber = Integer.parseInt(data);
        }

        data = Utils.getOption('r', options);

        if (data.length() != 0) {
            this.rulesPerClass = Integer.parseInt(data);
        }

        data = Utils.getOption('f', options);

        if (data.length() != 0) {
            Genom.numberOfSets = Integer.parseInt(data);
        }

        data = Utils.getOption('t', options);

        if (data.length() != 0) {
            Genom.setType = Integer.parseInt(data);
        }

        data = Utils.getOption('b', options);

        if (data.length() != 0) {
            Genom.beta = Double.parseDouble(data);
        }

        data = Utils.getOption('e', options);

        if (data.length() != 0) {
            Genom.eps = Double.parseDouble(data);
        }

        data = Utils.getOption('d', options);

        if (data.length() != 0) {
            Genom.delta = Double.parseDouble(data);
        }

        data = Utils.getOption('z', options);

        if (data.length() != 0) {
            Genom.dzeta = Double.parseDouble(data);
        }

        data = Utils.getOption('m', options);

        if (data.length() != 0) {
            this.mutationProb = Double.parseDouble(data);
        }

        data = Utils.getOption('c', options);

        if (data.length() != 0) {
            this.crossProb = Double.parseDouble(data);
        }

        this.options = options;
    }

    //************************* constructors *********************************
    public FuzzyRuleClassifier(Samples S) {
        super(S);
        samples = this.normalize(S);
        // genoms = new Genom[defaultNumber];
        options = new String[]{};
        //HashMap<String, ArrayList<Integer>>[] tmp = this.partionData(0.2);
        //learningSet = tmp[0];
        //testSet = tmp[1];

        double[] m = this.findMinMax(samples);
        RandomClass.setMin(m[0]);
        RandomClass.setMax(m[1]);

    }

    public FuzzyRuleClassifier(File input) throws IOException, ParseException {
        this(new ARFFLoader(input).getSamples());
    }

    public FuzzyRuleClassifier(InputStream input) throws IOException, ParseException {
        this(new ARFFLoader(input).getSamples());
    }

    public FuzzyRuleClassifier(String source) throws IOException, ParseException {
        this(new File(source));
    }
    //************************ end ******************************************

    private int howManyInputs() {
        return (this.samples.get(0).size() - 1);
    }

    private Object[] howManyClasses() {
        HashMap<String, Integer> data = new HashMap<String, Integer>();

        for (int i = 0; i < this.samples.size(); i++) {
            int size = samples.get(i).size();
            String str = samples.get(i).get(size - 1).toString();

            if (!data.containsKey(str)) {
                data.put(str, 0);
            } else {
                int value = data.get(str).intValue();
                data.put(str, value + 1);
            }
        }

        return data.keySet().toArray();
    }
    
    @Override
    public void addSamples(Samples s) {
        this.samples.addAll(s);
    }

    @Override
    public void addSample(Sample s) {
        this.samples.add(s);
    }

    @Override
    public double[] classifySample(Sample s) {

        if (this.bestResult == null) {
            return null;
        }

        String result = this.bestResult.classify(s);
        double[] r = new double[result.length()];

        for (int i = 0; i < r.length; i++) {
            r[i] = (double) (result.charAt(i));
        }

        return r;
    }

    public String interprate(double[] data) {
        String result = new String("");

        for (int i = 0; i < data.length; i++) {
            result += (char) (data[i]);
        }
        return result;
    }

    @Override
    public void rebuild() {
        buildClassifier();
    }

    @Override
    public Classifier copy() {
        try {
            FuzzyRuleClassifier fuzzy = new FuzzyRuleClassifier(samples);
            fuzzy.setOptions(getOptions());
            fuzzy.bestResult = new Genom(this.bestResult);
            return fuzzy;
        } catch (Exception ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

   
    public String[] getOptions() {
        String[] _options = new String[options.length];

        for (int i = 0; i < _options.length; i++) {
            _options[i] = new String(options[i]);
        }

        return _options;
    }

    private Samples normalize(Samples nSamples) {
        double length;
        Samples _samples = new Samples(nSamples.getAtts());

        for (int i = 0; i < nSamples.size(); i++) {
            length = 0.0;
            int size = nSamples.get(i).size() - 1;

            for (int j = 0; j < size; j++) {
                double data = Double.parseDouble(nSamples.get(i).get(j).toString());
                length += data * data;
            }

            length = Math.sqrt(length);

            List<Object> list = new ArrayList<Object>();

            for (int j = 0; j < size; j++) {
                double data = Double.parseDouble(nSamples.get(i).get(j).toString());
                data /= length;
                String str = Double.toString(data);
                Object obj = nSamples.getAtts().get(j).getRepresentation(str);
                list.add(obj);
            }

            Object obj = nSamples.getAtts().get(size).
                    getRepresentation(nSamples.get(i).get(size).toString());

            list.add(obj);

            Samples _s = new Samples(nSamples.getAtts());
            Sample newSample = new Sample(_s, list.toArray());
            _samples.add(newSample);
        }

        return _samples;
    }

    public String getInfo() {
        return "Fuzzy Rule Classifier for APW Project, made by Przemek Woś...";
    }

    private ArrayList<Genom> roullet(ArrayList<Genom> x, final int howMany) {
        double E = 0.0;

        for (int i = 0; i < x.size(); i++) {
            E += x.get(i).fitness();
        }

        double[] p = new double[x.size()];
        double[] q = new double[x.size()];

        for (int i = 0; i < p.length; i++) {
            p[i] = x.get(i).fitness() / E;
            q[i] = 0.0;
            for (int j = 0; j <= i; j++) {
                q[i] += p[j];
            }
        }

        ArrayList<Genom> result = new ArrayList<Genom>(howMany);

        for (int i = 0; i < howMany; i++) {
            double value = Math.random();

            if (value <= q[0]) {
                result.add(x.get(0));
            } else {
                if (value > q[q.length - 1]) {
                    result.add(x.get(x.size() - 1));
                } else {
                    for (int j = 1; j < x.size(); j++) {
                        if ((value <= q[j]) && (value > q[j - 1])) {
                            result.add(x.get(j));
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    private ArrayList<Genom> getNewPapulation(ArrayList<Genom> x, final int howMany) {
        ArrayList<Genom> result = new ArrayList<Genom>(howMany);

        for (int i = 0; i < howMany; i++) {
            result.add(x.get(i));
        }
        return result;
    }

    private double[] findMinMax(Samples _s) {
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < _s.size(); i++) {
            for (int j = 0; j < _s.get(i).size() - 1; j++) {
                double value = Double.valueOf(_s.get(i).get(j).toString()).doubleValue();
                if (max < value) {
                    max = value;
                }
                if (min > value) {
                    min = value;
                }
            }
        }

        return new double[]{min, max};
    }

    private boolean gradeFunction(ArrayList<Genom> gens) {
        for (int i = 0; i < gens.size(); i++) {
            gens.get(i).setCorr(0);
            gens.get(i).setIncorr(0);
            gens.get(i).setUnClass(0);

            for (int j = 0; j < samples.size(); j++) {
                String result = gens.get(i).classify(samples.get(j));

                if (result == null) {
                    gens.get(i).setUnClass(gens.get(i).getUnClass() + 1);
                } else {
                    int index = samples.get(j).size() - 1;

                    if (result.compareTo(samples.get(j).get(index).toString()) == 0) {
                        gens.get(i).setCorr(gens.get(i).getCorr() + 1);
                    } else {
                        gens.get(i).setIncorr(gens.get(i).getIncorr() + 1);
                    }
                }
            }

            if ((gens.get(i).getIncorr() == 0.0) && (gens.get(i).getUnClass() == 0.0)) {
                return true;
            }
        }
        return false;
    }

    public void buildClassifier() {
        ArrayList<Genom> gens = new ArrayList<Genom>();

        for (int i = 0; i < defaultNumber; i++) {
            gens.add(new Genom(this.rulesPerClass, howManyInputs(), howManyClasses()));
        }

        int generation = 0;
        final int maxEpos = 10000;
        boolean theBestFound = false;

        ArrayList<Genom> parants;

        gradeFunction(gens);
        Collections.sort(gens);

        do {

            int size = gens.size() / 2;
            parants = roullet(gens, size);

            for (int i = 0; i < size / 2; i++) {
                if (Math.random() <= this.crossProb) {
                    Genom o1 = parants.get(2 * i);     //0,2,4
                    Genom o2 = parants.get(2 * i + 1); //1,3,5
                    Genom[] result = o1.crossWith(o2);
                    gens.add(result[0]);
                    gens.add(result[1]);
                }
            }

            for (int i = 0; i < gens.size(); i++) {
                if (Math.random() <= mutationProb) {
                    gens.add(gens.get(i).mutate());
                }
            }


            if (!gradeFunction(gens)) {
                Collections.sort(gens);
                gens = getNewPapulation(gens, defaultNumber);
            } else {
                theBestFound = true;
            }


            System.out.println("Pokolenie: " + generation +
                    " Gen: " + 0 +
                    " Corr: " + gens.get(0).getCorr() +
                    " Incorr: " + gens.get(0).getIncorr() +
                    " unClass: " + gens.get(0).getUnClass() +
                    " Rules: " + (int) gens.get(0).getPrem() +
                    " Fsets: " + (int) gens.get(0).getFsets() +
                    " Fitness : " + gens.get(0).fitness() +
                    " Statistic: " + (100.0 * gens.get(0).getCorr() / samples.size()));


        } while ((++generation < maxEpos) && !theBestFound);

        Collections.sort(gens);
        bestResult = gens.get(0);

    }

    public Genom[] makePapulation() {
        Genom[] gen = new Genom[defaultNumber];

        for (int i = 0; i < gen.length; i++) {
            gen[i] = new Genom(5, howManyInputs(), howManyClasses());
        }

        return gen;
    }

    public static void main(String[] arg) {

        try {
            FuzzyRuleClassifier fuzzy = new FuzzyRuleClassifier("d:/svm/data/iris.arff");

            fuzzy.setOptions(new String[]{"-o", "20", "-r", "5",
                        "-f", "5", "-t", "0",
                        "-m", "0.3", "-c", "0.8",
                        "-b", "0.5", "-d", "0.4",
                        "-e", "0.4", "-z", "0.3",});

            fuzzy.buildClassifier();



        } catch (IOException ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @Override
    public String[] getRules() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
