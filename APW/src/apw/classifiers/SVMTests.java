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

package apw.classifiers;

import apw.classifiers.fuzzyRuleClassifier.ClassyficationTests;
import apw.core.Samples;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author przemo
 */
public class SVMTests {
    SVMClassifier svm = null;
    
    int howManyTests = 20;
    double procent = 20.0;

    public SVMTests(){
    }

    public Samples[] makeSets(String source,final double procent){
        return ClassyficationTests.makeSets(source, procent);
    }

    public String[] makeTest1(String source) throws Exception{
       
        Samples[] data = makeSets(source, procent);
        Samples learningSet = data[0];
        Samples testingSet  = data[1];

        String[] result = new String[howManyTests+1];

        if(testingSet.size() == 0){
            result[0] = "Training Set Empty...";
            return result;
        }

        double good=0,bad=0;


        for(int i = 0; i < howManyTests; i++){
             String[] ops = {
                
                new String("-S"), // WLSVM options
                new String("3"),  // Classification problem

                new String("-K"), // RBF kernel
                new String("1"),

                new String("-G"), // gamma
                new String("3"),

                new String("-D"),
                new String("3"),

                new String("-R"),
                new String("5"),

                new String("-C"), // C
                new String("3"),

                new String("-E"), 
                new String("0.5"),

                new String("-Z"), // normalize input data
                new String("1"),

                new String("-M"), // cache size in MB
                new String("100")
            };

            svm = new SVMClassifier(learningSet);
            svm.setOptions(ops);
            svm.buildClassifier();

            double wrong = 0,right = 0;

            for(int j=0; j < testingSet.size();j++){
                String mustBe = testingSet.get(j).get(testingSet.get(j).size()-1).toString();
                String was = svm.interprate(svm.classifySample(testingSet.get(j)));

                if (was == null) {
                    wrong+=1.0;
                }else{
                    if(was.compareTo(mustBe) ==  0){
                        right+=1.0;
                    }else{
                        wrong+=1.0;
                    }
                }
            }

            result[i] = "Test Number: " + i +
                        " Classified good: " + right +
                        " Classified Bad: " + wrong + 
                        " Good Procent: " + (100.0*right/(right+wrong));

            good += right;
            bad += wrong;

        }

        svm = null;

        result[howManyTests] = "Srednio zaklasyfikowal dobrze: " + (good/howManyTests) + 
                               "\n" +
                               "Srednio zle zaklysifikowal: " + (bad/howManyTests);

        return result;
    }
    public String makeTest2(){
        return null;
    }
    public String makeTest3(){
        return null;
    }




    public static void main(String[] args){
        try {
            
            SVMTests tests = new SVMTests();

            System.out.println("*******Testowanie Iris*******");
            String[] result = tests.makeTest1("c:/svm/data/iris.arff");
            
            for (int i = 0; i < result.length; i++) {
                System.out.println(result[i]);
            }
            
            System.out.println("*******Testowanie Wine*******");
            result = tests.makeTest1("c:/svm/data/wine.arff");

            for (int i = 0; i < result.length; i++) {
                System.out.println(result[i]);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(SVMTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
