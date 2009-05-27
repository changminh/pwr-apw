/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.augmentedLearning.logic;

import alice.tuprolog.Term;
import apw.core.Samples;
import apw.augmentedLearning.gui.LoadingSamples_Step1;
import apw.augmentedLearning.gui.LoadingSamples_Step3;
import apw.augmentedLearning.gui.LoadingSamples_Step2;
import apw.core.Sample;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author nitric
 */
public class LoadingSamplesMain {

    private static LoadingSamplesMain inst;
    private LoadingSamples_Step1 step1;
    private LoadingSamples_Step2 step2;
    private LoadingSamples_Step3 step3;
    private ArrayList<Rule> rules = new ArrayList<Rule>();
    private ArrayList<Term[]> terms = new ArrayList<Term[]>();
    private HashSet<Integer> termsAccessors = new HashSet<Integer>();
    private HashMap<Integer, Double> minValues = new HashMap<Integer, Double>();
    private HashMap<Integer, Double> maxValues = new HashMap<Integer, Double>();
    private Samples samples;
    private DataFile dataFile;

    public void setSamples(Samples samples) {
        this.samples = samples;
    }

    public Samples getSamples() {
        return samples;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public ArrayList<Rule> getRules() {
        return rules;
    }

    public LoadingSamples_Step1 getStep1() {
        return step1;
    }

    public LoadingSamples_Step2 getStep2() {
        return step2;
    }

    public void addTerms(Term[] termTable) {
        terms.add(termTable);
    }

    public ArrayList<Term[]> getConvertedTerms() {
        return terms;
    }

    public HashSet<Integer> getTermsAccessors() {
        return termsAccessors;
    }

    public DataFile getDataFile() {
        return dataFile;
    }

    public static void main(String[] args) {
        inst = new LoadingSamplesMain();
        inst.step1 = new LoadingSamples_Step1(inst);
        inst.step1.setVisible(true);
    }

    public void step2(DataFile dataFile) {
        this.dataFile = dataFile;
        step2 = new LoadingSamples_Step2(dataFile, inst);
        step2.setVisible(true);
    }

    public void step3() {
        collectMinMaxValues();
        dataFile.setMinValues(minValues);
        dataFile.setMaxValues(maxValues);
        for (int i = 0; i < terms.size(); i++)
            termsAccessors.add(i);
        step3 = new LoadingSamples_Step3(dataFile, inst);
        step3.setVisible(true);
    }

    public void step4() {
        step3.dispose();
        RuleAquisition aquisition = new RuleAquisition(this);
        aquisition.doJob();
    }

    private void collectMinMaxValues() {
        double min, max;
        Sample s;
        Double temp;
        for (int i = 0; i < dataFile.getAttributesCount(); i++) {
            if (dataFile.isNominalAttribute(i)) {
                continue;
            }
            else {
                s = samples.get(0);
                min = (Double)s.get(i);
                max = (Double)s.get(i);
                for (int j = 1; j < samples.size(); j++) {
                    s = samples.get(j);
                    if ((temp = (Double)s.get(i)) < min)
                        min = temp;
                    else if (temp > max)
                        max = temp;
                }
                minValues.put(i, min);
                maxValues.put(i, max);
            }
        }
    }
}
