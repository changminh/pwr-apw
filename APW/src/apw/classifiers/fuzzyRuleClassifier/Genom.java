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
import java.util.Random;

/**
 *
 * @author przemo
 */
public class Genom {

    private ArrayList<FuzzyRule> rules = new ArrayList<FuzzyRule>();
    private ArrayList<FuzzySet[]> sets = new ArrayList<FuzzySet[]>();
    private static int numberOfSets = 4;

    private void setValues(int _rules, int _sets) {

        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < _rules; i++) {
            rules.add(new FuzzyRule());
        }

        for (int i = 0; i < _sets; i++) {
            switch (random.nextInt(3)) {
                case 0:{
                        TriangleSet[] _set = new TriangleSet[numberOfSets];
                        
                        _set[0].setRight(_set[1]);

                        for(int j=1;j<_set.length - 1;j++){
                            _set[j].setLeft (_set[j-1]);
                            _set[j].setRight(_set[j+1]);
                        }

                        _set[_set.length-1].setLeft(_set[_set.length-2]);

                        sets.add(_set);
                    }break;
                case 1:{
                         TrapeziumSet[] _set = new TrapeziumSet[numberOfSets];
                        
                        _set[0].setRight(_set[1]);

                        for(int j=1;j<_set.length - 1;j++){
                            _set[j].setLeft (_set[j-1]);
                            _set[j].setRight(_set[j+1]);
                        }

                        _set[_set.length-1].setLeft(_set[_set.length-2]);

                        sets.add(_set);
                    } break;
                case 2:{
                        sets.add(new GaussFuzzySet[numberOfSets]);
                    }break;
                    
            }
        }

    }

    public Genom(){}
    public Genom(int _rules, int _sets) {
        this.setValues(_rules, _sets);
    }

    public Genom(Genom _genom) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    Genom mutate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    Genom crossWith(Genom _genom) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void setNumOfSets(int num){
        numberOfSets = num;
    }
    public static int getNumOfSets(){return numberOfSets;}

}
