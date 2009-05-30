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
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 *
 * @author Przemek Woś
 */

public class FuzzyRuleClassifier extends apw.classifiers.RuleClassifier {

    protected Samples samples = null;
    protected Individual[] individual = null;
    protected String[] rules = null;
    protected String[] options;
    protected final int defaultNumber = 1000;

    public FuzzyRuleClassifier(Samples S) {
        super(S);
        samples = S;
        individual = new Individual[defaultNumber];
        options = new String[]{};
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

    @Override
    public double[] classifySample(Sample s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addSamples(Samples s) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addSample(Sample s) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rebuild() {
        buildClassifier();
    }

    @Override
    public Classifier copy() {
        FuzzyRuleClassifier fuzzy = new FuzzyRuleClassifier(samples);
        fuzzy.setOptions(this.getOptions());
        fuzzy.setIndividuals(this.getIndivdualSet());
        fuzzy.rules = this.getRules();
        return fuzzy;
    }

    public Individual[] getIndivdualSet() {
        Individual[] ind = new Individual[this.individual.length];
        for (int i = 0; i < ind.length; i++) {
            ind[i] = new Individual(this.individual[i]);
        }
        return ind;
    }

    public void setIndividuals(Individual[] i) {
        individual = i;
    }

    @Override
    public String[] getRules() {
        String[] _rules = new String[rules.length];

        for (int i = 0; i < _rules.length; i++) {
            _rules[i] = new String(rules[i]);
        }
        
        return _rules;
    }

    public void setOptions(String[] ops) {
    }

    public String[] getOptions() {
        return this.options.clone();
    }

    public Samples normalize(Samples nSamples) {
        double length;
        Samples _samples = new Samples(nSamples.getAtts());

        for (int i = 0; i < nSamples.size(); i++) {
            length = 0.0;
            int size = nSamples.get(i).size() - 1;

            for (int j = 0; j < size; j++) {
                double data = atof(nSamples.get(i).get(j).toString());
                length += data * data;
            }

            length = Math.sqrt(length);

            List<Object> list = new ArrayList<Object>();

            for (int j = 0; j < size; j++) {
                double data = atof(nSamples.get(i).get(j).toString());
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

    private double atof(String str) {
        return Double.valueOf(str).doubleValue();
    }
    /*
    private int atoi(String str) {
    return Double.valueOf(str).intValue();
    }
     */

    public String getInfo() {
        return "Fuzzy Rule Classifier for APW Project, made by Przemek Woś...";
    }

    public void setNumberOfIndividuals(int number) {
        individual = new Individual[number];
    }

    public int getNumberOfIndividuals() {
        return (individual != null) ? individual.length : 0;
    }

    public void buildClassifier() {
    }

    public static void main(String[] arg) {
        try {
            FuzzyRuleClassifier fuzzy = new FuzzyRuleClassifier("c:/svm/data/iris.arff");
        //fuzzy.setNumberOfIndividuals(-1);


        } catch (IOException ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(FuzzyRuleClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
