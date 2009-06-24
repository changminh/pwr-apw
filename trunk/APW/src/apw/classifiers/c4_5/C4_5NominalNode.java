package apw.classifiers.c4_5;

import apw.classifiers.id3.DecisionLeaf;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.util.Entropy;

import java.util.*;
public class C4_5NominalNode extends C4_5DecisionNode {

	protected C4_5DecisionNode[] childNodes;
	protected Nominal ruleAttribute = null;

	public C4_5NominalNode(Samples samples, Nominal attribute) 
	{	
		super(samples);

		ruleAttribute = attribute;

		if(samples.size()==0)
			throw new RuntimeException("samples.size()==0 ??? Can not be !!!");
				

		
		int att_num = samples.get(0).getSamples().getAtts().indexOf(ruleAttribute);
		
		//TODO remove (only for emergency)
		if(ruleAttribute==null)
		{
			System.err.println("null attribute ERROR");
			childNodes = new C4_5DecisionNode[1];
			childNodes[0] = new C4_5DecisionLeaf(samples);
			return;
		}

		Samples[] samplesGroups = new Samples[ruleAttribute.getSortedIKeys().length];
		
		for (int i = 0; i < samplesGroups.length; i++) {
			samplesGroups[i] = samples.copyStructure();
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
			String prefix =  "& "+ruleAttribute.getName()+"=="+ruleAttribute.getSortedIKeys()[i]+"("+childNodes[i].source.size()+")";
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
