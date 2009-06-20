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

import apw.core.Sample;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 *
 * @author Przemek Woś
 */
class Genom implements Comparable<Genom> {

    private ArrayList<FuzzyRule> rules = new ArrayList<FuzzyRule>();
    private ArrayList<FuzzySet[]> sets = new ArrayList<FuzzySet[]>();

    private double corr = 0,
                   incorr = 0,
                   unclass = 0,
                   prem = 0,
                   fsets = 0;

    public static int numberOfSets = 6;

    public static double beta = 0.75,
                         delta = 0.1,
                         eps = 0.1,
                         dzeta = 0.8;

    public static int setType = 0;

    private void setValues(int factor, int _sets, Object[] classes) {

        //Object[] set = classes;

        for (int i = 0; i < classes.length; i++) {
            for (int j = 0; j < factor; j++) {
                Pair<Boolean, Integer>[] con = new Pair[_sets];

                for (int c = 0; c < con.length; c++) {
                    con[c] = new Pair<Boolean, Integer>();
                    con[c].setFirst(true);
                    con[c].setSecond(RandomClass.nextInt(0, numberOfSets));
                }

                FuzzyRule r = new FuzzyRule(con, classes[i].toString(), sets);
                rules.add(r);
            }
        }

        int nSet;

        //Random rand = new Random();

        for (int i = 0; i < _sets; i++) {

            if (setType > 2) {
                nSet = i % 3;
            } else {
                nSet = setType;
            }

            switch (nSet) {
                case 1:
                     {
                        TriangleSet[] _set = new TriangleSet[numberOfSets];

                        for (int ii = 0; ii < _set.length; ii++) {
                            _set[ii] = new TriangleSet(RandomClass.getMax() - RandomClass.getMin());
                        }

                        class Comp implements Comparator<TriangleSet> {

                            public int compare(TriangleSet o1, TriangleSet o2) {
                                double d1 = o1.getParams()[0],
                                       d2 = o2.getParams()[0];
                                return (d1 == d2) ? 0 : ((d1 > d2) ? 1 : -1);
                            }
                            
                        }

                        Arrays.sort(_set, new Comp());

                        _set[0].setRight(_set[1]);

                        for (int j = 1; j < _set.length - 1; j++) {
                            _set[j].setLeft (_set[j - 1]);
                            _set[j].setRight(_set[j + 1]);
                        }

                        _set[_set.length - 1].setLeft(_set[_set.length - 2]);

                        sets.add(_set);
                    }
                    break;
                case 2:
                     {
                        TrapeziumSet[] _set = new TrapeziumSet[numberOfSets];

                        ArrayList<Double> liczby = new ArrayList<Double>();

                        double range = RandomClass.getMax() - RandomClass.getMin();
                        for (int ii = 0; ii < _set.length; ii++) {
                            _set[ii] = new TrapeziumSet(range);
                            liczby.add(RandomClass.nextDouble() * range + RandomClass.getMin());
                            liczby.add(RandomClass.nextDouble() * range + RandomClass.getMin());
                        }

                        Collections.sort(liczby);

                        _set[0].setRight(_set[1]);
                        _set[0].setParam(liczby.get(0), liczby.get(1));

                        for (int j = 1; j < _set.length - 1; j++) {
                            _set[j].setLeft (_set[j - 1]);
                            _set[j].setRight(_set[j + 1]); // 2,3,4,5,6,7
                            _set[j].setParam(liczby.get(2 * j), liczby.get(2 * j + 1));
                        }

                        _set[_set.length - 1].setLeft(_set[_set.length - 2]);

                        _set[_set.length - 1].setParam(liczby.get(liczby.size() - 2),
                                                       liczby.get(liczby.size() - 1));

                        sets.add(_set);
                    }
                    break;
                case 0:
                     {
                        FuzzySet[] _set = new FuzzySet[numberOfSets];

                        for (int ii = 0; ii < _set.length; ii++) {
                            _set[ii] = new GaussFuzzySet(RandomClass.getMax() - RandomClass.getMin());
                        }

                        sets.add(_set);
                    }
                    break;

            }
        }

        this.repairGenom();
        this.setFsets();
        this.setPrem();
    }

    public Genom(int f, int _sets, Object[] classes) {
        this.setValues(f, _sets, classes);
    }

