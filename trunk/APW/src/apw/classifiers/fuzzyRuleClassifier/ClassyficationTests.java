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
import apw.core.Samples;
import apw.core.loader.ARFFLoader;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Przemek Wos
 */
public class ClassyficationTests {

    private static Samples normalize(Samples nSamples) {
        double length;
        Samples _samples = new Samples(nSamples.getAtts());

        for (int i = 0; i < nSamples.size(); i++) {
            length = 0.0;
            int size = nSamples.get(i).size() - 1;

            for (int j = 0; j < size; j++) {
                double data = Double.parseDouble(nSamples.get(i).get(j).toString());
                length += data * data;
            }

            length = Math.sqrt(length);

            List<Object> list = new ArrayList<Object>();

            for (int j = 0; j < size; j++) {
                double data = Double.parseDouble(nSamples.get(i).get(j).toString());
                data /= length;
                String str = Double.toString(data);
                Object obj = nSamples.getAtts().get(j).getRepresentation(str);
                list.add(obj);
            }

            Object obj = nSamples.getAtts().get(size).
                    getRepresentation(nSamples.get(i).get(size).toString());

            list.add(obj);

            Samples _s = new Samples(nSamples.getAtts());
            Sample newSample = new Sample(_s, list.toArray());
            _samples.add(newSample);
        }

        return _samples;
    }

