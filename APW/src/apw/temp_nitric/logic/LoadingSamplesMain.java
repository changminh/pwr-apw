/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package apw.temp_nitric.logic;

import apw.core.Samples;
import apw.temp_nitric.gui.LoadingSamples_Step1;
import apw.temp_nitric.gui.LoadingSamples_Step3;
import apw.temp_nitric.gui.LoadingSamples_Step2;
import java.util.ArrayList;
import jpl.Term;

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
    private Samples samples;
    private ArrayList<Term[]> terms = new ArrayList<Term[]>();

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

    public static void main(String[] args) {
        inst = new LoadingSamplesMain();
        inst.step1 = new LoadingSamples_Step1(inst);
        inst.step1.setVisible(true);
    }

    public void step2(DataFile dataFile) {
        step2 = new LoadingSamples_Step2(dataFile, inst);
        step2.setVisible(true);
    }

    public void step3(DataFile dataFile) {
        step3 = new LoadingSamples_Step3(dataFile, inst);
        step3.setVisible(true);
    }
}
