package apw.classifiers.c4_5;

import apw.classifiers.id3.DecisionLeaf;
import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.util.Entropy;

import java.util.*;
public class C4_5NominalNode<T> extends C4_5DecisionNode<T> {

	protected C4_5DecisionNode<T>[] childNodes;
	protected Nominal ruleAttribute = null;

	public C4_5NominalNode(List<Sample> samples, List<Nominal> nominals, LinkedList<Numeric> numerics) 
	{	
		super(samples);
		if(samples.size()==0)
			throw new RuntimeException("samples.size()==0 ??? Can not be !!!");
				
		double bestEntropy = -1000.0;
		
		for (Nominal nominal : nominals) 
		{
			double entropy = Entropy.nominalEntropy(samples, nominal);
			if(entropy>bestEntropy)
			{
				bestEntropy = entropy;
				ruleAttribute = nominal;
			}
		}
		LinkedList<Nominal> attributes_new = new LinkedList<Nominal>();
		attributes_new.addAll(nominals);
		attributes_new.remove(ruleAttribute);
		
		int att_num = samples.get(0).getSamples().getAtts().indexOf(ruleAttribute);
		
		if(ruleAttribute==null)
		{
			childNodes = new C4_5DecisionNode[1];
			childNodes[0] = new C4_5DecisionLeaf<T>(samples);
			return;
		}

		LinkedList<Sample>[] samplesGroups = new LinkedList[ruleAttribute.getSortedIKeys().length];
		
		for (int i = 0; i < samplesGroups.length; i++) {
			samplesGroups[i] = new LinkedList<Sample>();
		}
		
		for (Sample s : samples) 
		{
			int index;
			if(s.get(att_num)==null)
				index = 0;
			else
				index = Arrays.binarySearch(ruleAttribute.getSortedIKeys(),s.get(att_num));
			samplesGroups[index].add(s);
		}
		LinkedList<Sample> biggestGroup = null;
		int maxSize = -1;
		
		for (LinkedList<Sample> linkedList : samplesGroups) {
			if(linkedList.size()>maxSize)
			{
				maxSize = linkedList.size();
				biggestGroup = linkedList;
			}
		}
		
		childNodes = new C4_5DecisionNode[samplesGroups.length];
		
		for (int i = 0; i < samplesGroups.length; i++) 
		{
			LinkedList<Sample> group = samplesGroups[i];
			
			if(group.size()==0)
			{
				childNodes[i] = new C4_5DecisionLeaf<T>(group,biggestGroup.get(0).classAttributeInt());
				
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
					childNodes[i] = new C4_5DecisionLeaf<T>(group,group.get(0).classAttributeInt());
					
				}
				else
				{
					if(attributes_new.size()==0)
					{
						childNodes[i] = new C4_5NumericNode<T>(group,numerics);
					}
					else
					{
						childNodes[i] = new C4_5NominalNode<T>(group,attributes_new,numerics);
					}
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
			String prefix =  "& "+ruleAttribute.getName()+"=="+ruleAttribute.getSortedIKeys()[i]+"("+source.size()+")";
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
		int att_num = x.getSamples().getAtts().indexOf(ruleAttribute);
		int index;
		if(x.get(att_num)!=null)
			index = Arrays.binarySearch(ruleAttribute.getSortedIKeys(),x.get(att_num));
		else 
			index=0;
		return childNodes[index].classify(x);
	}

}
