package apw.classifiers.c4_5;

import apw.classifiers.c4_5.complex.Complex;
import apw.classifiers.c4_5.complex.NominalSelector;
import apw.classifiers.c4_5.complex.Selector;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;

import java.util.*;

public class C4_5NominalNode extends C4_5DecisionNode {

	protected C4_5DecisionNode[] childNodes;
	protected Nominal ruleAttribute = null;
	protected int att_num;
	
	public C4_5NominalNode(Samples samples, Nominal attribute) 
	{	
		super(samples);

		ruleAttribute = attribute;

		if(samples.size()==0)
			throw new RuntimeException("samples.size()==0 ??? Can not be !!!");
				
		att_num = samples.getAtts().indexOf(ruleAttribute);
		
		Samples[] samplesGroups = new Samples[ruleAttribute.getSortedIKeys().length];
		
		for (int i = 0; i < samplesGroups.length; i++) {
			samplesGroups[i] = samples.copyStructure();
		}
		
		for (Sample s : samples) 
		{
			int index;
			if(s.get(att_num)==null)
			{
				for (Samples sg : samplesGroups) {
					sg.add(s);
				}
			}
			else
			{
				index = Arrays.binarySearch(ruleAttribute.getSortedIKeys(),s.get(att_num));
				samplesGroups[index].add(s);
			}
		}
		
		Samples biggestGroup = null;
		int maxSize = -1;
		
		for (Samples linkedList : samplesGroups) {
			if(linkedList.size()>maxSize)
			{
				maxSize = linkedList.size();
				biggestGroup = linkedList;
			}
		}
		
		childNodes = new C4_5DecisionNode[samplesGroups.length];
		
		for (int i = 0; i < samplesGroups.length; i++) 
		{
			Samples group = samplesGroups[i];
			
			if(group.size()==0)
			{
				childNodes[i] = new C4_5DecisionLeaf(group,biggestGroup.get(0).classAttributeInt());
			}
			else
			{
				Object sampleClass = group.get(0).classAttributeInt();
				boolean sameClass = true;
				for (Sample sample : group) {
					if(!sample.classAttributeInt().equals(sampleClass))
					{
						sameClass = false;
						break;
					}
				}
				if(sameClass)
				{
					childNodes[i] = new C4_5DecisionLeaf(group,group.get(0).classAttributeInt());
					
				}
				else
				{
					childNodes[i] = createNode(group);
				}
			}
		}
		

	
	}

	@Override
	public LinkedList<String> getRules() 
	{
		if(childNodes.length==1)return childNodes[0].getRules();
		LinkedList<String> result = new LinkedList<String>();
		for (int i = 0; i < childNodes.length; i++) {
			String prefix =  "& "+ruleAttribute.getName()+"=="+ruleAttribute.getSortedIKeys()[i]+"(COV="+childNodes[i].source.size()+" sample(s))(ACC="+(childNodes[i].total==0?"?":(100*childNodes[i].correct/childNodes[i].total))+"%)";
			List<String> li = childNodes[i].getRules();
			for (String string : li) {
				result.add(prefix+" "+string);
			}
		}
		return result;
	}
	
	@Override
	public Object classify(Sample x) 
	{
		if(childNodes.length==1)return childNodes[0].classify(x);
		int index;
		if(x.get(att_num)!=null)
			index = Arrays.binarySearch(ruleAttribute.getSortedIKeys(),x.get(att_num));
		else 
			index=0;
		return childNodes[index].classify(x);
	}

	@Override
	public boolean evaluate(Sample sample) 
	{
		if(childNodes.length==1)
		{
			boolean result = childNodes[0].evaluate(sample);
			total++;
			if(result)
				correct++;
			return result;
		}
		
		int index;
		if(sample.get(att_num)!=null)
			index = Arrays.binarySearch(ruleAttribute.getSortedIKeys(),sample.get(att_num));
		else 
			index=0;
		
		boolean result = childNodes[index].evaluate(sample);		// TODO Auto-generated method stub
		if(!result)
		{
			for (C4_5DecisionNode node : childNodes) 
			{
				result = node.evaluate(sample);		// TODO Auto-generated method stub
				if(result)
					break;
			}
		}
		total++;
		if(result)
			correct++;
		return result;
	}

	@Override
	public boolean evaluateButIgnore(Sample sample) {
		if(childNodes.length==1)
		{
			boolean result = childNodes[0].evaluate(sample);
			return result;
		}
		
		int index;
		if(sample.get(att_num)!=null)
			index = Arrays.binarySearch(ruleAttribute.getSortedIKeys(),sample.get(att_num));
		else 
			index=0;
		
		boolean result = childNodes[index].evaluate(sample);		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public void prune() 
	{
		for (int i = 0; i < childNodes.length; i++) 
		{
			if(childNodes[i].correct*2<childNodes[i].total)
			{
				childNodes[i] = new C4_5DecisionLeaf(childNodes[i].source);
			}
			else
			{
				childNodes[i].prune();
			}
		}
		
	}

	@Override
	public List<Complex> generateComplexList() 
	{
		if(childNodes.length==1)return childNodes[0].generateComplexList();

		LinkedList<Complex> result = new LinkedList<Complex>();
		for (int i = 0; i < childNodes.length; i++) 
		{
			List<Complex> li = childNodes[i].generateComplexList();
			for (Complex complex : li) {
				Selector s = complex.getSelector(att_num);
					if(s!=null)
					{
						System.err.println("Selector error");
					}
				complex.setSelector(new NominalSelector(ruleAttribute.getSortedIKeys()[i]), att_num);
				result.add(complex);
			}
		}
		return result;	}
}
