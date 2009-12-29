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

import java.text.DecimalFormat;

/**
 * @author  Przemek Woś
 */

class TrapeziumSet extends  FuzzySet{
    private TrapeziumSet left = null,
                         right = null;
    
    private double d1=0.0,
                   d2=0.0;

    private double zakres;
    private DecimalFormat df = new DecimalFormat("#####.###");
    
    public TrapeziumSet(double zakres){
        this.zakres = zakres;
        d1= Math.random()*zakres;
        d2= Math.random()*zakres;

        if(d1 > d2){
            double tmp = d1;
            d1=d2;
            d2=tmp;
        }
    }

    @Override
    public void setParam(double... data) {
         if((data != null) && (data.length > 1)){
            d1 = data[0];
            d2 = data[1];
        }else{
             System.err.println("Paramatr funkcji setParam równy null albo mniejszt rowny 1");
        }
    }

    @Override
    public double[] getParams() {
        return new double[]{d1,d2};
    }

    @Override
    public double evaluate(double x) {
        if((x >= d1)&&(x <= d2)){
            return 1.0;
        }

        double left_d=0;
        double right_d=0;

        if(left == null){
            if(x < d1){ return 1.0; }
        }else{
            left_d = left.getParams()[1];
        }

        if(right == null){
            if(x > d2){ return 1.0; }
        }else{
            right_d = right.getParams()[0];
        }


        if(x < d1){
            if(left_d <= x){
                return (x - left_d)/(d1 - left_d);
            }
        }else{
            if(right_d >= x){
                return (right_d - x)/(right_d - d2);
            }
        }
        
        return 0.0;
    }

    @Override
    public TrapeziumSet clone() {
        TrapeziumSet s = new TrapeziumSet(this.zakres);
        s.d1 = this.d1;
        s.d2 = this.d2;
        return s;
    }
    
    @Override
    public void correct() {
        if(d1 > d2){
            double tmp = d1;
            d1=d2;
            d2=tmp;
        }
    }

    @Override
    public String toString() {
        
        return "Aktywny: " + (isActive()?1:0) + "; dl: " + df.format(d1) +"; dr: " + df.format(d2) + ";";
    }

    @Override
    public FuzzySet getLeft() {
       return this.left;
    }

    @Override
    public FuzzySet getRight() {
        return this.right;
    }
    
    @Override
    public void setLeft(FuzzySet s) {
       this.left = (TrapeziumSet)s;
    }

    @Override
    public void setRight(FuzzySet s) {
        this.right =  (TrapeziumSet)s;
    }

}
