package apw.classifiers.id3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import apw.classifiers.Classifier;
import apw.classifiers.RuleClassifier;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;

/**
 * @author Krzysztof Praszmo
 */

public class ID3 extends RuleClassifier {

	private ID3DecisionNode<Object> tree;
	
	public static double nominalEntropy(List<Sample> samples,Nominal attribute)
	{
		double entropy = 0.0;
		if(samples.size()==0)return 0.0;
		
		//System.out.println(attribute);
		int att_num = samples.get(0).getSamples().getAtts().indexOf(attribute);

		String[] keys = attribute.getSortedIKeys();
		double[] counts = new double[keys.length];
		if(counts.length<2)
			return 0.0;
		
		double sum = 0.0;
		for (Sample s : samples) 
		{
			if(s.get(att_num)!=null)
			{
				counts[Arrays.binarySearch(keys,s.get(att_num))]++;
				sum++;
			}
		}
		for (int i = 0; i < counts.length; i++) {
			counts[i]/=sum;
		}

		for (double d : counts) 
		{	
			if(d>0.0)
				entropy += d*(Math.log(1/d)/Math.log((double)counts.length));
		}
		
		return entropy;
	}
	
	public ID3(Samples s) 
	{	
		super(s);
		ArrayList<Nominal> attributes = new ArrayList<Nominal>(s.getAtts().size());
		for (Attribute attribute : s.getAtts()) {
			if (attribute instanceof Nominal) 
			{
				Nominal n = (Nominal) attribute;
				if(n!=s.getClassAttribute())
					attributes.add(n);
			}
		}
		
		tree = new ID3DecisionNode<Object>(s,attributes);

		for (String str : getRules()) {
			System.out.println(str);
		}
	}

	@Override
	public String[] getRules() {
		LinkedList<String> list = tree.getRules();
		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = "if "+list.get(i).substring(2);
		}
		return result;
	}

	@Override
	public void addSample(Sample s) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();	
	}

	@Override
	public void addSamples(Samples s) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}


	@Override
	public double[] classifySample(Sample s) 
	{
		
		Object sampleClass = tree.classify(s);
		Attribute att = s.getSamples().getClassAttribute();
		if (att instanceof Nominal) {
			Nominal nominal = (Nominal) att;
			double[] result = new double[nominal.getSortedIKeys().length];
			result[Arrays.binarySearch(nominal.getSortedIKeys(),sampleClass)] = 1.0;
			return result;
		}
		throw new IllegalArgumentException("Sample class must be Nominal");
	}

	@Override
	public Classifier copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rebuild() {
		// TODO Auto-generated method stub

	}

}
