package apw.classifiers.id3;

import apw.core.Nominal;
import apw.core.Sample;
import java.util.*;
public class ID3DecisionNode<T> extends DecisionNode<T> {

	protected DecisionNode<T>[] childNodes;
	protected Nominal ruleAttribute = null;

	public ID3DecisionNode(List<Sample> samples, List<Nominal> attributes) 
	{	
		super(samples);

		if(samples.size()==0)
			throw new RuntimeException("samples.size()==0 ??? Can not be !!!");
				
		double bestEntropy = -1.0;
		
		for (Nominal nominal : attributes) 
		{
			double entropy = ID3.nominalEntropy(samples, nominal);
			if(entropy>bestEntropy)
			{
				bestEntropy = entropy;
				ruleAttribute = nominal;
			}
		}

		attributes.remove(ruleAttribute);
		
		int att_num = samples.get(0).getSamples().getAtts().indexOf(ruleAttribute);
		

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
		
		childNodes = new DecisionNode[samplesGroups.length];
		
		for (int i = 0; i < samplesGroups.length; i++) 
		{
			LinkedList<Sample> group = samplesGroups[i];
			
			if(group.size()==0)
			{
				childNodes[i] = new DecisionLeaf<T>(group,biggestGroup.get(0).classAttributeInt());
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
					childNodes[i] = new DecisionLeaf<T>(group,group.get(0).classAttributeInt());
					
				}
				else
				{
					if(attributes.size()==0)
					{
						childNodes[i] = new DecisionLeaf<T>(group);
					}
					else
					{
						childNodes[i] = new ID3DecisionNode<T>(group,attributes);
					}
				}
			}
		}
		

	
	}

	@Override
	public LinkedList<String> getRules() 
	{
		LinkedList<String> result = new LinkedList<String>();
		for (int i = 0; i < childNodes.length; i++) {
			String prefix =  "& "+ruleAttribute.getName()+"=="+ruleAttribute.getSortedIKeys()[i];
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
		int att_num = x.getSamples().getAtts().indexOf(ruleAttribute);
		int index;
		if(x.get(att_num)!=null)
			index = Arrays.binarySearch(ruleAttribute.getSortedIKeys(),x.get(att_num));
		else 
			index=0;
		return childNodes[index].classify(x);
	}

}
