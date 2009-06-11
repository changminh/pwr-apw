package apw.classifiers.id3;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;

/**
 * @author Krzysztof Praszmo
 */

public class DecisionLeaf<T> extends DecisionNode<T> {

	private Object nodeClass;
	
	public DecisionLeaf(List<Sample> samples)
	{
		super(samples);
		Attribute a = samples.get(0).getSamples().getClassAttribute();
		if (a instanceof Nominal) {
			int class_num = samples.get(0).getSamples().getAtts().indexOf(a);
			Nominal n  = (Nominal) a;
			String[] keys = n.getSortedIKeys();
			int[] counts = new int[keys.length];
			
			for (Sample s : samples) 
			{
				counts[Arrays.binarySearch(keys,s.get(class_num))]++;
			}

			int maxSize = -1;
			
			for (int i = 0; i < counts.length; i++) {
				if(counts[i]>maxSize)
				{
					maxSize = counts[i];
					nodeClass = keys[i];
				}
	
			}
		}
		else
			throw new IllegalArgumentException("Samples class must be Nominal");
	}
	
	public DecisionLeaf(List<Sample> samples,Object nodeClass) 
	{
		super(samples);
		this.nodeClass = nodeClass;
		
	}

	@Override
	public Object classify(Sample x) {
		return nodeClass;
	}
	
	@Override
	public LinkedList<String> getRules() 
	{
		LinkedList<String> result = new LinkedList<String>();
		result.add("then "+ nodeClass);
		return result;
	}

}
