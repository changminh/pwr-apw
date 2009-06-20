package apw.augmentedLearning.logic;

import apw.augmentedLearning.gui.ComplexCreatorFrame;
import apw.augmentedLearning.gui.PreviewOfSample;
import apw.augmentedLearning.gui.ProgressIndicator;
import apw.augmentedLearning.gui.WrongRuleWarning;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Nitric
 */
public class RuleAcquisition {
    private LoadingSamplesMain advisor;
    private HashSet<Integer> accessorsToBeRemoved = new HashSet<Integer>();
    private HashSet<Integer> accessors;
    private ArrayList<Complex> star = new ArrayList<Complex>();
    private String currentPositiveSeedCategory;
    private DataFile dataFile;
    private int attributesCount;
    private int classAttrId;
    private Random r = new Random();
    private HashSet<Integer> negativeSeeds = new HashSet<Integer>();
    private static final int maxComplexes = 4;
    private Object[][] rawData;
    private static final String rulePrefix = "rule_";
    private int found = 0;
    private ProgressIndicator progress;
    private ArrayList<Integer> validSamples = new ArrayList<Integer>();
    private HashSet<Integer> bannedSamples;
    private HashSet<Integer> samplesWithNull;
    private HashSet<Integer> samplesWithNullCoveredByAdditionalRules;           // Variable name FAIL
    private boolean hasToWait = false;
    private boolean switchToNext = false;           // Will be used when user will input additional rules
    private String tempRuleName;
    private int userRulesCount = 0;
    private String htmlFileName = "wyniki.html";
    private Iterator<Integer> leftSamplesWithNulls;
    private int currentSampleWithNulls;
    private TreeMap<Integer, HashSet<Integer>> rulesCoveringStats = new TreeMap<Integer, HashSet<Integer>>();

    public RuleAcquisition(LoadingSamplesMain advisor) {
        this.advisor = advisor;
        accessors = advisor.getTermsAccessors();
        dataFile = advisor.getDataFile();
        attributesCount = dataFile.getAttributesCount();
        classAttrId = dataFile.getClassAttributeIndex();
        rawData = dataFile.getRawObjects();
        bannedSamples = advisor.getBannedSamples();
        samplesWithNull = dataFile.getSamplesWithNull();
    }

    public void doJob() {
        new Thread(new Runnable() {
            public void run() {
                progress = new ProgressIndicator(accessors.size(), null);
                progress.setVisible(true);
                kaaqa();
            }
        }).start();
    }

    /**
     * Knowledge Augmented AQ Algorithm
     */
    public void kaaqa() {
        // Remove samples, which are covered by the rules given by the expert:
        Rule tempRule;
        int count = 0;
        for (int i = 0; i < rawData.length; i++)
            if (!bannedSamples.contains(i))
                validSamples.add(i);
        System.out.println("Przed usuwaniem przykładów pokrytych przez reguły eksperta.");
        System.out.println("Przykładów = " + accessors.size());
        for (Rule rule : advisor.getRules()) {
            removeCoveredSamples(rule);
        }
        while (accessors.size() > 0) {
            progress.setProgress(accessors.size());
            System.out.println("Przebieg #" + ++count + ", pozostało przykładów: " + accessors.size());
            tempRule = findRule_AQ();
            if (tempRule != null) {
                removeCoveredSamples(tempRule);
                advisor.addRule(tempRule);
            }
            else if (accessors.size() <= dataFile.getSamplesWithNull().size()) {
                System.out.println("Pozostało " + accessors.size() + " niepokrytych krotek.");
                break;
            }
            else {
                System.out.println("Wygenerowano pusty kompleks...");
            }
        }
        progress.dispose();
        // Show the rules covering:
        /* *
        outer:
        for (int i : validSamples) {
            for (Object o : rawData[i])
                System.out.print(o + " ");
            System.out.print(" --> \n");
            for (int j = 0; j < advisor.getRules().size(); j++) {
                if (advisor.getRules().get(j).covers(rawData[i])) {
                    System.out.print(advisor.getRules().get(j).getName() + ", ");
                    System.out.println(advisor.getRules().get(j).getThenClause().get(0)
                            .getSelector(classAttrId));
                }
            }
            System.out.println("");
        } /* */
        advisor.aqFinished();
        finalRulesCheck();
        acquireRulesForRestSamples();
        savePrologRepresentationToFile();
        showRulesInHtmlDocument();
        System.out.println("Dziękujemy, zapraszamy ponownie.");
    }
    
