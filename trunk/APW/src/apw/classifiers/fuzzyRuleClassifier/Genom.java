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

/**
 *
 * @author Przemek Woś
 */
class Genom implements Comparable<Genom> {

    private ArrayList<FuzzyRule> rules = new ArrayList<FuzzyRule>();
    private ArrayList<FuzzySet[]> sets = new ArrayList<FuzzySet[]>();
    private double corr=0,incorr=0,unclass=0,prem=0,fsets=0;

    public static int numberOfSets = 6;
    public static double beta  = 0.75,
                         delta = 0.1,
                         eps = 0.1,
                         dzeta = 0.8;

    public static int setType = 0;
  
    

    private void setValues(int factor, int _sets, Object[] classes) {

        Object[] set = classes;

        for (int i = 0; i < set.length; i++) {
            for (int j = 0; j < factor; j++) {
                Pair<Boolean, Integer>[] con = new Pair[_sets];

                for (int c = 0; c < con.length; c++) {
                    con[c] = new Pair<Boolean, Integer>();
                    con[c].setFirst(true);
                    con[c].setSecond(RandomClass.nextInt(0,numberOfSets));
                }

                FuzzyRule r = new FuzzyRule(con, set[i].toString(), sets);
                rules.add(r);
            }
        }

        for (int i = 0; i < _sets; i++) {
            switch (setType) {
                case 1:{
                        TriangleSet[] _set = new TriangleSet[numberOfSets];

                        for(int ii = 0; ii < _set.length; ii++ ){
                            _set[ii] = new TriangleSet(RandomClass.getMax() - RandomClass.getMin());
                        }

                        _set[0].setRight(_set[1]);

                        for (int j = 1; j < _set.length - 1; j++) {
                            _set[j].setLeft (_set[j - 1]);
                            _set[j].setRight(_set[j + 1]);
                            _set[j].correct();
                        }

                        _set[_set.length - 1].setLeft(_set[_set.length - 2]);

                        sets.add(_set);
                    }
                    break;
                case 2:{
                        TrapeziumSet[] _set = new TrapeziumSet[numberOfSets];

                        for(int ii = 0; ii < _set.length; ii++ ){
                            _set[ii] = new TrapeziumSet(RandomClass.getMax() - RandomClass.getMin());
                        }

                        _set[0].setRight(_set[1]);

                        for (int j = 1; j < _set.length - 1; j++) {
                            _set[j].setLeft (_set[j - 1]);
                            _set[j].setRight(_set[j + 1]);
                            _set[j].correct();
                        }

                        _set[_set.length - 1].setLeft(_set[_set.length - 2]);

                        sets.add(_set);
                    }
                    break;
                case 0:{
                        GaussFuzzySet[] _set = new GaussFuzzySet[numberOfSets];

                        for(int ii = 0; ii < _set.length; ii++ ){
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
            s.add(vec);
        }

        rules = f;
        sets = s;
    }

    private void repairGenom() {
        int count = 0;
        //Random RandClass = new Random(System.currentTimeMillis() + System.nanoTime());

        for (int i = 0; i < this.rules.size(); i++) {
            if (rules.get(i).isActive()) {
                count++;
                break;
            }
        }

        if (count == 0) {
            int index = RandomClass.nextInt(0,rules.size());
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
                int index = RandomClass.nextInt(0,fs.length);
                fs[index].setActive(true);
            }
        }
    }


    public double getIncorr(){return incorr;}
    public double getCorr(){return corr;}
    public double getUnClass(){return unclass ;}

    public void setIncorr(double x){incorr = x;}
    public void setCorr(double x){corr = x;}
    public void setUnClass(double x){unclass = x;}
   
    public String classify(Sample s){
        double max = 0.0;
        int index = -1;
        
        for(int i=0; i < this.rules.size(); i++ ){
            if(rules.get(i).isActive()){
                double value = this.rules.get(i).classiify(s);
                
                if(max < value){
                    max = value;
                    index = i;
                }
            }
        }
        return (max != 0.0)?new String(rules.get(index).getConclusion()):null;
    }


    private final double k(double x){
        return (x != 0.0)?(1.0/x):2.0;
    }


    private void setFsets(){
        this.fsets = 0;
        
        for(int i=0; i < sets.size(); i++){
            for(int j=0; j < sets.get(i).length; j++){
                if(sets.get(i)[j].isActive()){
                    this.fsets++;
                }
            }
        } 
    }
    
    private void setPrem(){
        this.prem = 0;
        
        for(int i=0; i < this.rules.size(); i++){
            if(this.rules.get(i).isActive()){
                Pair<Boolean,Integer>[] cond = this.rules.get(i).getConditions();

                for(int j=0; j < cond.length; j++){
                    if(cond[j].getFirst().booleanValue()){
                        this.prem++;
                    }
                }
            }
        }
    }

    public double getPrem(){return this.prem;}
    public double getFsets(){return this.fsets;}

    public double fitness() {


        double result = corr*(beta*k(incorr) + dzeta*k(unclass)) + delta*k(prem) + eps*k(fsets);
                        
        return result;
    }

    Genom mutate() {
        Genom g = new Genom(this);
     
        for (int i = 0; i < g.rules.size(); i++) {
            FuzzyRule rr = g.rules.get(i);

            if (RandomClass.nextBoolean()) {
                rr.setActive(!rr.isActive());
            }

            if (RandomClass.nextBoolean()) {
                g.rules.set(i, rr.mutate());
            }
        }

        for (int i = 0; i < g.sets.size(); i++) {
            FuzzySet[] s = g.sets.get(i);

            for (FuzzySet fuzzySet : s) {

              if (RandomClass.nextBoolean()) {
                 fuzzySet.setActive(!fuzzySet.isActive());
              }
              
              if(RandomClass.nextBoolean())
                if (fuzzySet instanceof TriangleSet) {
                    double d1 = fuzzySet.getParams()[0];
                    
                    d1 = d1 + RandomClass.rDouble();
                    fuzzySet.setParam(d1);
                    fuzzySet.correct();
                    
                } else {

                    if (fuzzySet instanceof TrapeziumSet) {
                        double d1 = fuzzySet.getParams()[0],
                               d2 = fuzzySet.getParams()[1];

                        d1 = d1 + RandomClass.rDouble();
                        d2 = d2 + RandomClass.rDouble();

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
            }//foreach...
        }//for g.sets.size

        for(int ii=0; ii < 3; ii++){
            if(RandomClass.nextBoolean()){
                int i = RandomClass.nextInt(0,g.rules.size());
                int j = RandomClass.nextInt(0,g.rules.size());

                if(i != j){
                    FuzzyRule tmp = g.rules.get(i);
                    g.rules.set(i, g.rules.get(j));
                    g.rules.set(j, tmp);
                }
            }
        }
        
        g.repairGenom();
        g.setFsets();
        g.setPrem();
        
        return g;
    }


    ArrayList<FuzzySet[]> getSets(){return this.sets;}

    Genom[] crossWith(Genom _genom) {
        Genom newGen1 = new Genom(this);
        Genom newGen2 = new Genom(_genom);

        //Random RandomClass = new Random(System.currentTimeMillis());

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

        //RandomClass = new Random(System.currentTimeMillis() + RandomClass.nextInt(10000));

        for (int i = 0; i < newGen1.sets.size(); i++) {
            if (RandomClass.nextBoolean()) {
                FuzzySet[] s = newGen1.sets.get(i);
                newGen1.sets.set(i, newGen2.sets.get(i));
                newGen2.sets.set(i, s);
            }
        }

        newGen1.repairGenom();
        newGen2.repairGenom();
        newGen1.setFsets();
        newGen1.setPrem();
        newGen2.setFsets();
        newGen2.setPrem();
        
        return new Genom[]{newGen1, newGen2};
    }

    @Override
    public String toString() {   
        String result = "";
     
        for( int i = 0; i < rules.size(); i++){
            result += rules.toString() + "\n";
        }
        
        return result;
    }

    public String getRule(int i){
        if((i < this.rules.size()) && (i > -1)){
            return this.rules.get(i).toString();
        }else{
            return null;
        }
    }

    public int getRuleNumber(){return this.rules.size();}


    public static void setNumOfSets(int num) {
        numberOfSets = num;
    }

    public static int getNumOfSets() {
        return numberOfSets;
    }

    public int compareTo(Genom o) {
        double f1 = fitness(),f2 = o.fitness();
        return (f1 == f2) ? 0 : ((f1 > f2) ? -1 : 1);
    }
}
