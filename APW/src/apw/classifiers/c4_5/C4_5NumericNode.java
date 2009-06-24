package apw.classifiers.c4_5;

import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.util.Entropy;

import java.util.*;
public class C4_5NumericNode extends C4_5DecisionNode {

	protected C4_5DecisionNode childNode1;
	protected C4_5DecisionNode childNode2;
	protected double divider;
	protected Numeric ruleAttribute = null;

	public C4_5NumericNode(Samples samples, Numeric attribute, double divider) 
	{	
		super(samples);
	
		ruleAttribute = attribute;
		this.divider = divider;
		
		if(samples.size()==0)
			throw new RuntimeException("samples.size()==0 ??? Can not be !!!");
				
		
		int att_num = samples.getAtts().indexOf(ruleAttribute);
		
		
		Samples samplesGroup1 = samples.copyStructure();
		Samples samplesGroup2 = samples.copyStructure();
		
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
			childNode1 = childNode2 = createNode(samplesGroup2);				
		}
		else if(samplesGroup2.size()==0)
		{
			childNode1 = childNode2 = createNode(samplesGroup1);				
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
					childNode1 = new C4_5DecisionLeaf(samplesGroup1,samplesGroup1.get(0).classAttributeInt());	
				}
				else
				{
					childNode1 = createNode(samplesGroup1);
					
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
					childNode2 = new C4_5DecisionLeaf(samplesGroup2,samplesGroup2.get(0).classAttributeInt());	
				}
				else
				{
					childNode2 = createNode(samplesGroup2);
					
				}
			}
		}
	}

	@Override
	public LinkedList<String> getRules() 
	{
		LinkedList<String> result = new LinkedList<String>();

		String prefix1 =  "& "+ruleAttribute.getName()+"< "+divider+"("+childNode1.source.size()+")";
			List<String> li1 = childNode1.getRules();
			for (String string : li1) {
				result.add(prefix1+" "+string);
			}
		String prefix2 =  "& "+ruleAttribute.getName()+">="+divider+"("+childNode2.source.size()+")";
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