    private void removeCoveredSamples(Rule rule) {
        // System.out.println("Kompleksy reguły: ");
        // for (Complex c : rule.getIfClause())
        //    System.out.println(c);
        int count = 0;
        for (Integer i : accessors) {
            if (rule.covers(rawData[i]))
                accessorsToBeRemoved.add(i);
        }
        for (Integer j : accessorsToBeRemoved) {
            accessors.remove(j);
            count++;
        }
        accessorsToBeRemoved.clear();
        System.out.println("Usuwanie przykładów pokrytych przez regułę w liczbie: " + count);
    }

    private Rule findRule_AQ() {
        star.add(getUniversalComplex());
        ArrayList<Complex> partialStar;
        Integer[] tempAccessors = accessors.toArray(new Integer[] { });
        int positiveSeed = -1;
        // Check whether there are still samples that doesn't contain null:
        boolean thereAre = false;
        for (int i : tempAccessors)
            if (!samplesWithNull.contains(i)) {
                thereAre = true;
                break;
            }
        if (!thereAre)
            return null;
        outer:
        while (positiveSeed == -1) {
            positiveSeed = tempAccessors[r.nextInt(tempAccessors.length)];
            // Check if positiveSeed doesn't contain any 'null' value:
            if (samplesWithNull.contains(positiveSeed)) {
                positiveSeed = -1;
                continue outer;
            }
        }
        int negativeSeed;
        currentPositiveSeedCategory = (String) rawData[positiveSeed][classAttrId];

        System.out.println("positiveSeed = " + positiveSeed);
        System.out.println("Wylosowane ziarno pozytywne: " + currentPositiveSeedCategory);
        /* Since current $star is universal complex, we need to determine which samples belongs
         * to class other than $positiveSeed. */
        for (Integer i : validSamples) {
            if (!((String) rawData[i][classAttrId]).equals(currentPositiveSeedCategory))
                negativeSeeds.add(i);
        }
        while (negativeSeeds.size() > 0) {
            System.out.println("Wyszukiwanie kompleksów.");
            System.out.println("negativeSeeds.size() = " + negativeSeeds.size());
            
            // Choose the negativeSeed
            tempAccessors = negativeSeeds.toArray(new Integer[] {});
            negativeSeed = tempAccessors[r.nextInt(tempAccessors.length)];
            // Create partial star
            partialStar = partialStar(positiveSeed, negativeSeed);
            // Intersect stars
            star = intersectStars(star, partialStar);
            checkComplexes(star, negativeSeed);
            // Leave only the best complexes
            removeLessGeneralComplexes();
            removeWeakComplexes();
            removeUncoveredNegativeSeeds();
        }

        if (!finalComplexCheck(star.get(0), positiveSeed))                      // Complex doesn't cover positive seed!
            return null;
        Rule rule = new Rule(rulePrefix + ++found, advisor.getSamples());
        rule.addIfComplex(star.get(0));
        /**
        System.out.println("==========================================================");
        System.out.println("Wybrany kompleks:");
        System.out.println(star.get(0));
        System.out.println("=========================================================="); /* */
        Complex then = getUniversalComplex();
        HashSet<String> pseudoSet = new HashSet<String>();
        pseudoSet.add(currentPositiveSeedCategory);
        then.alterSelector(SelectorForNominal.getSelSet(classAttrId, false, pseudoSet));
        rule.addThenComplex(then);
        removeCoveredSamples(rule);
        return rule;
    }

    private Complex getUniversalComplex() {
        Complex result = new Complex();
        for (int i = 0; i < attributesCount; i++)
            if (dataFile.isNominalAttribute(i))
                result.addSelector(SelectorForNominal.getSelUniversal(i, dataFile.getNominalValuesOfAttribute(i)));
            else
                result.addSelector(SelectorForNumber.getUniversalSelector(i));
        return result;
    }

