package apw.classifiers.knn;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import apw.classifiers.Classifier;
import apw.classifiers.c4_5.C4_5;
import apw.classifiers.id3.ID3;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.loader.ARFFLoader;

public class KNN extends Classifier {

	private static final double DOUBLE_PRECISION = 0.00000000001;
	
	private boolean numeric = false;
	
	public static final int SIMPLE_VOTING = 1;
	public static final int RANKING_BASED_VOTING = 2;
	public static final int DISTANCE_BASED_VOTING = 3;
	
	private int votingType = SIMPLE_VOTING;
    private ArrayList<Attribute> attributes;
    private int k;
    private LinkedList<Sample> toAdd = new LinkedList<Sample>();
    private LinkedList<Sample> samples = new LinkedList<Sample>();
    private Samples samples_desc;
    private int[] sizes;// table stores sizes of attributes
	/*
     * if attribute[i] is Numeric, size[i]=0
     * if attribute[i] is Nominal, size[i]=number of classes
     * */
    double[] max;//maximum values of attributes
    double[] min;//minimum values of attributes

    /**
     * @author Krzysztof Praszmo
     */
    /**
     * Creates new KNN Classifier based on given Samples with number of
     * neighbors equals 1
     *
     * @param samples -
     *            training examples
     * @see Classifier
     */
    public KNN(Samples samples) {
        super(samples);
        this.votingType = SIMPLE_VOTING;
        samples_desc = samples;
        this.attributes = samples.getAtts();
        this.k = 1;
        for (Sample sample : samples)
            toAdd.add(sample);
        init();
    }

    /**
     * Creates new KNN Classifier based on given Samples and number of neighbors
     *
     * @param samples -
     *            training examples
     * @param k -
     *            number of nearest neighbors
     * @param votingType -
     *            Voting type {SIMPLE_VOTING,RANKING_BASED_VOTING,DISTANCE_BASED_VOTING}
     * @see Classifier
     */
    public KNN(Samples samples, int k,int votingType) {
        super(samples);
        this.votingType = votingType;
        samples_desc = samples;

        this.attributes = samples.getAtts();
        this.k = k;
        for (Sample sample : samples)
            toAdd.add(sample);
        init();
    }

    private void init() {
        max = new double[toAdd.get(0).toDoubleArray().length];
        min = new double[toAdd.get(0).toDoubleArray().length];
        Arrays.fill(max, -Double.MAX_VALUE);
        Arrays.fill(min, Double.MAX_VALUE);
        rebuild();
    }

    @Override
    public void rebuild() {
        while (!toAdd.isEmpty()) {
            Sample s = toAdd.pop();
            samples.add(s);
            double[] vals = s.toDoubleArray();
            for (int i = 0; i < vals.length; i++) {
                max[i] = Math.max(max[i], vals[i]);
                min[i] = Math.min(min[i], vals[i]);
            }
        }
        
    }

    @Override
    public void addSample(Sample s) throws UnsupportedOperationException {
        toAdd.add(s);
    }

    @Override
    public void addSamples(Samples s) throws UnsupportedOperationException {
        toAdd.addAll(s);
    }

