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
 *
 * @author przemo
 */

public class GaussFuzzySet extends FuzzySet{
    private double dValue=0,
                   sigma=0;
    private DecimalFormat df = new DecimalFormat("#####.###");

    //private static Random rand = new Random((int)(Math.random()*10000));
    private double zakres;

    public GaussFuzzySet(double zakres){
       this.zakres = zakres;
       dValue = RandomClass.nextDouble()*zakres;
       sigma  = Math.pow(zakres,2)/400.0;
    }

    @Override
    public void setParam(double... data) {
        if((data != null) && (data.length > 1)){
            dValue = data[0];
            sigma  = data[1];
        }else{
            System.err.println("Paramatr funkcji setParam równy null albo mniejszt rowny 1");
        }
    }

    @Override
    public double[] getParams() {
        return new double[]{dValue,sigma};
    }

    @Override
    public double evaluate(double x) {
        return Math.exp(-Math.pow(x - dValue, 2.0)/(2.0*sigma));
    }

    @Override
    public GaussFuzzySet clone() {
        GaussFuzzySet s = new GaussFuzzySet(zakres);
        s.dValue = this.dValue;
        s.sigma = this.sigma;
        return s;
    }

    @Override
    public void correct() {}

    @Override
    public String toString() {
        //DecimalFormat df = new DecimalFormat("#####.####");
        return "Aktywny: " + (isActive()?1:0) + "; d : " + df.format(dValue) +"; s: " + df.format(sigma) + ";";
    }

    @Override
    public FuzzySet getLeft() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FuzzySet getRight() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public void setLeft(FuzzySet s) {
       //this.left = (GaussFuzzySet)s;
    }

    @Override
    public void setRight(FuzzySet s) {
        //this.right =  (GaussFuzzySet)s;
    }

}