    private ArrayList<Complex> partialStar(Integer positiveSeed, Integer negativeSeed) {
        System.out.println("Pozytywne ziarno: ");
        for (Object t : rawData[positiveSeed])
            System.out.print("" + t + "  ");
        System.out.println("");
        System.out.println("Negatywne ziarno: ");
        for (Object t : rawData[negativeSeed])
            System.out.print("" + t + "  ");
        System.out.println("");
        ArrayList<Complex> result = new ArrayList<Complex>();
        SelectorForNominal sfnom;
        SelectorForNumber[] sfnum;
        Complex temp;
        String positiveS, negativeS;                  // For values of particular nominal attributes
        Double positiveD, negativeD;                  // For values of particular numeric attributes
        for (int i = 0; i < attributesCount; i++) {
            if (i == classAttrId)
                continue;
            temp = getUniversalComplex();
            if (temp.getSelector(i).isForNominalAttribute()) {
                /* Selector for nominal: we remove the value of the $negativeSeed from the selectors
                 * allowed values. If the values of attribute both for the positive and negative seeds
                 * are equal, we don't add any complex to the partial star. */
                sfnom = (SelectorForNominal) temp.getSelector(i);
                positiveS = (String) rawData[positiveSeed][i];
                negativeS = (String) rawData[negativeSeed][i];
                if (!(negativeS == null || positiveS.equals(negativeS))) {
                    /* Important: we assume, that $sfnom contains all the possible values of attribute!
                     * Otherwise, the selector wouldn't be as generall as possible as not all permitted
                     * values would be contained by it! */
                    sfnom.removeValue(negativeS);
                    temp.alterSelector(sfnom);
                    result.add(temp);
                }
            }
            else {
                /* Selector for number: we can have 0-2 selectors derived from original selector
                 divided by the forbidden value of the negativeSeed's attribute's value. */
                positiveD = (Double)rawData[positiveSeed][i];
                negativeD = (Double)rawData[negativeSeed][i];
                if (!(negativeD == null || negativeD.equals(positiveD))) {
                    sfnum = ((SelectorForNumber) temp.getSelector(i)).divide(negativeD);
                    for (SelectorForNumber sel : sfnum) {
                        if (sel.covers(positiveD)) {
                            temp.alterSelector(sel);
                            result.add(temp);
                            temp = getUniversalComplex();
                        }
                    }
                }
            }
        }
        System.out.println("Znalezionych kompleksów: " + result.size());
        if (result.isEmpty())
            System.out.println("O w mordę jeża, mamy problem z danymi... :/");
        return result;
    }

    private ArrayList<Complex> intersectStars(ArrayList<Complex> star, ArrayList<Complex> partialStar ) {
        ArrayList<Complex> result = new ArrayList<Complex>();
        Complex temp;
        boolean flag;
        for (Complex cs : star) {
            flag = true;
            for (Complex cp : partialStar) {
                temp = cs.intersection(cp);
                for (int i = 0; i < attributesCount; i++) {
                    if (cp.getSelector(i).isEmpty())
                        flag = false;
                }
                if (flag)
                    result.add(temp);
            }
        }
        return result;
    }

    private void removeLessGeneralComplexes () {
        // System.out.println("Przed odchudzaniem kompleksów = " + star.size());
        boolean[] skip = new boolean[star.size()];
        boolean interrupt = false;
        for (int i = 0; i < star.size() - 1; i++) {
            if (skip[i])
                continue;
            interrupt = false;
            for (int j = i + 1; j < star.size() && !interrupt; j++)
                switch (star.get(i).isMoreGeneralThan(star.get(j))) {
                    case EQUAL: skip[j] = true; break;
                    case LESS_GENERAL: skip[i] = true; interrupt = true; break;
                    case MORE_GENERAL: skip[j] = true; break;
                    case NOT_COMPARABLE: break;
                }
        }
        for (int i = skip.length - 1; i >= 0; i--) {
            if (skip[i])
                star.remove(i);
        }
        // System.out.println("Po odchudzaniu kompleksów = " + star.size());
    }

