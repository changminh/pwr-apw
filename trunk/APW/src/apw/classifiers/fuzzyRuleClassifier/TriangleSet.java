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

/**
 *
 * @author przemo
 */
class TriangleSet extends  FuzzySet {
    private TriangleSet left = null,right = null;
    private double dValue=0.0;
    private double zakres ;

    public TriangleSet(double zakres) {
        this.zakres = zakres;
        dValue = Math.random()*zakres;
    }

    @Override
    public void setParam(double... data) {
        if( data != null){
            this.dValue = data[0];
        }else{
            System.err.println("Paramatr funkcji setParam równy null");
        }
    }

    @Override
    public double[] getParams() {
        return new double[]{dValue};
    }

    @Override
    public double evaluate(double x) {
        double[] left_d = null;
        double[] right_d = null;

        if(this.left == null){
            if(x <= this.dValue){ return 1.0; }
        }else{
            left_d = left.getParams();
        }
        
        if(this.right == null){
            if(x >= this.dValue){ return 1.0; }
        }else{
            right_d = right.getParams();
        }

        if(x < dValue){
            if(left_d[0] <= x){
                return (x - left_d[0])/(dValue - left_d[0]);
            }
        }else{
            if(right_d[0] > x){
                return (right_d[0] - x)/(right_d[0] - dValue);
            }
        }
        
        return 0.0;
    }

    @Override
    public TriangleSet clone() {
        TriangleSet s = new TriangleSet(this.zakres);
        s.dValue = this.dValue;
        s.left = this.left;
        s.right = this.right;
        return s;
    }

    public void setLeft(TriangleSet _left){
        this.left = _left;
    }

    public void setRight(TriangleSet _right){
        this.right = _right;
    }

    public TriangleSet getLeft(){
        return this.left;
    }

    public TriangleSet getRight(){
        return this.right;
    }
}
