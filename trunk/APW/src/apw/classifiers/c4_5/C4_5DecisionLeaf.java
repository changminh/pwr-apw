package apw.classifiers.c4_5;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import apw.classifiers.c4_5.complex.Complex;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;

/**
 * @author Krzysztof Praszmo
 */

public class C4_5DecisionLeaf extends C4_5DecisionNode {

	private Object nodeClass;
	
	public C4_5DecisionLeaf(Samples samples)
	{
		super(samples);
		Attribute a = samples.getClassAttribute();
		if (a instanceof Nominal) {
			int class_num = samples.getAtts().indexOf(a);
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
	
	public C4_5DecisionLeaf(Samples samples,Object nodeClass) 
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
		result.add("then "+ nodeClass+"(COV="+source.size()+" sample(s))(ACC="+(total==0?"?":(100*correct/total))+"%)");
		return result;
	}

	@Override
	public boolean evaluate(Sample sample) 
	{
		boolean result = nodeClass.equals(sample.get(source.getAtts().indexOf(source.getClassAttribute())));
		
		total++;
		if(result)
		{
			correct++;
		}
		return result;
			
	}
	
	public boolean evaluateButIgnore(Sample sample) 
	{
		boolean result = nodeClass.equals(sample.get(source.getAtts().indexOf(source.getClassAttribute())));
		return result;
			
	}
	
	@Override
	public void prune() {}

	@Override
	public List<Complex> generateComplexList() 
	{
		LinkedList<Complex> result = new LinkedList<Complex>();
		result.add(new Complex(source.getAtts().size(), nodeClass));
		return result;
	}
	

}