    private void removeUncoveredNegativeSeeds() {
        outer:
        for (Integer i : negativeSeeds) {
            for (Complex c : star) {
                if (c.covers(dataFile.getRawObjects()[i])) {
                    continue outer;
                }
            }
            // If reached here, it means that none complex covers the negative
            // sample, so it can be removed
            accessorsToBeRemoved.add(i);
        }
        for (Integer i : accessorsToBeRemoved)
            negativeSeeds.remove(i);
        accessorsToBeRemoved.clear();
        System.out.println("Po usunięciu niepokrytych: negativeSeeds.size() = " + negativeSeeds.size());
    }

    private void removeWeakComplexes() {
        double[] notes = new double[star.size()];
        int[] positiveSamplesCovered = new int[star.size()];
        int[] negativeSamplesNotCovered = new int[star.size()];
        int[] bonus = new int[star.size()];
        int[] fail = new int[star.size()];
        // int positiveSamplesCovered;
        // int negativeSamplesNotCovered;
        double weightP = 1.d, weightN = 1.d, weightB = 10.d;
        /* Below: count of samples, that so far are not covered by any rule and are correctly classified
         by the complex. */
        // int bonus;
        Object[] currentSample;
        boolean theSameClass;
        for (int sampleID : accessors) {
            currentSample = dataFile.getRawObjects()[sampleID];
            theSameClass = ((String)currentSample[classAttrId]).equals(currentPositiveSeedCategory);
            for (int complexId = 0; complexId < star.size(); complexId++) {
                if (star.get(complexId).covers(currentSample)) {
                    if (theSameClass) {
                        positiveSamplesCovered[complexId]++;
                        if (accessors.contains(sampleID))
                            bonus[complexId]++;
                    }
                    else {
                        fail[complexId]++;
                    }
                }
                else if (!theSameClass) {
                    negativeSamplesNotCovered[complexId]++;
                    if (accessors.contains(sampleID))
                        bonus[complexId]++;
                }
            }
        }
        // System.out.println("Oceny:");
        for (int i = 0; i < star.size(); i++) {
            notes[i] = weightB * bonus[i]
                    + weightN * negativeSamplesNotCovered[i]
                    + weightP * positiveSamplesCovered[i];
            // System.out.print(" " + notes[i] + ", " + fail[i] + "\n");
        }

        // System.out.println("");
        // Choose the best complexes:
        Double max;
        int index = 0;                                                          
        ArrayList<Integer> best = new ArrayList<Integer>();
        for (int i = 0; i < maxComplexes; i++) {
            max = -1.d;
            for (int j = 0; j < star.size(); j++) {
                if (max < notes[j] && !best.contains(j)) {
                    index = j;
                    max = notes[j];
                }
            }
            best.add(index);
        }
        ArrayList<Complex> newStar = new ArrayList<Complex>();
        for (Integer i : best)
            newStar.add(star.get(i));
        star = newStar;
    }

    private void checkComplexes(ArrayList<Complex> complexes, Integer negativeSeed) {
        // TODO: Remove later
        // Assert, that all created complexes doesn't cover negative seed:
        Object[] negativeSample = rawData[negativeSeed];
        for (Complex c : complexes) {
            if (c.covers(negativeSample)) {
                System.err.println("Proszę tutaj spojrzeć!!!");
                System.exit(-1);
            }
        }
    }

    private boolean finalComplexCheck(Complex c, int positiveSeed) {
        if (!c.covers(rawData[positiveSeed]))                           // Rule doesn't cover positive seed?!
            return false;
        for (int i : validSamples) {
            // System.out.print(sample[classAttrId] + " =?= " + currentPositiveSeedCategory);
            if (c.covers(rawData[i])) {
                if (!rawData[i][classAttrId].equals(currentPositiveSeedCategory)) {
                    System.out.println("Ale gafa... :(");
                    return false;                                       // Rule covers at least one negative seed!
                }
            }
        }
        return true;
    }

