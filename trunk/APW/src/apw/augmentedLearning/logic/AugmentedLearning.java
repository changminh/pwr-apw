/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apw.augmentedLearning.logic;

import alice.tuprolog.Term;
import apw.classifiers.Classifier;
import apw.core.Sample;
import apw.core.Samples;
import apw.augmentedLearning.gui.LoadingSamples_Step1;
import apw.augmentedLearning.gui.LoadingSamples_Step3;
import apw.augmentedLearning.gui.LoadingSamples_Step2;
import apw.classifiers.RuleClassifier;
import apw.core.Attribute;
import apw.core.Nominal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 *
 * @author nitric
 */
public class AugmentedLearning extends RuleClassifier {

    private static AugmentedLearning inst;
    private LoadingSamples_Step1 step1;
    private LoadingSamples_Step2 step2;
    private LoadingSamples_Step3 step3;
    private ArrayList<Rule> rules = new ArrayList<Rule>();
    private ArrayList<Term[]> terms = new ArrayList<Term[]>();
    private HashSet<Integer> termsAccessors = new HashSet<Integer>();
    private HashSet<Integer> bannedSamples = new HashSet<Integer>();
    private Samples samples;
    private DataFile dataFile;
    private int[] rulesCounter = new int[3];        // rules: main, acquired, additional
    private int mode = 0;                           // indicates which type of rules are currently added
    private boolean autonomic = true;
    private int classAttIndex;
    private int attAmount;
    private boolean hasToWait = true;               // When reset to false, it means that object is correctly prepared

    public AugmentedLearning(Samples samples) {
        super(null);
        if (samples == null)
            return;
        inst = this;
        autonomic = false;
        this.samples = samples;
        dataFile = new DataFile(samples);
        terms = dataFile.createTerms();
        step2(dataFile, false);
        try {
            waitForComplete();
        } catch (InterruptedException ex) {
            throw new ExceptionInInitializerError();
        }
    }

    public boolean autonomicMode() {
        return autonomic;
    }

    public HashSet<Integer> getBannedSamples() {
        return bannedSamples;
    }

    public void setBannedSamples(HashSet<Integer> bannedSamples) {
        this.bannedSamples = bannedSamples;
    }

    public void setDataFile(DataFile dataFile) {
        this.dataFile = dataFile;
    }

    public void setSamples(Samples samples) {
        this.samples = samples;
    }

    public Samples getSamples() {
        return samples;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
        rulesCounter[mode]++;
    }

    public ArrayList<Rule> internalRules() {
        return rules;
    }

    public void setRules(ArrayList<Rule> rules) {
        this.rules = rules;
    }

    public int[] getRulesCounter() {
        return rulesCounter;
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
        inst = new AugmentedLearning(null);
        inst.step1 = new LoadingSamples_Step1(inst);
        inst.step1.setVisible(true);
    }

    public void step2(DataFile dataFile, boolean autonomic) {
        this.dataFile = dataFile;
        if (autonomic) {
            step2 = new LoadingSamples_Step2(dataFile, inst);
            step2.setVisible(true);
        } else
            step2 = new LoadingSamples_Step2(this);
    }

    public void step3() {
        for (int i = 0; i < terms.size(); i++)
            termsAccessors.add(i);
        step3 = new LoadingSamples_Step3(dataFile, inst);
        attAmount = dataFile.getAttributesCount();
        classAttIndex = dataFile.getClassAttributeIndex();
        step3.setVisible(true);
    }

    public void step4() {
        step3.dispose();
        mode++;                                             // We're going now to acquire rules wiht AQ algorithm;
        RuleAcquisition aquisition = new RuleAcquisition(this);
        aquisition.doJob();
    }

    public void aqFinished() {
        mode++;
    }

    public String classify(Object[] sample) {
        String type = null;
        Rule winner = null;
        int support = 0;
        LinkedList<Rule> miss = new LinkedList<Rule>();
        for (Rule r : rules)
            if (r.covers(sample))
                if (type == null) {
                    winner = r;
                    type = ((SelectorForNominal) r.getThenClause().
                            get(0).
                            getSelector(dataFile.getClassAttributeIndex())).
                            getUniqueValue();
                } else
                    if (!(((SelectorForNominal) r.getThenClause().
                            get(0).
                            getSelector(dataFile.getClassAttributeIndex())).
                            getUniqueValue().
                            equals(type)))
                        miss.add(r);
                    else
                        support++;
        if (winner == null) {
            JOptionPane.showMessageDialog(null, "Brak reguł pokrywających przykład!");
            return null;
        }
        if (autonomic) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html><body>");
            sb.append("Przewidywany typ: <b>" + type + "</b><br>");
            sb.append("Zwycięska reguła: <b>" + winner.name + "</b><br>");
            if (support > 0)
                sb.append("Innych reguł pokrywających: <b>" + support + "</b><br>");
            if (miss.size() > 0) {
                sb.append("Reguły sprzeczne: <b>");
                for (int i = 0; i < miss.size() - 1; i++)
                    sb.append("" + miss.get(i).name + ", ");
                sb.append("" + miss.get(miss.size() - 1).name);
            }
            sb.append("</b></body></html>");
            JOptionPane.showMessageDialog(null, sb.toString());
        }
        return type;
    }

    @Override
    public String[] getRules() {
        String[] result = new String[rules.size()];
        for (int i = 0; i < rules.size(); i++)
            result[i] = rules.get(i).
                    translateToText();
        return result;
    }

    private Object[] sampleToArray(Sample s) {
        Object[] result = new Object[attAmount];
        for (int i = 0; i < attAmount; i++)
            if (i != classAttIndex)
                result[i] = s.get(i);
            else
                result[i] = null;
        return result;
    }

    private synchronized void waitForComplete() throws InterruptedException {
        while (hasToWait == true) {
            wait();
        }
    }

    public synchronized void classifierIsReady() {
        hasToWait = false;
        notifyAll();
    }

    @Override
    public double[] classifySample(Sample s) {
        Object sampleClass = classify(sampleToArray(s));
        Attribute att = s.getSamples().
                getClassAttribute();
        if (att instanceof Nominal) {
            Nominal nominal = (Nominal) att;
            double[] result = new double[nominal.getSortedIKeys().length];
            result[Arrays.binarySearch(nominal.getSortedIKeys(), sampleClass)] = 1.0;
            return result;
        }
        throw new IllegalArgumentException("Sample class must be Nominal");
    }

    @Override
    public void addSamples(Samples s) throws UnsupportedOperationException {

    }

    @Override
    public void addSample(Sample s) throws UnsupportedOperationException {
    }

    @Override
    public void rebuild() {
    }

    @Override
    public Classifier copy() {
        throw new UnsupportedOperationException();
    }
}
