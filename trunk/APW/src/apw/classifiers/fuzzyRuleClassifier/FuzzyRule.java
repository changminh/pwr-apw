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
import java.util.Random;

/**
 *
 * @author Przemek Woś
 */

public class FuzzyRule {
    private boolean isActive=true;
    private Pair<Boolean,Integer>[] condition=null;
    private String conclusion=null;
    private ArrayList<FuzzySet[]> sets = null; //referncja do list zbiorow rozmytch w genomie,
                                               //do ktorego nalezy regula
    private double and(double a, double b){
        return (a < b)?a:b;
    }

    public void repairRule(){
        int count=0;
        for(int i = 0;i < condition.length; i++){
            if(condition[i].getFirst().booleanValue()){
                count++;
                break;
            }
        }

        if(count == 0){
            int index = new Random(System.currentTimeMillis() + System.nanoTime()).nextInt(condition.length);
            condition[index].setFirst(new Boolean(true));
        }
    }

    public FuzzyRule mutate(){
        FuzzyRule rule = new FuzzyRule(this);
        //Random RandomClass = new Random(System.currentTimeMillis() + System.nanoTime());

        for(int i=0; i < condition.length; i++){
            if(RandomClass.nextBoolean()){
                boolean boolValue = rule.condition[i].getFirst().booleanValue();
                rule.condition[i].setFirst(!boolValue);
            }
            
            if(RandomClass.nextBoolean()){
                int index = rule.condition[i].getSecond().intValue();
                int value = RandomClass.nextInt(index, sets.get(i).length);
                rule.condition[i].setSecond(value);
            }
        }

        rule.repairRule();
        
        return rule;
    }

    public FuzzyRule(FuzzyRule rule){
       this(rule.condition, rule.conclusion,rule.sets);
       this.isActive = rule.isActive;
    }

    public FuzzyRule(Pair<Boolean,Integer>[] _condition, String con, ArrayList<FuzzySet[]> a){
        this.conclusion = new String(con);
        
        Pair<Boolean, Integer>[] cond = new Pair[_condition.length];

        for(int i=0; i < _condition.length; i++){
            Boolean b = new Boolean(_condition[i].getFirst());
            Integer ii = new Integer(_condition[i].getSecond());
            cond[i] = new Pair(b,ii);
        }
        
        this.condition = cond;
        this.sets  = a;
    }

    public boolean isActive(){
        return isActive;
    }

    public void setActive(boolean _act){this.isActive = _act;}



    private int findNearest(FuzzySet[] _sets,int index){
        int i1=Integer.MAX_VALUE,
            i2=Integer.MAX_VALUE;
        
        for(int i=index+1; i < _sets.length; i++){
            if(_sets[i].isActive()){
                i1++; break;
            }else{
                i1++;
            }
        }

        for(int i=index-1; i >= 0; i--){
            if(_sets[i].isActive()){
                i2++; break;
            }else{
                i2++;
            }
        }
        
        return (i1==12)?i1:((i1>i2)?i2:i1);
    }


    public double classiify(Sample s){
        double result = Double.MAX_VALUE;

        if(s.size() - 1 != condition.length){
            System.err.println("Dlugosc probek nie odpowiednia w metodzie FuzzyRule::classify");
        }


        for(int i=0;i < condition.length; i++){
            if(condition[i].getFirst().booleanValue() == true){

                int index = condition[i].getSecond().intValue();

                double x = Double.valueOf(s.get(i).toString()).doubleValue();
                double value = Double.MAX_VALUE;
              
                if(sets.get(i)[index].isActive()){
                    value = sets.get(i)[index].evaluate(x);
                }else{
                    int ii = findNearest(sets.get(i), index);
                    value = sets.get(i)[ii].evaluate(x);
                }

                result = and(result, value);
            }
        }
        
        return result;
    }

    @Override
    public String toString(){
        String result = "(Aktywana: " + (isActive()?1:0) + ";" + " przeslanki:";
        
        for(int i = 0; i < this.condition.length;i++ ){
            int ac = (this.condition[i].getFirst().booleanValue() == true)?1:0;
            result += "(" + ac + "; " + this.condition[i].getSecond().intValue() + ")";
        }

        result += "; konlkuzja:(" + this.conclusion + "))";
        return result;
    }

    @Override
    public FuzzyRule clone(){
        return new FuzzyRule(this);
    }

    public ArrayList<FuzzySet[]> getSets(){return this.sets;}
    public void setSets(ArrayList<FuzzySet[]> a){ this.sets = a;}
    public String getConclusion(){ return new String(this.conclusion); }
    public Pair<Boolean,Integer>[] getConditions(){return this.condition;}
}
