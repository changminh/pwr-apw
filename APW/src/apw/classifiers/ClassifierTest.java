package apw.classifiers;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Random;

import apw.classifiers.c4_5.C4_5;
import apw.classifiers.id3.ID3;
import apw.classifiers.knn.KNN;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;

public abstract class ClassifierTest {

	/**
	 * @param args
	 */
    public static void main(String[] args) {
        // TODO to be removed
//*      File f = new File("data/weather.nominal.arff");
      File f = new File("data/weather.arff");
//        File f = new File("data/shuttle.arff");
//        File f = new File("data/iris.arff");
//      File f = new File("data/soybean.arff");
//*      File f = new File("data/segment-test.arff");
//        File f = new File("data/labor.arff");
//*    	File f = new File("data/contact-lenses.arff");
//      File f = new File("data/cpu.arff");
      int g_ok = 0;
      int g_sum = 0;

      for(int it=0;it<1;it++)
      {
    	try {
            System.out.println("Loading data.");
            ARFFLoader l = new ARFFLoader(f);
            Samples s = l.getSamples();
         
            Random r = new Random(System.nanoTime());
//            Random r = new Random(451);
            LinkedList<Sample> test = new LinkedList<Sample>();
            for (int i = 0; i < Math.min(s.size()/4,50); i++)
            {
                test.add(s.remove(r.nextInt(s.size())));
//            	test.add(s.get(r.nextInt(s.size())));
            }
            System.out.println("Dataset size: " + s.size() + ". Building classifier.");

            Classifier classifier;
//            classifier = new KNN(s);
//            classifier = new KNN(s, 4, KNN.SIMPLE_VOTING);
//            classifier = new KNN(s, 4, KNN.DISTANCE_BASED_VOTING);
//          classifier = new KNN(s, 4, KNN.RANKING_BASED_VOTING);
//            classifier = new ID3(s);
            classifier = new C4_5(s);
         
            
           boolean numeric = false;
            if(numeric)
            {
            	double diff = 0.0;
            for (Sample sample : test) {
            	double[] probs;
                Object result = classifier.classifySampleAsObject(sample);
                //System.out.println();
                Object correct = sample.classAttributeInt();
                //System.out.println();
                
                System.out.println("class: "+result.toString());
                //System.out.println(Arrays.toString(probs));
                //System.out.println(sample);
                System.out.println("correct: "+correct);
                double dif = Math.abs((((Double)correct)-((Double)result))/((Double)correct));
                diff += dif;
                System.out.println("dif: "+dif);
                System.out.println();
                
            
                
            }
            	System.out.println("AVERAGE DIF:"+(diff*100/test.size())+"%");
            }
            else
            {
                int ok = 0;
               for (Sample sample : test) {
                	double[] probs;
                    Object result = classifier.classifySampleAsObject(sample);
                    //System.out.println();
                    Object correct = sample.classAttributeInt();
                    //System.out.println();
                    
                    //System.out.println("class: "+result.toString());
                    //System.out.println(Arrays.toString(probs));
                    //System.out.println(sample);
                    //System.out.println("correct: "+correct);
                    //System.out.println();
                    if(result.equals(correct)) ok++;
                
                    
                }
               g_ok+=ok;
               g_sum+=test.size();
                	System.out.println("RATE:"+(ok*100/test.size())+"%");
            	
            }

            //System.out.println("Testing classification rate.");
            //System.out.println("Classification rate " + s.getCorrectClassificationRate(myKNN));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      
    }
  	System.out.println("RATE:"+((double)g_ok/(double)g_sum));

    }
}
