package apw.classifiers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

import apw.classifiers.c4_5.C4_5;
import apw.classifiers.c4_5.complex.Complex;
import apw.classifiers.c4_5.complex.ComplexSet;
import apw.classifiers.id3.ID3;
import apw.classifiers.knn.KNN;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;

public abstract class ClassifierTest {

	/**
	 * @param args
	 */
	
	public static double evaluateClassifier(Classifier classifier,Samples test)
	{
        int ok = 0;
        for (Sample sample : test) {
             Object result = classifier.classifySampleAsObject(sample);
             Object correct = sample.classAttributeInt();
             
             //System.out.println("class: "+result.toString());
             //System.out.println(Arrays.toString(probs));
             //System.out.println(sample);
             //System.out.println("correct: "+correct);
             //System.out.println();

             if(result.equals(correct)) ok++;
         
             
         }
        return (double)ok/(double)test.size();
	}
	
    public static void main(String[] args) 
    {
//      File f = new File("data/weather.nominal.arff");
//    	File f = new File("data/weather.arff");
//    	File f = new File("data/shuttle.arff");
//    	File f = new File("data/shuttle2.arff");
//      File f = new File("data/iris.arff");
      File f = new File("data/soybean.arff");
//      File f = new File("data/segment-test.arff");
//      File f = new File("data/labor.arff");
//    	File f = new File("data/contact-lenses.arff");
//      File f = new File("data/cpu.arff");

      Random r = new Random(System.nanoTime());
//      Random r = new Random(3323);

      System.out.println("Loading data.");
      ARFFLoader l;
	try 
	{
		l = new ARFFLoader(f);
		Samples s = l.getSamples();
	      Samples test = s.copyStructure();
	      for (int i = 0; i < Math.min(s.size()/3,100); i++)
	      {
	    	  test.add(s.remove(r.nextInt(s.size())));
//	        test.add(s.get(r.nextInt(s.size())));
	      }
	      
	      System.out.println("Dataset size: " + s.size() + ". Building classifier.");

//	      	if (classifier instanceof RuleClassifier) 
//	      	{
//				RuleClassifier rc = (RuleClassifier) classifier;
//				String[] rules = rc.getRules();
//				for (String string : rules) {
////					System.out.println(string);
//				}
//			}
	            
//        C4_5 r1 = new C4_5(s);
//        ComplexSet r2 = new ComplexSet(s);
//        
//        for(Complex c:r2.getComplexes())
//        {
//        	c.evaluate(test);
//        }
//       
//        String[] s1 = r1.getRules();
//        String[] s2 = r2.getRules();
//        
//        for (int i = 0; i < s1.length; i++) {
////			System.out.println(s1[i]);
////			System.out.println(s2[i]);
////			System.out.println();
//        }
//        System.out.println();
//        for (int i = 0; i < s2.length; i++) {
//			System.out.println(s2[i]);
//        }
//	  	System.out.println("C4_5 RATE:"+evaluateClassifier(r1,test));
//	  	System.out.println("C4_5_TRIMMED RATE:"+evaluateClassifier(r2,test));

	  	System.out.println("KNN_1 RATE:"+evaluateClassifier(new KNN(s),test));
	  	System.out.println("KNN_SIMPLE RATE:"+evaluateClassifier(new KNN(s, 4, KNN.SIMPLE_VOTING),test));
	  	System.out.println("KNN_DISTANCE RATE:"+evaluateClassifier(new KNN(s, 4, KNN.DISTANCE_BASED_VOTING),test));
	  	System.out.println("KNN_RANKING RATE:"+evaluateClassifier(new KNN(s, 4, KNN.RANKING_BASED_VOTING),test));
	  	System.out.println("C4_5 RATE:"+evaluateClassifier(new C4_5(s),test));
	  	System.out.println("C4_5_TRIMMED RATE:"+evaluateClassifier( new ComplexSet(s),test));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


    }
}