    @Override
    public double[] classifySample(Sample s) {
        LinkedList<KNNSortableSample> bests = new LinkedList<KNNSortableSample>();
        double[] vals = s.toDoubleArray();
        double[] dvals = new double[vals.length];
        for (int i = 0; i < vals.length; i++)
        {
            if (max[i] - min[i]<=DOUBLE_PRECISION)
                dvals[i] = 0;
            else
                dvals[i] = ((Double) vals[i] - min[i]) / (max[i] - min[i]);
        }
        for (Sample sample : samples) {
            double r = dif(sample, dvals);
            if (bests.size() < k)
                bests.add(new KNNSortableSample(r, sample));
            else if (bests.get(0).getRange() > r)
                bests.add(new KNNSortableSample(r, sample));
            if (bests.size() > k + 20) {
                Collections.sort(bests);
                while (bests.size() != k) {
                    bests.remove(0);
                }
            }

        }
        Collections.sort(bests);
        while (bests.size() != k) {
            bests.remove(0);
        }
        
        Attribute classAttribute = samples_desc.getClassAttribute();
        if (classAttribute instanceof Nominal) 
        {
        	if(votingType==SIMPLE_VOTING)
        	{
    			Nominal nominal = (Nominal) classAttribute;
    	        String[] keys = nominal.getSortedIKeys();
    	        double[] result = new double[keys.length];
    	        for (KNNSortableSample knnss : bests) 
    	        {
    	            String key = (String) knnss.getSample().get(attributes.indexOf(samples_desc.getClassAttribute()));
    	            for (int i = 0; i < result.length; i++)
    	                if (keys[i].equals(key))
    	                    result[i] += 1.0;
    	        }
    	        return result;        		
        	}
        	if(votingType==DISTANCE_BASED_VOTING)
        	{
    			Nominal nominal = (Nominal) classAttribute;
    	        String[] keys = nominal.getSortedIKeys();
    	        double[] result = new double[keys.length];
    	        for (KNNSortableSample knnss : bests) 
    	        {
    	            String key = (String) knnss.getSample().get(attributes.indexOf(samples_desc.getClassAttribute()));
    	            for (int i = 0; i < result.length; i++)
    	                if (keys[i].equals(key))
    	                {
    			        	double weight = knnss.getRange();
    			        	weight = 1.0-weight;
    			        	if(weight<0.0)weight = 0.0;
    			        	result[i] += weight;
    	                }
    	        }
    	        return result;        		
        	}
        	if(votingType==RANKING_BASED_VOTING)
        	{
    			Nominal nominal = (Nominal) classAttribute;
    	        String[] keys = nominal.getSortedIKeys();
    	        double[] result = new double[keys.length];
		        double score = 1;
		        for (KNNSortableSample knnss : bests) 
    	        {
    	            String key = (String) knnss.getSample().get(attributes.indexOf(samples_desc.getClassAttribute()));
    	            for (int i = 0; i < result.length; i++)
    	                if (keys[i].equals(key))
    	                {
    			        	result[i] += score;
    	                }
    	            score+=1.0;
    	        }
    	        return result;        		
        	}
			
		}
        if (classAttribute instanceof Numeric) 
        {
        	if(votingType == SIMPLE_VOTING)
        	{
	        	double result = 0.0;
		        for (KNNSortableSample knnss : bests) 
		        {
		           result+=((Double)knnss.getSample().get(attributes.indexOf(samples_desc.getClassAttribute())));
		        }
		        result/=(double)bests.size();
		        return new double[]{result};
        	}
        	if(votingType == DISTANCE_BASED_VOTING)
        	{
	        	double result = 0.0;
	        	double sum = 0.0;
		        for (KNNSortableSample knnss : bests) 
		        {
		        	double val = ((Double)knnss.getSample().get(attributes.indexOf(samples_desc.getClassAttribute())));
		        	double weight = knnss.getRange();
		        	weight = 1.0-weight;
		        	if(weight<0.0)weight = 0.0;
		        	sum+=weight;
		        	result+=weight*val;
		        	   
		        }
		        result/=sum;
		        return new double[]{result};
        	}
        	if(votingType == RANKING_BASED_VOTING)
        	{
	        	double result = 0.0;
	        	double sum = 0.0;
		        double score = 1;
	        	for (KNNSortableSample knnss : bests) 
		        {
		        	double val = ((Double)knnss.getSample().get(attributes.indexOf(samples_desc.getClassAttribute())));
		        	sum+=score;
		        	result+=score*val;
		        	score+=1.0;
		        }
		        result/=sum;
		        return new double[]{result};
        	}
        }
       	return null;
        



    }

    private double dif(Sample s, double[] vals) 
    {
        double result = 0;
        double[] svals = s.toDoubleArray();
        double[] dvals = new double[svals.length];
        for (int i = 0; i < vals.length; i++)
            if (max[i] - min[i]<=DOUBLE_PRECISION)
                dvals[i] = 0;
            else
                dvals[i] = (svals[i] - min[i]) / (max[i] - min[i]);
        for (int i = 0; i < vals.length; i++)
            result += Math.abs(dvals[i] - vals[i]);
        result = Math.sqrt(result);
        result /= Math.sqrt(vals.length);
        return result;
    }

    @Override
    public Classifier copy() {
        // TODO Auto-generated method stub
        return null;
    }

 

}
