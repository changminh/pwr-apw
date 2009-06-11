package apw.classifiers.c4_5;

import java.util.LinkedList;
import java.util.List;

import apw.core.Sample;

/**
 * @author Krzysztof Praszmo
 */

public abstract class C4_5DecisionNode<T> 
{
	protected List<Sample> source;
	
	public C4_5DecisionNode(List<Sample> samples)
	{
		source = samples;
	}
	
	public abstract Object classify(Sample x);
	
	public abstract LinkedList<String> getRules();
}
