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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Przemek Woś
 */
public class Genom implements Comparable<Genom> {

    private ArrayList<FuzzyRule> rules = new ArrayList<FuzzyRule>();
    private ArrayList<FuzzySet[]> sets = new ArrayList<FuzzySet[]>();
    private static int numberOfSets = 4;

    public void setValues(int factor, int _sets, Object[] classes) {

        Random random = new Random(System.currentTimeMillis());
        Random rand2 = new Random(System.currentTimeMillis() + System.nanoTime() + 100);

        Object[] set = classes;

        for (int i = 0; i < set.length; i++) {
            for (int j = 0; j < factor; j++) {
                Pair<Boolean, Integer>[] con = new Pair[_sets];

                for (int c = 0; c < _sets; c++) {
                    con[c].setFirst(random.nextBoolean());
                    con[c].setSecond(rand2.nextInt(_sets));
                }

                FuzzyRule r = new FuzzyRule(con, set[i].toString(), sets);
                //r.repairRule();
                rules.add(r);
            }
        }


        for (int i = 0; i < _sets; i++) {
            switch (random.nextInt(3)) {
                case 0:
                     {
                        TriangleSet[] _set = new TriangleSet[numberOfSets];

                        _set[0].setRight(_set[1]);

                        for (int j = 1; j < _set.length - 1; j++) {
                            _set[j].setLeft(_set[j - 1]);
                            _set[j].setRight(_set[j + 1]);
                        }

                        _set[_set.length - 1].setLeft(_set[_set.length - 2]);

                        sets.add(_set);
                    }
                    break;
                case 1:
                     {
                        TrapeziumSet[] _set = new TrapeziumSet[numberOfSets];

                        _set[0].setRight(_set[1]);

                        for (int j = 1; j < _set.length - 1; j++) {
                            _set[j].setLeft(_set[j - 1]);
                            _set[j].setRight(_set[j + 1]);
                        }

                        _set[_set.length - 1].setLeft(_set[_set.length - 2]);

                        sets.add(_set);
                    }
                    break;
                case 2:
                     {
                        sets.add(new GaussFuzzySet[numberOfSets]);
                    }
                    break;

            }
        }

        this.repairGenom();
    }

    public Genom(int f, int _sets, Object[] classes) {
        this.setValues(f, _sets, classes);
    }

    public Genom(Genom _genom) {
        ArrayList<FuzzyRule> f = new ArrayList<FuzzyRule>();

        for (FuzzyRule rule : this.rules) {
            f.add(rule.clone());
        }

        ArrayList<FuzzySet[]> s = new ArrayList<FuzzySet[]>();

        for (FuzzySet[] ss : this.sets) {
            FuzzySet[] vec = new FuzzySet[ss.length];
            for (int i = 0; i < ss.length; i++) {
                vec[i] = ss[i].clone();
            }
            s.add(vec);
        }

        rules = f;
        sets = s;
    }

    private void repairGenom() {

        int count = 0;

        for (int i = 0; i < this.rules.size(); i++) {
            if (rules.get(i).getActive()) {
                count++;
                break;
            }
        }

        if (count == 0) {
            int index = new Random(System.currentTimeMillis()).nextInt(rules.size());
            rules.get(index).setActive(true);
        }

        for (int i = 0; i < this.sets.size(); i++) {
            count = 0;
            FuzzySet[] fs = this.sets.get(i);

            for (FuzzySet s : fs) {
                if (s.getActive()) {
                    count++;
                    break;
                }
            }

            if (count == 0) {
                int index = new Random(System.currentTimeMillis()).nextInt(fs.length);
                fs[index].setActive(true);
            }
        }


    }

    public double fitness() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    Genom mutate() {
        Genom g = new Genom(this);
        Random rand = new Random(System.currentTimeMillis());
        double zakres = numberOfSets;

        for (int i = 0; i < g.rules.size(); i++) {
            FuzzyRule rr = g.rules.get(i);

            if (rand.nextBoolean()) {
                rr.setActive(!rr.getActive());
            }

            if (rand.nextBoolean()) {
                rr.mutate();
            }
        }

        zakres = 10; // tego nie jest pewnien... jaka wartosc tu wpisac...

        rand = new Random(System.currentTimeMillis() + System.nanoTime() + rand.nextInt(10000));

        for (int i = 0; i < g.sets.size(); i++) {
            FuzzySet[] s = g.sets.get(i);

            for (FuzzySet fuzzySet : s) {

                if (rand.nextBoolean()) {
                    fuzzySet.setActive(!fuzzySet.getActive());
                }

                if (fuzzySet instanceof TriangleSet) {
                    double d1 = fuzzySet.getParams()[0];
                    d1 = d1 +
                            ((rand.nextBoolean()) ? (Math.random() * zakres / 10.0) : -(Math.random() * zakres / 10.0));
                    fuzzySet.setParam(d1);
                } else {

                    if (fuzzySet instanceof TrapeziumSet) {
                        double d1 = fuzzySet.getParams()[0],
                                d2 = fuzzySet.getParams()[1];

                        d1 = d1 +
                                ((rand.nextBoolean()) ? (Math.random() * (zakres / 10.0)) : -(Math.random() * (zakres / 10.0)));

                        d2 = d2 +
                                ((rand.nextBoolean()) ? (Math.random() * (zakres / 10.0)) : -(Math.random() * (zakres / 10.0)));

                        fuzzySet.setParam(d1, d2);

                    } else {

                        if (fuzzySet instanceof GaussFuzzySet) {
                            double dVal = fuzzySet.getParams()[0],
                                    sigma = fuzzySet.getParams()[1];

                            dVal = dVal +
                                    ((rand.nextBoolean()) ? (Math.random() * zakres / 10.0) : -(Math.random() * zakres / 10.0));

                            sigma = sigma * (1 + 2 * (Math.random() - 0.5));

                            fuzzySet.setParam(dVal, sigma);
                        }//if 3...
                    }//else if 2
                }//else if 1
            }//foreach...
        }//for g.sets.size

        for(int ii=0; ii < 3; ii++){
            if(rand.nextBoolean()){
                int i = rand.nextInt(g.rules.size());
                int j = rand.nextInt(g.rules.size());

                if(i != j){
                    FuzzyRule tmp = g.rules.get(i);
                    g.rules.set(i, g.rules.get(j));
                    g.rules.set(j, tmp);
                }
            }
        }
        
        g.repairGenom();

        return g;
    }

    Genom[] crossWith(Genom _genom) {
        Genom newGen1 = new Genom(this);
        Genom newGen2 = new Genom(_genom);

        Random rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < newGen1.rules.size(); i++) {
            if (rand.nextBoolean()) {
                FuzzyRule r = newGen1.rules.get(i);
                newGen1.rules.set(i, newGen2.rules.get(i));
                newGen2.rules.set(i, r);
            }
        }

        rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < newGen1.sets.size(); i++) {
            if (rand.nextBoolean()) {
                FuzzySet[] s = newGen1.sets.get(i);
                newGen1.sets.set(i, newGen2.sets.get(i));
                newGen2.sets.set(i, s);
            }
        }

        return new Genom[]{newGen1, newGen2};
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void setNumOfSets(int num) {
        numberOfSets = num;
    }

    public static int getNumOfSets() {
        return numberOfSets;
    }

    public int compareTo(Genom o) {
        double f1 = fitness();
        double f2 = o.fitness();
        return (f1 == f2) ? 0 : ((f1 - f2 > 0) ? 1 : -1);
    }
}