    public Genom(Genom _genom) {
        ArrayList<FuzzyRule> f = new ArrayList<FuzzyRule>();

        for (FuzzyRule rule : _genom.rules) {
            f.add(rule.clone());
        }

        ArrayList<FuzzySet[]> s = new ArrayList<FuzzySet[]>();

        for (FuzzySet[] ss : _genom.sets) {
            FuzzySet[] vec = new FuzzySet[ss.length];
            for (int i = 0; i < ss.length; i++) {
                vec[i] = ss[i].clone();
            }
            vec[0].setRight(vec[1]);
            
            for(int i = 1; i < vec.length - 1; i++){
                vec[i].setLeft(vec[i-1]);
                vec[i].setRight(vec[i+1]);
            }

            vec[vec.length-1].setLeft(vec[vec.length-2]);
            s.add(vec);
        }

        rules = f;
        sets = s;
    }

    private void repairGenom() {
        int count = 0;

        for (int i = 0; i < this.rules.size(); i++) {
            if (rules.get(i).isActive()) {
                count++;
                break;
            }
        }

        if (count == 0) {
            int index = RandomClass.nextInt(0, rules.size());
            rules.get(index).setActive(true);
        }

        for (int i = 0; i < this.sets.size(); i++) {
            count = 0;
            FuzzySet[] fs = this.sets.get(i);

            for (FuzzySet s : fs) {
                if (s.isActive()) {
                    count++;
                    break;
                }
            }

            if (count == 0) {
                int index = RandomClass.nextInt(0, fs.length);
                fs[index].setActive(true);
            }
        }
    }

    public double getInCorr() {
        return incorr;
    }

    public double getCorr() {
        return corr;
    }

    public double getUnClass() {
        return unclass;
    }

    public void setIncorr(double x) {
        incorr = x;
    }

    public void setCorr(double x) {
        corr = x;
    }

    public void setUnClass(double x) {
        unclass = x;
    }