    public static Samples[] makeSets(String source, final double procent) {

        if (procent < 0.0 || procent > 100.0) {
            throw new IllegalStateException("Zmienna procent ma zly zakres(oczekiwany: 0.0 - 100.0)");
        }

        double Procent = procent / 100.0;


        try {
            Samples allSamples = normalize(new ARFFLoader(new File(source)).getSamples());
            HashMap<String, ArrayList<Integer>> learningData = new HashMap<String, ArrayList<Integer>>();
            HashMap<String, ArrayList<Integer>> testData = new HashMap<String, ArrayList<Integer>>();

            for (int i = 0; i < allSamples.size(); i++) {
                Sample s = allSamples.get(i);
                String str = s.get(s.size() - 1).toString();

                if (!learningData.containsKey(str)) {
                    ArrayList<Integer> value = new ArrayList<Integer>();
                    value.add(i);
                    learningData.put(str, value);
                } else {
                    learningData.get(str).add(i);
                }
            }

            Object[] classes = learningData.keySet().toArray();

            for (int i = 0; i < classes.length; i++) {
                ArrayList<Integer> lIndexes = learningData.get(classes[i].toString());
                ArrayList<Integer> tIndexes = new ArrayList<Integer>();

                long howMany = Math.round(lIndexes.size() * Procent);
                int size = lIndexes.size() - 1;

                for (int j = 0; j < howMany; j++) {
                    tIndexes.add(lIndexes.get(size - j).intValue());
                    lIndexes.remove(size - j);
                }

                testData.put(classes[i].toString(), tIndexes);
            }

            Samples lSamples = new Samples(allSamples.getAtts());
            Samples tSamples = new Samples(allSamples.getAtts());

            for (int i = 0; i < classes.length; i++) {
                int size = learningData.get(classes[i].toString()).size();

                for (int j = 0; j < size; j++) {
                    int index = learningData.get(classes[i].toString()).get(j).intValue();
                    lSamples.add(allSamples.get(index));
                }
            }

            for (int i = 0; i < classes.length; i++) {
                int size = testData.get(classes[i].toString()).size();
                for (int j = 0; j < size; j++) {
                    int index = testData.get(classes[i].toString()).get(j).intValue();
                    tSamples.add(allSamples.get(index));
                }
            }

            return new Samples[]{lSamples, tSamples};

        } catch (IOException ex) {
            Logger.getLogger(ClassyficationTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ClassyficationTests.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
    FuzzyRuleClassifier fuzzy = null;
    int howManyTests = 5;
    double procent = 50.0;

    public ClassyficationTests() {
    }

    public String[] makeTest1(String source) throws Exception {
        Samples[] data = makeSets(source, procent);
        Samples learningSet = data[0];
        Samples testingSet = data[1];

        String[] result = new String[howManyTests+1];

        if (testingSet.size() == 0) {

            result[0] = "Training Set Empty...";
            return result;
        }
        double good=0,bad=0;

        for (int i = 0; i < howManyTests; i++) {
            String[] ops = new String[]{
                "-o", "10",           //liczba osobnikow przypadajaca na populacje
                "-r", "15",            //liczba regul przypadajaca na jedna klase
                "-f", "5",            //liczba zbiorow rozmytych przypadajaca na jedna grupe
                "-t", "3",            //typ zbiorów 0 - gauss, 1 - trojkatny, 2 - trapezowy 3 - mieszane
                "-m", "0.7",          //prawdopodobienstwo mutacji
                "-c", "0.7",          //prawdopodobienstwo krzyzowania
                "-bp", "5",          //procent o jaki ma sie zwiekszyc poprawne klasyfikoanie zbioru uczacego
                                      //gdy wywolamy metode rebuid
                "-mg", "10000",       //maksymalna liczba epok jaka trwa uczenie

                "-b", "0.5",          //wspolczynnik beta w funckji przystosowania
                                      //tzn. jak bardzo bierzemy pod uwage zle zaklasyfikowania

                "-d", "0.4",          //wspolczynnik delta w funckji przystosowania
                                      //tzn. jak bardzo bierzemy pod uwage brak zaklasyfikowania

                "-e", "0.4",          //wspolczynnik epsilon w funckji przystosowania
                                      //tzn. jak bardzo bierzemy licybe aktzwnzch
                                      //pryeslanek we wszystkich regulach

                "-z", "0.3",          //wspolczynnik dzeta w funckji przystosowania
                                      //tzn. jak bardzo bierzemy pod uwage brak zaklasyfikowania

                "-rp", "65.0",        //procent poprawnej klasyfikacji po jakim
                                      //osobnik zostanie zakceptowany jako rozwiazanie

                "-N" , "1",           // czy normalizujemy dane 0 - nie 1 - tak
                
                "-gw", "100"};        //liczba epok po ktorej,
                                      ///jesli nic sie nie zmieni, uczenie zostanie przerwane


            fuzzy = new FuzzyRuleClassifier(learningSet);
            fuzzy.setOptions(ops);
            fuzzy.buildClassifier();

            double wrong = 0, right = 0;

            for (int j = 0; j < testingSet.size(); j++) {
                String mustBe = testingSet.get(j).get(testingSet.get(j).size() - 1).toString();

                double[] cl = fuzzy.classifySample(testingSet.get(j));

                if (cl == null) {
                    wrong++;
                } else {
                    String was = fuzzy.interprate(cl);

                    if (was.compareTo(mustBe) == 0) {
                        right += 1.0;
                    } else {
                        wrong += 1.0;
                    }
                }
            }

            good += right;
            bad += wrong;


            result[i] = "Test Number: " + i +
                    " Classified good: " + right +
                    " Classified Bad: " + wrong +
                    " Good Procent: " + (100.0*right / (right + wrong));
            System.out.print("*");
        }
        
        result[howManyTests] = "Srednio zaklasyfikowal dobrze: " + (good/howManyTests) +
                               "\n" +
                               "Srednio zle zaklysifikowal: " + (bad/howManyTests);

        fuzzy = null;

        return result;
    }

    public String[] makeTest2(String source) {
        Samples[] data = makeSets(source, procent);
        Samples learningSet = data[0];
        Samples testingSet = data[1];

        String[] result = new String[howManyTests + 1];

        if (testingSet.size() == 0) {

            result[0] = "Training Set Empty...";
            return result;
        }

        double good=0,bad=0;

        for (int i = 0; i < howManyTests; i++) {
            String[] ops = new String[]{
                "-o", "6",      //liczba osobnikow przypadajaca na populacje
                "-r", "20",     //liczba regul przypadajaca na jedna klase
                "-f", "10",      //liczba zbiorow rozmytych przypadajaca na jedna grupe
                "-t", "0",      //typ zbiorów 0 - gauss, 1 - trojkatny, 2 - trapezowy 3 - mieszane
                "-m", "0.7",    //prawdopodobienstwo mutacji
                "-c", "0.5",    //prawdopodobienstwo krzyzowania
                "-bp", "50",    //procent o jaki ma sie zwiekszyc poprawne klasyfikoanie zbioru uczacego
                                //gdy wywolamy metode rebuid
                "-mg", "10000", //maksymalna liczba epok jaka trwa uczenie

                "-b", "0.5",    //wspolczynnik beta w funckji przystosowania
                                //tzn. jak bardzo bierzemy pod uwage zle zaklasyfikowania

                "-d", "0.4",    //wspolczynnik delta w funckji przystosowania
                                //tzn. jak bardzo bierzemy pod uwage brak zaklasyfikowania

                "-e", "0.4",    //wspolczynnik epsilon w funckji przystosowania
                                //tzn. jak bardzo bierzemy licybe aktzwnzch
                                //pryeslanek we wszystkich regulach

                "-z", "0.3",    //wspolczynnik dzeta w funckji przystosowania
                                //tzn. jak bardzo bierzemy pod uwage brak zaklasyfikowania

                "-rp", "70.0",  //procent poprawnej klasyfikacji po jakim
                                //osobnik zostanie zakceptowany jako rozwiazanie
                
                "-N" , "1",     // czy normalizujemy dane 0 - nie 1 - tak

                "-gw", "20"};  //liczba epok po ktorej,
                                //jesli nic sie nie zmieni, uczenie zostanie przerwane
                                //jesli nic sie nie zmieni, uczenie zostanie przerwane

            try {
               
                fuzzy = new FuzzyRuleClassifier(learningSet);
                fuzzy.setOptions(ops);
                fuzzy.buildClassifier();
                double wrong = 0;
                double right = 0;
                for (int j = 0; j < testingSet.size(); j++) {
                    String mustBe = testingSet.get(j).get(testingSet.get(j).size() - 1).toString();
                    double[] cl = fuzzy.classifySample(testingSet.get(j));
                    if (cl == null) {
                        wrong++;
                    // System.out.print("Null dla " + j);
                    } else {
                        String was = fuzzy.interprate(cl);
                        if (was.compareTo(mustBe) == 0) {
                            right += 1.0;
                        } else {
                            wrong += 1.0;
                        }
                    }
                }
                good += right;
                bad += wrong;

                result[i] = "Test Number: " + i + " Classified good: " + right + " Classified Bad: " + wrong + " Good Procent: " + (100.0*right / (right + wrong));
                System.out.print("*");
            } catch (Exception ex) {
                Logger.getLogger(ClassyficationTests.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        result[howManyTests] = "Srednio zaklasyfikowal dobrze: " + (good/howManyTests) +
                               "\n" +
                               "Srednio zle zaklysifikowal: " + (bad/howManyTests);

        fuzzy = null;

        return result;
    }

    

    public static void main(String[] args) {
        try {

            ClassyficationTests tests = new ClassyficationTests();

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
            Logger.getLogger(ClassyficationTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
