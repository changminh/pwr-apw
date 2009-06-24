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
import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import apw.core.meta.ClassifierCapabilities;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
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
@ClassifierCapabilities(multiClass=true, handlesNominalAtts=true, handlesNumericAtts=true)
public class FuzzyRuleClassifier extends RuleClassifier {
    protected Samples samples = null;
    protected String[] options;
    protected Genom bestResult = null;
    protected int defaultNumber = 12;
    protected int rulesPerClass = 5;
    protected double mutationProb = 0.05;
    protected double crossProb = 0.2;
    protected double porcent = 95.0;
    protected int generetionWiat = 200;
    protected int maxEpos = 640;
    protected double rebuildProcent = 0;
    protected double rPro = 1;
    protected boolean normilize = false;
    private boolean _rebuild = false;

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

        data = Utils.getOption("rp", options);

        if (data.length() != 0) {
            this.porcent = Double.parseDouble(data);
            this.rebuildProcent = this.porcent;
        }

        data = Utils.getOption("gw", options);

        if (data.length() != 0) {
            this.generetionWiat = Integer.parseInt(data);
        //System.out.println(data);
        }

        data = Utils.getOption("mg", options);

        if (data.length() != 0) {
            this.maxEpos = Integer.parseInt(data);
        }

        data = Utils.getOption("bp", options);

        if (data.length() != 0) {
            rPro = Double.parseDouble(data);
        }

        data = Utils.getOption("n", options);

        if (data.length() != 0) {
            this.normilize = (Integer.parseInt(data) == 0) ? false : true;
        }

