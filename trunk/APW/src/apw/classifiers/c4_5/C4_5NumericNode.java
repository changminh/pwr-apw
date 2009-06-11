package apw.classifiers.c4_5;

import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.util.Entropy;

import java.util.*;
public class C4_5NumericNode<T> extends C4_5DecisionNode<T> {

	protected C4_5DecisionNode<T> childNode1;
	protected C4_5DecisionNode<T> childNode2;
	protected double divider;
	protected Numeric ruleAttribute = null;

	public C4_5NumericNode(List<Sample> samples, LinkedList<Numeric> numerics) 
	{	
		super(samples);
		if(samples.size()==0)
			throw new RuntimeException("samples.size()==0 ??? Can not be !!!");
				
		double bestEntropy = 1000.0;
		
		for (Numeric numeric : numerics) 
		{
			for (Sample sample : samples) {

				double div = (Double)sample.get(sample.getSamples().getAtts().indexOf(numeric));
				double entropy = Entropy.numericEntropy(samples, numeric, div);
				if(entropy<bestEntropy)
				{
					divider = div;
					bestEntropy = entropy;
					ruleAttribute = numeric;
				}
				
			}
		}
//		System.out.println(bestEntropy);
		
//		LinkedList<Numeri> attributes_new = new LinkedList<Nominal>();
//		attributes_new.addAll(nominals);
//		attributes_new.remove(ruleAttribute);
		
		int att_num = samples.get(0).getSamples().getAtts().indexOf(ruleAttribute);
		

		
		LinkedList<Sample> samplesGroup1 = new LinkedList<Sample>();
		LinkedList<Sample> samplesGroup2 = new LinkedList<Sample>();
		
		for (Sample s : samples) 
		{
			double temp = (Double)s.get(att_num);
			if(temp<divider)
				samplesGroup1.add(s);
			else
				samplesGroup2.add(s);
		}

		
		
		

		if(samplesGroup1.size()==0)
		{
			childNode1 = childNode2 = new C4_5NumericNode<T>(samplesGroup2,numerics);				
		}
		else if(samplesGroup2.size()==0)
		{
			childNode1 = childNode2 = new C4_5NumericNode<T>(samplesGroup1,numerics);				
		} 
		else
		{
			if(true)//group 1
			{
				Object sampleClass = samplesGroup1.get(0).classAttributeInt();
				boolean sameClass = true;
				for (Sample sample : samplesGroup1) 
				{
					if(!sample.classAttributeInt().equals(sampleClass))
					{
						sameClass = false;
						break;
					}
				}
				if(sameClass)
				{
					childNode1 = new C4_5DecisionLeaf<T>(samplesGroup1,samplesGroup1.get(0).classAttributeInt());	
				}
				else
				{
					childNode1 = new C4_5NumericNode<T>(samplesGroup1,numerics);
					
				}
			}
			if(true)//group 2
			{
				Object sampleClass = samplesGroup2.get(0).classAttributeInt();
				boolean sameClass = true;
				for (Sample sample : samplesGroup2) 
				{
					if(!sample.classAttributeInt().equals(sampleClass))
					{
						sameClass = false;
						break;
					}
				}
				if(sameClass)
				{
					childNode2 = new C4_5DecisionLeaf<T>(samplesGroup2,samplesGroup2.get(0).classAttributeInt());	
				}
				else
				{
					childNode2 = new C4_5NumericNode<T>(samplesGroup2,numerics);
					
				}
			}
		}
	}

	@Override
	public LinkedList<String> getRules() 
	{
		LinkedList<String> result = new LinkedList<String>();

		String prefix1 =  "& "+ruleAttribute.getName()+"< "+divider;
			List<String> li1 = childNode1.getRules();
			for (String string : li1) {
				result.add(prefix1+" "+string);
			}
		String prefix2 =  "& "+ruleAttribute.getName()+">="+divider;
			List<String> li2 = childNode2.getRules();
			for (String string : li2) {
				result.add(prefix2+" "+string);
			}
		return result;
	}
	
	@Override
	public Object classify(Sample x) 
	{
		int att_num = x.getSamples().getAtts().indexOf(ruleAttribute);
		double temp = (Double)x.get(att_num);
		if(temp<divider)
			return childNode1.classify(x);
		else 
			return childNode2.classify(x);
	}

}
