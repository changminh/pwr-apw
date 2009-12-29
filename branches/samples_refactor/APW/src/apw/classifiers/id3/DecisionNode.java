package apw.classifiers.id3;

import java.util.LinkedList;
import java.util.List;

import apw.core.Sample;

/**
 * @author Krzysztof Praszmo
 */

public abstract class DecisionNode<T> 
{
	protected List<Sample> source;
	
	public DecisionNode(List<Sample> samples)
	{
		source = samples;
	}
	
	public abstract Object classify(Sample x);
	
	public abstract LinkedList<String> getRules();
}