    private void finalRulesCheck() {
        int errors = 0;
        int success;
        HashSet<Integer> temp;
        Rule check;
        for (int ruleIndex = 0; ruleIndex < advisor.getRules().size(); ruleIndex++) {
            check = advisor.getRules().get(ruleIndex);
            success = 0;
            String classOfRule = ((SelectorForNominal) check.thenClause.get(0).getSelector(classAttrId)).getUniqueValue();
            for (int i : validSamples) {
                if (check.covers(rawData[i])) {
                    if (rawData[i][classAttrId].equals(classOfRule))
                        success++;
                    else
                        errors++;
                }
            }
            // Save the amount of samples covered by rules
            if ((temp = rulesCoveringStats.get(success)) == null)
                temp = new HashSet<Integer>();
            temp.add(ruleIndex);
            rulesCoveringStats.put(success, temp);
        }
        Iterator<Integer> iter = rulesCoveringStats.descendingKeySet().iterator();
        Iterator<Integer> ruleIter;
        ArrayList<Rule> orderedRules = new ArrayList<Rule>();
        while (iter.hasNext()) {
            ruleIter = rulesCoveringStats.get(success = iter.next()).iterator();
            while (ruleIter.hasNext()) {
                check = advisor.getRules().get(ruleIter.next());
                System.out.println("Reguła " + check.getName() + " pokrywa " + success + " krotek");
                orderedRules.add(check);
            }
        }
        // Now we have ordered list of rules, so we can alter rules list in advisor's object:
        advisor.setRules(orderedRules);
    }

    public void acquireRulesForRestSamples() {
        if (accessors.size() < 1)
            return;
        int answer = JOptionPane.showConfirmDialog(
                null,
                "Pozostało " + accessors.size() + " niepokrytych przykładów. " + "Czy chcesz wprowadzić dla nich swoje reguły?",
                "Pozostały niepokryte przykłady",
                JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.NO_OPTION)
            return;

        leftSamplesWithNulls = accessors.iterator();
        currentSampleWithNulls = leftSamplesWithNulls.hasNext() ? leftSamplesWithNulls.next() : -1;
        while (currentSampleWithNulls != -1) {
            new RuleCreatorThread(dataFile, currentSampleWithNulls).start();
            try {
                waitForResponse();
            }
            catch (InterruptedException ex) {
                System.err.println("Oh, it's not polite to disturb!");
            }
        }
    }

    /**
     *
     * @param c Complex that the new user's rule consist of. If null, then user resigned to insert new rule so we can
     * skip the $currentSampleWithNulls. If not null, we must check whether the rule covers the sample. If it doesn't,
     * we don't switch to next sample
     * @param i Now I don't now what the parameter is for :D But I'm 99% sure that it's equal to $currentSampleWithNulls
     */
    public synchronized void addComplex(Complex c, int i) {
        switchToNext = true;
        HashSet<Integer> coveredPositive = new HashSet<Integer>();
        HashSet<Integer> coveredNegative = new HashSet<Integer>();
        Object classAttributeValue = rawData[i][classAttrId];
        if (c != null) {
            if (!c.covers(rawData[i])) {
                    JOptionPane.showMessageDialog(null, "Reguła nie pokrywa przykładu!");
                    switchToNext = false;
            }
            else {
                // We're checking whether the new rule will cover other positive samples; also when it covers wrong samples,
                // user will be shown these negative samples and the rule won't be added to the result set.
                for (Integer sample : samplesWithNull) {
                    if (c.covers(rawData[sample]))
                        if (rawData[sample][classAttrId].equals(classAttributeValue))
                            coveredPositive.add(sample);
                        else
                            coveredNegative.add(sample);
                }
                if (coveredNegative.size() > 0) {
                    java.awt.EventQueue.invokeLater(new WrongRuleWarningThread(this, coveredNegative));
                    try {
                        waitForResponse();
                    }
                    catch(InterruptedException ex) { }
                }
                else {
                    tempRuleName = JOptionPane.showInputDialog(
                                    null, "Podaj nazwę reguły (bez polskich liter, z małej litery)",
                                    "regula_uzupelniajaca_nr_" + (userRulesCount + 1)
                                   );
                    if (tempRuleName != null) {
                        userRulesCount++;
                        Rule temp = new Rule(tempRuleName, advisor.getSamples());
                        temp.addIfComplex(c);
                        Complex then = getUniversalComplex();
                        then.alterSelector(SelectorForNominal.getSelSet(
                                            classAttrId,
                                            (String)rawData[i][classAttrId])
                                            );
                        temp.addThenComplex(then);
                        advisor.addRule(temp);
                    }
                }
            }
        }
        // User resigned of inserting rule, so we can switch to next example if exists.
        if (switchToNext)
            currentSampleWithNulls = leftSamplesWithNulls.hasNext() ? leftSamplesWithNulls.next() : -1;
        hasToWait = false;
        notifyAll();
    }