    public String classifySample(Sample _sample) {
        double max = 0.0;
        int index = -1;

        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).isActive()) {
                double value = rules.get(i).classiify(_sample);

                if (max < value) {
                    max = value;
                    index = i;
                }
            }
        }
        return (max != 0.0) ? new String(rules.get(index).getConclusion()) : null;
    }

    private final double k(double x) {
        return (x != 0.0) ? (1.0 / x) : 2.0;
    }

    private void setFsets() {
        this.fsets = 0;

        for (int i = 0; i < sets.size(); i++) {
            for (int j = 0; j < sets.get(i).length; j++) {
                if (sets.get(i)[j].isActive()) {
                    this.fsets++;
                }
            }
        }
    }

    private void setPrem() {
        this.prem = 0;

        for (int i = 0; i < this.rules.size(); i++) {
            if (this.rules.get(i).isActive()) {
                Pair<Boolean, Integer>[] cond = this.rules.get(i).getConditions();

                for (int j = 0; j < cond.length; j++) {
                    if (cond[j].getFirst().booleanValue()) {
                        this.prem++;
                    }
                }
            }
        }
    }

    public double getPrem() {
        return this.prem;
    }

    public double getFsets() {
        return this.fsets;
    }

    public double fitness() {
        double result = corr * (beta * k(incorr) + delta * k(unclass)) + dzeta * k(prem) + eps * k(fsets);
        return result;
    }

    Genom mutate() {
        Genom _genom = new Genom(this);

        for (int i = 0; i < _genom.rules.size(); i++) {
            FuzzyRule rr = _genom.rules.get(i);

            if (RandomClass.nextBoolean()) {
                rr.setActive(!rr.isActive());
            }
            if (RandomClass.nextBoolean()) {
                _genom.rules.set(i, rr.mutate());
            }
        }

        for (int i = 0; i < _genom.sets.size(); i++) {
            FuzzySet[] _sets = _genom.sets.get(i);

            for (FuzzySet fuzzySet : _sets) {

                if (RandomClass.nextBoolean()) {
                    fuzzySet.setActive(!fuzzySet.isActive());
                }

                if (RandomClass.nextBoolean()) {
                    if (fuzzySet instanceof TriangleSet) {
                        double d1;
                        double dl, dr;

                        if (fuzzySet.getLeft() != null) {
                            dl = fuzzySet.getLeft().getParams()[0];
                        } else {
                            dl = RandomClass.getMin();
                        }

                        if (fuzzySet.getRight() != null) {
                            dr = fuzzySet.getRight().getParams()[0];
                        } else {
                            dr = RandomClass.getMax();
                        }

                        d1 = (dr - dl) * RandomClass.nextDouble() + dl;

                        fuzzySet.setParam(d1);
                        
                    } else {

                        if (fuzzySet instanceof TrapeziumSet) {
                            double d1 = fuzzySet.getParams()[0],
                                    d2 = fuzzySet.getParams()[1],
                                    dl,
                                    dr;

                            if (fuzzySet.getLeft() != null) {
                                dl = fuzzySet.getLeft().getParams()[1];
                            } else {
                                dl = RandomClass.getMin();
                            }

                            if (fuzzySet.getRight() != null) {
                                dr = fuzzySet.getRight().getParams()[0];
                            } else {
                                dr = RandomClass.getMax();
                            }

                            d1 = (dr - dl) * RandomClass.nextDouble() + dl;
                            d2 = (dr - dl) * RandomClass.nextDouble() + dl;

                            fuzzySet.setParam(d1, d2);
                            fuzzySet.correct();

                        } else {
                            if (fuzzySet instanceof GaussFuzzySet) {
                                double dVal = fuzzySet.getParams()[0],
                                        sigma = fuzzySet.getParams()[1];

                                dVal = dVal + RandomClass.rDouble();

                                sigma = sigma * (1 + 2 * (Math.random() - 0.5));

                                fuzzySet.setParam(dVal, sigma);
                            }//if 3...
                        }//else if 2
                    }//else if 1
                }
            }//foreach...





        }//for g.sets.size

        for (int ii = 0; ii < _genom.rules.size() / 2; ii++) {
            if (RandomClass.nextBoolean()) {
                int i = RandomClass.nextInt(0, _genom.rules.size());
                int j = RandomClass.nextInt(0, _genom.rules.size());

                if (i != j) {
                    FuzzyRule tmp = _genom.rules.get(i);
                    _genom.rules.set(i, _genom.rules.get(j));
                    _genom.rules.set(j, tmp);
                }
            }
        }

        _genom.repairGenom();
        _genom.setFsets();
        _genom.setPrem();

        return _genom;
    }

    ArrayList<FuzzySet[]> getSets() {
        return this.sets;
    }

    Genom[] crossWith(Genom _genom) {
        Genom newGen1 = new Genom(this);
        Genom newGen2 = new Genom(_genom);

        for (int i = 0; i < newGen1.rules.size(); i++) {
            if (RandomClass.nextBoolean()) {
                ArrayList<FuzzySet[]> s1 = newGen1.getSets();
                FuzzyRule r = newGen1.rules.get(i);
                newGen1.rules.set(i, newGen2.rules.get(i));
                newGen1.rules.get(i).setSets(newGen2.getSets());
                newGen2.rules.set(i, r);
                newGen2.rules.get(i).setSets(s1);
            }
        }

        for (int i = 0; i < newGen1.sets.size(); i++) {
            if (RandomClass.nextBoolean()) {
                FuzzySet[] s = newGen1.sets.get(i);
                newGen1.sets.set(i, newGen2.sets.get(i));
                newGen2.sets.set(i, s);
            }
        }

        newGen1.repairGenom();
        newGen1.setFsets();
        newGen1.setPrem();
        newGen2.repairGenom();
        newGen2.setFsets();
        newGen2.setPrem();


        return new Genom[]{newGen1, newGen2};
    }

    @Override
    public String toString() {
        String result = "";

        for (int i = 0; i < rules.size(); i++) {
            result += rules.toString() + "\n";
        }

        return result;
    }

    public String getRule(int i) {
        if ((i < rules.size()) && (i > -1)) {
            return rules.get(i).toString();
        } else {
            return null;
        }
    }

    public int getRuleNumber() {
        return rules.size();
    }

    public static void setNumOfSets(int num) {
        numberOfSets = num;
    }

    public int getNumOfSets() {
        return sets.size();
    }

    public int compareTo(Genom o) {
        double f1 = fitness(),
                f2 = o.fitness();

        return (f1 == f2) ? 0 : ((f1 > f2) ? -1 : 1);
    }

    public String[] getSets(int ii) {

        if ((ii < sets.size()) && (ii >= 0)) {
            String[] result = new String[sets.get(ii).length];

            for (int i = 0; i < result.length; i++) {
                result[i] = sets.get(ii)[i].toString();
            }

            return result;
        }

        return null;
    }
}
