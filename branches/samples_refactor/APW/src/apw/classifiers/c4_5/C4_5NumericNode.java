package apw.classifiers.c4_5;

import apw.classifiers.c4_5.complex.Complex;
import apw.classifiers.c4_5.complex.NominalSelector;
import apw.classifiers.c4_5.complex.NumericSelector;
import apw.classifiers.c4_5.complex.Selector;
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
	protected int att_num;
	
	public C4_5NumericNode(Samples samples, Numeric attribute, double divider) 
	{	
		super(samples);
	
		ruleAttribute = attribute;
		this.divider = divider;
		
		if(samples.size()==0)
			throw new RuntimeException("samples.size()==0 ??? Can not be !!!");
				
		
		att_num = samples.getAtts().indexOf(ruleAttribute);
		
		
		Samples samplesGroup1 = samples.copyStructure();
		Samples samplesGroup2 = samples.copyStructure();
		
		for (Sample s : samples) 
		{
			if(s.get(att_num)!=null)
			{
				double temp = (Double)s.get(att_num);
				if(temp<divider)
					samplesGroup1.add(s);
				else
					samplesGroup2.add(s);
			}
			else
			{
				samplesGroup1.add(s);
				samplesGroup2.add(s);
			}
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

		String prefix1 =  "& "+ruleAttribute.getName()+"< "+divider+"(COV="+childNode1.source.size()+" sample(s))(ACC="+(childNode1.total==0?"?":(100*childNode1.correct/childNode1.total))+"%)";
			List<String> li1 = childNode1.getRules();
			for (String string : li1) {
				result.add(prefix1+" "+string);
			}
		String prefix2 =  "& "+ruleAttribute.getName()+">="+divider+"(COV"+childNode2.source.size()+" sample(s))(ACC="+(childNode2.total==0?"?":(100*childNode2.correct/childNode2.total))+"%)";
			List<String> li2 = childNode2.getRules();
			for (String string : li2) {
				result.add(prefix2+" "+string);
			}
		return result;
	}
	
	@Override
	public Object classify(Sample x) 
	{
		if(x.get(att_num)==null)
				return childNode1.classify(x);
		double temp = (Double)x.get(att_num);
		if(temp<divider)
			return childNode1.classify(x);
		else 
			return childNode2.classify(x);
	}

	public boolean evaluate(Sample sample) 
	{
		double temp;
		if(sample.get(att_num)==null)
			temp = divider-1;
		else
			temp = (Double)sample.get(att_num);
		boolean result;
		if(temp<divider)
		{
			result = childNode1.evaluate(sample);
			if(!result)result = childNode2.evaluate(sample);
		}
		else 
		{
			result = childNode2.evaluate(sample);
			if(!result)result = childNode1.evaluate(sample);		
		}
		total++;
		if(result)
			correct++;
		return result;
	}

	@Override
	public boolean evaluateButIgnore(Sample sample) {
		double temp = (Double)sample.get(att_num);
		boolean result;
		if(temp<divider)
		{
			result = childNode1.evaluate(sample);
		}
		else 
		{
			result = childNode2.evaluate(sample);
		}
		return result;
	}
	
	@Override
	public void prune() {
		if(childNode1.correct*2<childNode1.total)
		{
			childNode1 = new C4_5DecisionLeaf(childNode1.source);
		}
		else
		{
			childNode1.prune();
		}
		if(childNode2.correct*2<childNode2.total)
		{
			childNode2 = new C4_5DecisionLeaf(childNode2.source);
		}
		else
		{
			childNode2.prune();
		}
		
	}

	@Override
	public List<Complex> generateComplexList() 
	{
		LinkedList<Complex> result = new LinkedList<Complex>();

		List<Complex> li1 = childNode1.generateComplexList();
		for (Complex complex : li1) {
			Selector s = complex.getSelector(att_num);
			NumericSelector ns = null;
			if(s==null)
			{
				ns = new NumericSelector();
			}
			else if (s instanceof NumericSelector) {
				ns = (NumericSelector) s;
			}
			ns.cutUp(divider);
			complex.setSelector(ns, att_num);
			result.add(complex);

		}
		List<Complex> li2 = childNode2.generateComplexList();
		for (Complex complex : li2) {
			Selector s = complex.getSelector(att_num);
			NumericSelector ns = null;
			if(s==null)
			{
				ns = new NumericSelector();
			}
			else if (s instanceof NumericSelector) {
				ns = (NumericSelector) s;
			}
			ns.cutDown(divider);
			complex.setSelector(ns, att_num);
			result.add(complex);
		}
		return result;	
		}
}
