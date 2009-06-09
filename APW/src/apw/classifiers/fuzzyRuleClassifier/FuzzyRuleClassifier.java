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
    protected final int defaultNumber = 1000;
    protected ArrayList<FuzzyRule> resultRules = new ArrayList<FuzzyRule>();
    protected HashMap<String, ArrayList<Integer>> learningSet = null;
    protected HashMap<String, ArrayList<Integer>> testSet = null;

    //************************* constructors *********************************
    public FuzzyRuleClassifier(Samples S) {
        super(S);
        samples = this.normalize(S);
        genoms = new Genom[defaultNumber];
        options = new String[]{};
        HashMap<String, ArrayList<Integer>>[] tmp = this.partionData(0.2);
        learningSet = tmp[0];
        testSet = tmp[1];
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

    private int howManyX() {
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

        if((procent <= 0) && (procent >= 1.0)){
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
            data[1].put(s[i].toString(),new ArrayList<Integer>());
            int howMany = (int)(data[0].get(s[i].toString()).size() * procent);

            int classSize = data[0].get(s[i].toString()).size()-1;

            for(int j= classSize; j >= (classSize-howMany); j--){
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
        int index = 0;
        double max = Double.MIN_VALUE;

        for (int i = 0; i < resultRules.size(); i++) {
            double val = resultRules.get(i).classiify(s);
            if (max < val) {
                max = val;
                index = i;
            }
        }

        double[] data = null;
        
        if (max != Double.MIN_VALUE) {
            data = new double[resultRules.get(index).getConclusion().length()];
            for(int i=0;i <data.length; i++){
                data[i] = (double)(resultRules.get(index).getConclusion().charAt(i));
            }
        }

        return data;
    }

    public String interprate(double[] data) {
        String result = new String("");

        for(int i=0; i< data.length; i++){
            result += (char)(data[i]);
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
        fuzzy.setOptions(this.getOptions());
        fuzzy.setGenoms(this.getGenomSet());
        fuzzy.resultRules = this.getResultRules();
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

    public ArrayList<FuzzyRule> getResultRules() {
        ArrayList<FuzzyRule> result = new ArrayList<FuzzyRule>();
        for (FuzzyRule _rule : this.resultRules) {
            result.add(new FuzzyRule(_rule));
        }
        return result;
    }

    @Override
    public String[] getRules() {
        String[] _rules = new String[resultRules.size()];

        for (int i = 0; i < resultRules.size(); i++) {
            _rules[i] = resultRules.get(i).toString();
        }

        return _rules;
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

    public Samples normalize(Samples nSamples) {
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

    public void buildClassifier() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public Genom[] makePapulation() {
        Genom[] gen = new Genom[this.defaultNumber];

        for (int i = 0; i < gen.length; i++) {
            gen[i] = new Genom(5, this.howManyX(), this.howManyClasses());
        }

        return gen;
    }

    public static void main(String[] arg) {

        try {
            FuzzyRuleClassifier fuzzy = new FuzzyRuleClassifier("d:/svm/data/iris.arff");

            //System.out.println(fuzzy.partionData(0.2)[0]);
            //System.out.println(fuzzy.partionData(0.2)[1]);




        } catch (IOException ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