    public synchronized void ruleCorrection (boolean b) throws InterruptedException {
        switchToNext = !b;
        hasToWait = false;
        notifyAll();
    }

    private synchronized void waitForResponse() throws InterruptedException {
        hasToWait = true;
        while (hasToWait)
            wait();
    }

    private void savePrologRepresentationToFile() {
        StringBuilder sb = new StringBuilder();
        for (Rule rule : advisor.getRules()) {
            rule.translate();
            sb.append(rule.translator.prologRepresentation() + "\n");
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("results.pl"));
            bw.write(sb.toString());
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(RuleAcquisition.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showRulesInHtmlDocument() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n");
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " +
                "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append(" <head>\n");
        sb.append(" <meta http-equiv=\"Content-Type\" content=\"text/html; charset=cp-1250\" />");
        sb.append("     <title>Wygenerowane reguły</title>\n");
        sb.append(" </head>\n");
        sb.append("");
        sb.append(" <body>\n");
        sb.append("     <center><h1>Otrzymane reguły klasyfikacji</h1></center>\n");
        int counter = 0;
        if (advisor.getRulesCounter()[0] > 0) {
            sb.append(" <h2>Pierwotne reguły użytkownika</h2>\n");
            for (int i = 0; i < advisor.getRulesCounter()[0]; i++) {
                sb.append(advisor.getRules().get(counter++).translateToHtml());
                sb.append("\n");
            }
        }
        sb.append(" <h2>Reguły wygenerowane przez algorytm AQ</h2>\n");
        for (int i = 0; i < advisor.getRulesCounter()[1]; i++) {
            sb.append(advisor.getRules().get(counter++).translateToHtml());
            sb.append("\n");
        }
        if (advisor.getRulesCounter()[2] > 0) {
            sb.append(" <h2>Uzupełniające reguły użytkownika</h2>\n");
            for (int i = 0; i < advisor.getRulesCounter()[2]; i++) {
                sb.append(advisor.getRules().get(counter++).translateToHtml());
                sb.append("\n");
            }
        }
        sb.append(" </body>\n");
        sb.append("</html>\n");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFileName));
            bw.write(sb.toString());
            bw.close();
            String path = new File(".").getAbsolutePath();
            path = path.substring(0, path.length() - 1);
            BrowserLaunch.openURL("file://localhost/" + path + htmlFileName);
        } catch (IOException ex) {
            Logger.getLogger(RuleAcquisition.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    class RuleCreatorThread extends Thread {
        DataFile dataFile;
        int currentSample;

        public RuleCreatorThread(DataFile dataFile, int currentSample) {
            this.dataFile = dataFile;
            this.currentSample = currentSample;
        }

        @Override
        public void run() {
            new ComplexCreatorFrame(dataFile, currentSample, RuleAcquisition.this).setVisible(true);
        }
    }

    class WrongRuleWarningThread implements Runnable {
        private RuleAcquisition parent;
        private PreviewOfSample preview;
        private HashSet<Integer> coveredNegative;

        public WrongRuleWarningThread(RuleAcquisition parent, HashSet<Integer> coveredNegative) {
            this.parent = parent;
            this.coveredNegative = coveredNegative;
        }

        @Override
        public void run() {
            preview = new PreviewOfSample(dataFile, coveredNegative);
            new WrongRuleWarning(parent, preview);
        }
    }
}

