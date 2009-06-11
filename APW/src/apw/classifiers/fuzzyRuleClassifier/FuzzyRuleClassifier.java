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
    protected Genom[] genoms = null;
    protected String[] options;
    protected HashMap<String, ArrayList<Integer>> learningSet = null;
    protected HashMap<String, ArrayList<Integer>> testSet = null;
    protected Genom bestResult = null;

    //************************* constructors *********************************
    public FuzzyRuleClassifier(Samples S) {
        super(S);
        samples = this.normalize(S);
        genoms = new Genom[defaultNumber];
        options = new String[]{};
        HashMap<String, ArrayList<Integer>>[] tmp = this.partionData(0.2);
        learningSet = tmp[0];
        testSet = tmp[1];

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

    private HashMap<String, ArrayList<Integer>>[] partionData(double procent) {

        if ((procent < 0) && (procent >= 1.0)) {
            throw new RuntimeException("Parametr metody partionData posiada nie prawidłową wartość: " + procent);
        }

        HashMap<String, ArrayList<Integer>>[] data = new HashMap[2];

        data[0] = new HashMap<String, ArrayList<Integer>>();

        for (int i = 0; i < this.samples.size(); i++) {
            int size = samples.get(i).size();
            String str = samples.get(i).get(size - 1).toString();

            if (!data[0].containsKey(str)) {
                data[0].put(str, new ArrayList<Integer>());
            } else {
                data[0].get(str).add(i);
            }
        }

        Object[] s = data[0].keySet().toArray();

        data[1] = new HashMap<String, ArrayList<Integer>>();

        for (int i = 0; i < s.length; i++) {
            data[1].put(s[i].toString(), new ArrayList<Integer>());
            int howMany = (int) (data[0].get(s[i].toString()).size() * procent);

            int classSize = data[0].get(s[i].toString()).size() - 1;

            for (int j = classSize; j >= (classSize - howMany); j--) {
                data[1].get(s[i].toString()).add(data[0].get(s[i].toString()).get(j));
                data[0].get(s[i].toString()).remove(j);
            }

        }

        return data;
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
        FuzzyRuleClassifier fuzzy = new FuzzyRuleClassifier(samples);
        fuzzy.setOptions(getOptions());
        fuzzy.setGenoms(getGenomSet());
        fuzzy.bestResult = new Genom(this.bestResult);
        return fuzzy;
    }

    public Genom[] getGenomSet() {
        Genom[] ind = new Genom[this.genoms.length];
        for (int i = 0; i < ind.length; i++) {
            ind[i] = new Genom(this.genoms[i]);
        }
        return ind;
    }

    public void setGenoms(Genom[] _genoms) {
        System.arraycopy(_genoms, 0, genoms, 0, _genoms.length);
    }

    public void setOptions(String[] ops) {
        throw new UnsupportedOperationException("Not yet Implemented");
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

    public void setNumberOfGenoms(int number) {
        genoms = new Genom[number];
    }

    public int getNumberOfGenoms() {
        return (genoms != null) ? genoms.length : 0;
    }

    private ArrayList<Genom> getNewPapulation(ArrayList<Genom> x, final int howMany) {
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
    protected final int defaultNumber = 10;

    private ArrayList<Genom> getNewPapulation2(ArrayList<Genom> x, final int howMany) {
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

        for (int i = 0; i < defaultNumber + 10; i++) {
            gens.add(new Genom(5, howManyInputs(), howManyClasses()));
        }

        int generation = 0;
        final int maxEpos = 10000;
        boolean theBestFound = false;
        double mutationProb = 0.1;
        double crossProb = 0.02;

        ArrayList<Genom> parants;

        this.gradeFunction(gens);

        Collections.sort(gens);
        
        do {

            int size = gens.size() / 2;
            parants = getNewPapulation(gens, size);

            for (int i = 0; i < size / 2; i++) {
                if (Math.random() <= crossProb) {
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
                    //gens.set(i, gens.get(i).mutate());
                }
            }

            if (!gradeFunction(gens)) {
                Collections.sort(gens);
                gens = getNewPapulation2(gens, defaultNumber);
            }



            System.out.println("Pokolenie: " + generation +
                    " Gen: " + 0 +
                    " Corr: " + gens.get(0).getCorr() +
                    " Incorr: " + gens.get(0).getIncorr() +
                    " unClass: " + gens.get(0).getUnClass() +
                    " Rules: " + (int) gens.get(0).getPrem() +
                    " Fsets: " + (int) gens.get(0).getFsets() +
                    " Fitness : " + gens.get(0).fitness());


        } while ((++generation < maxEpos) && !theBestFound);

        Collections.sort(gens);
        bestResult = gens.get(0);

    //throw new UnsupportedOperationException("Not yet implemented");
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
            fuzzy.buildClassifier();



        } catch (IOException ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String[] getRules() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