        this.options = options;
    }

    //************************* constructors *********************************
    public FuzzyRuleClassifier(Samples S) {
        super(S);
        samples = S;
        options = new String[]{};
        double[] m = findMinMax(samples);
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

    private Object[] classesInProblem() {
        HashMap<String, Integer> data = new HashMap<String, Integer>();

        for (int i = 0; i < this.samples.size(); i++) {
            int size = samples.get(i).size();
            String str = samples.get(i).get(size - 1).toString();

            if (!data.containsKey(str)) {
                data.put(str, 0);
            } else {
                data.put(str, data.get(str).intValue() + 1);
            }
        }

        return data.keySet().toArray();
    }

    @Override
    public void addSamples(Samples s) {
        this.samples.addAll(s);
        if (this.normilize == true) {
            this.samples = this.normalize(samples);
        }
    }

    @Override
    public void addSample(Sample s) {
        this.samples.add(s);
        if (this.normilize == true) {
            this.samples = this.normalize(samples);
        }
    }

    private boolean checkSampleIdentity(Sample _sample){
        Samples _ss = _sample.getSamples();

        if(samples.getAtts().size() < _ss.getAtts().size()){
            return false;
        }
        
        if(_ss.getAtts().size() < samples.getAtts().size() - 1){
            return false;
        }

        for(int i=0; i < _ss.getAtts().size(); i++){
            if(_ss.getAtts().get(i) instanceof Nominal){
                if(!(samples.getAtts().get(i) instanceof Nominal)){return false;}
            }else{
                if(_ss.getAtts().get(i) instanceof Numeric){
                    if(!(samples.getAtts().get(i) instanceof Numeric)){return false;}
                }
            }
        }

        return true;
    }


    @Override
    public double[] classifySample(Sample _sample) {

        Object[] _class = this.classesInProblem();
        double[] result = new double[_class.length];

        ArrayList<String> _classes = new ArrayList(_class.length);

        for (int i = 0; i < _class.length; i++) {
            _classes.add(_class[i].toString());
            result[i] = 0.0;
        }

        if(!checkSampleIdentity(_sample)){return result;}


        if (bestResult != null) {
            String clasyfication = bestResult.classifySample(this.samples,_sample);
            Collections.sort(_classes);

            if (clasyfication != null) {
                for (int i = 0; i < _classes.size(); i++) {
                    if (_classes.get(i).compareTo(clasyfication) == 0) {
                        result[i] = 1.0;
                    } else {
                        result[i] = 0.0;
                    }
                }
            }
        }

        return result;
    }

  
   

    @Override
    public void rebuild() {

        if (_rebuild == false) {
            this.buildClassifier();
            _rebuild = true;
        } else {

            if (bestResult == null) {
                return;
            }

            rebuildProcent += rPro;

            if (rebuildProcent > 100.0) {
                rebuildProcent = 100.0;
            }

            ArrayList<Genom> gens = new ArrayList<Genom>();
            gens.add(bestResult);

            for (int i = 1; i < defaultNumber; i++) {
                gens.add(bestResult.mutate());
            }

            int generation = 0;
            boolean best = false;

            ArrayList<Genom> parants;

            gradeFunction(gens, rebuildProcent);
            Collections.sort(gens);

            Pair<Genom, Integer> resultGen = new Pair<Genom, Integer>(gens.get(0), 0);

            do {
                int size = gens.size() / 2;
                parants = roulette(gens, size);

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
                    }
                }

                if (!gradeFunction(gens, rebuildProcent)) {
                    Collections.sort(gens);
                    gens = getNewPapulation(gens, defaultNumber);

                    if (resultGen.getFirst().compareTo(gens.get(0)) == 0) {
                        resultGen.setSecond(resultGen.getSecond().intValue() + 1);
                    } else {
                        resultGen.setFirst(gens.get(0));
                        resultGen.setSecond(0);
                    }

                } else {
                    generation = maxEpos;
                    best = true;
                }

            } while ((++generation < maxEpos) &&
                    resultGen.getSecond().intValue() < generetionWiat);


            if (!best) {
                this.rebuildProcent -= this.rPro;
            }

            Collections.sort(gens);
            bestResult = gens.get(0);
        }
    }

    @Override
    public Classifier copy() {
        try {
            FuzzyRuleClassifier fuzzy = new FuzzyRuleClassifier(samples);
            fuzzy.setOptions(getOptions().clone());
            fuzzy.bestResult = new Genom(bestResult);
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

    private double atof(String str) {
        return Double.valueOf(str).doubleValue();
    }

    private int atoi(String str) {
        return Double.valueOf(str).intValue();
    }

    public Samples normalize(Samples nSamples) {
        double length;
        Samples _samples = new Samples(nSamples.getAtts());

        for (int i = 0; i < nSamples.size(); i++) {
            length = 0.0;
            int size = nSamples.get(i).size() - 1;
            double data;

            for (int j = 0; j < size; j++) {
                String str = nSamples.getAtts().get(j).getRepresentation(nSamples.get(i).get(j)).toString();
                data = atof(str);
                length += data * data;
            }

            length = Math.sqrt(length);

            List<Object> list = new ArrayList<Object>();

            for (int j = 0; j < size; j++) {
                if(!nSamples.getAtts().get(j).isNominal()){
                    String str = nSamples.getAtts().get(j).getRepresentation(nSamples.get(i).get(j)).toString();
                    data = atof(str);
                    data /= length;
                    str = Double.toString(data);
                    Object obj = nSamples.getAtts().get(j).getRepresentation(str);
                    list.add(obj);
                }else{
                    list.add(nSamples.getAtts().get(j).getRepresentation(nSamples.get(i).get(j)));
                }
            }

            Object obj = nSamples.getAtts().get(size).
                    getRepresentation(nSamples.get(i).get(size).toString());


            list.add(obj);
            Sample newSample = new Sample(new Samples(nSamples.getAtts()), list.toArray());
            System.out.println(newSample.toString());
            _samples.add(newSample);


        }

        return _samples;
    }

    public String getInfo() {
        return "Fuzzy Rule Classifier for APW Project, made by Przemek Woś...";
    }

    private ArrayList<Genom> roulette(ArrayList<Genom> x, final int howMany) {
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
            Sample _ss = _s.get(i);
            for (int j = 0; j < _ss.size() - 1; j++) {
                double value = Double.valueOf(_s.getAtts().get(j).getRepresentation(_ss.get(j)).toString()).doubleValue();
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

    private boolean gradeFunction(ArrayList<Genom> gens, final double pr) {
        for (int i = 0; i < gens.size(); i++) {
            gens.get(i).setCorr(0);
            gens.get(i).setIncorr(0);
            gens.get(i).setUnClass(0);

            for (int j = 0; j < samples.size(); j++) {
                String result = gens.get(i).classifySample(this.samples,samples.get(j));

                if (result == null) {
                    gens.get(i).setUnClass(gens.get(i).getUnClass() + 1);
                } else {
                    int index = samples.get(j).size() - 1;

                    if (result.compareTo(samples.get(j).get(index).toString()) == 0) {
                        gens.get(i).setCorr(gens.get(i).getCorr() + 1);
                    } else {
                        gens.get(i).setIncorr(gens.get(i).getInCorr() + 1);
                    }
                }
            }

            if ((gens.get(i).getCorr() * 100.0) / samples.size() >= pr) {
                return true;
            }

        }
        return false;
    }

    public void buildClassifier() {

        if (this.normilize == true) {
            this.samples = this.normalize(samples);
        }

        ArrayList<Genom> gens = new ArrayList<Genom>();

        for (int i = 0; i < defaultNumber; i++) {
            gens.add(new Genom(rulesPerClass, howManyInputs(), classesInProblem()));
        }

        int generation = 0;

        ArrayList<Genom> parants;

        gradeFunction(gens, this.porcent);
        Collections.sort(gens);

        Pair<Genom, Integer> resultGen = new Pair<Genom, Integer>(gens.get(0), 0);

        do {
            int size = gens.size() / 2;
            parants = roulette(gens, size);

            for (int i = 0; i < size / 2; i++) {
                if (RandomClass.nextDouble() <= crossProb) {
                    Genom o1 = parants.get(2 * i);     //0,2,4
                    Genom o2 = parants.get(2 * i + 1); //1,3,5
                    Genom[] result = o1.crossWith(o2);
                    gens.add(result[0]);
                    gens.add(result[1]);
                }
            }

            for (int i = 0; i < gens.size(); i++) {
                if (RandomClass.nextDouble() <= mutationProb) {
                    gens.add(gens.get(i).mutate());
                }
            }

            if (!gradeFunction(gens, this.porcent)) {
                Collections.sort(gens);
                gens = getNewPapulation(gens, defaultNumber);

                if (comapre(resultGen.getFirst(), gens.get(0))) {
                    resultGen.setSecond(resultGen.getSecond().intValue() + 1);
                } else {
                    resultGen.setFirst(gens.get(0));
                    resultGen.setSecond(0);
                }

            } else {
                generation = maxEpos;
            }
            /*
            System.out.println("Pokolenie: " + generation +
            "; Gen: " + 0 +
            "; Corr: " + gens.get(0).getCorr() +
            "; Incorr: " + gens.get(0).getInCorr() +
            "; unClass: " + gens.get(0).getUnClass() +
            "; Rules: " + (int) gens.get(0).getPrem() +
            "; Fsets: " + (int) gens.get(0).getFsets() +
            "; Fitness : " + gens.get(0).fitness() +
            "; Statistic: " + (100.0 * gens.get(0).getCorr() / samples.size()));
             */
            System.err.print("*");
        } while ((++generation < maxEpos) &&
                resultGen.getSecond().intValue() < generetionWiat);

        Collections.sort(gens);
        bestResult = gens.get(0);
    /*
    System.out.println("Pokolenie: " + generation +
    "; Gen: " + 0 +
    "; Corr: " + gens.get(0).getCorr() +
    "; Incorr: " + gens.get(0).getInCorr() +
    "; unClass: " + gens.get(0).getUnClass() +
    "; Rules: " + (int) gens.get(0).getPrem() +
    "; Fsets: " + (int) gens.get(0).getFsets() +
    "; Fitness : " + gens.get(0).fitness() +
    "; Statistic: " + (100.0 * gens.get(0).getCorr() / samples.size()));
    */
    }

    private boolean comapre(Genom a, Genom b) {
        boolean class1 = a.getCorr() == b.getCorr();
        boolean class2 = a.getInCorr() == b.getInCorr();
        boolean class3 = a.getUnClass() == b.getUnClass();
        boolean class4 = a.getFsets() == b.getFsets();
        boolean class5 = a.getPrem() == b.getPrem();

        return class1 && class2 && class3 && class4 && class5;
    }

    public void printRules(PrintStream out) {

        out.println("Reguly: ");

        for (int i = 0; i < bestResult.getRuleNumber(); i++) {
            if (this.bestResult.getRule(i).contains("Aktywna: 1")) {
                out.println(bestResult.getRule(i).toString());
            }
        }

        out.println("\nZbiory: \n");

        for (int i = 0; i < bestResult.getNumOfSets(); i++) {
            String[] _sets = bestResult.getSets(i);

            if (_sets[0].contains("d:")) {
                out.println("(Typ grupy: 1;");
            } else {
                if (_sets[0].contains("dl:")) {
                    out.println("(Typ grupy: 2;");
                } else {
                    out.println("(Typ grupy: 0;");
                }
            }

            for (int j = 0; j < _sets.length; j++) {
                out.println("(" + _sets[j] + ")");
            }

            out.println(")\n");
        }

    }

    public static void main(String[] arg) {

        try {
            FuzzyRuleClassifier fuzzy = new FuzzyRuleClassifier("data/weather.nominal.arff");

            String[] data = new String[]{"-o", "10", //liczba osobnikow przypadajaca na populacje
                "-r", "5", //liczba regul przypadajaca na jedna klase
                "-f", "5", //liczba zbiorow rozmytych przypadajaca na jedna grupe
                "-t", "0", //typ zbiorów 0 - gauss, 1 - trojkatny, 2 - trapezowy 3 - mieszane
                "-m", "0.7", //prawdopodobienstwo mutacji
                "-c", "0.5", //prawdopodobienstwo krzyzowania
                "-bp", "50", //procent o jaki ma sie zwiekszyc poprawne klasyfikoanie zbioru uczacego
                //gdy wywolamy metode rebuid
                "-mg", "10000", //maksymalna liczba epok jaka trwa uczenie

                "-b", "0.5", //wspolczynnik beta w funckji przystosowania
                //tzn. jak bardzo bierzemy pod uwage zle zaklasyfikowania

                "-d", "0.4", //wspolczynnik delta w funckji przystosowania
                //tzn. jak bardzo bierzemy pod uwage brak zaklasyfikowania

                "-e", "0.4", //wspolczynnik epsilon w funckji przystosowania
                //tzn. jak bardzo bierzemy licybe aktzwnzch
                //pryeslanek we wszystkich regulach

                "-z", "0.3", //wspolczynnik dzeta w funckji przystosowania
                //tzn. jak bardzo bierzemy pod uwage brak zaklasyfikowania

                "-rp", "90.0", //procent poprawnej klasyfikacji po jakim
                //osobnik zostanie zakceptowany jako rozwiazanie

                "-gw", "500"};  //liczba epok po ktorej,
            ///jesli nic sie nie zmieni, uczenie zostanie przerwane


            fuzzy.setOptions(data);
            System.out.println("Zaczynamy uczenie( to moze chwile potrwac :) )");
            fuzzy.buildClassifier();
            //fuzzy.printRules(System.out);
            Sample s = new ARFFLoader(new File("data/iris.arff")).getSamples().get(0);
            
            fuzzy.classifySample(s);


            //fuzzy.bestResult.classifyWithProb(fuzzy.samples,s);

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
