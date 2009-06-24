package apw.classifiers.c4_5;

import java.util.LinkedList;
import java.util.List;

import apw.classifiers.id3.DecisionLeaf;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.util.Entropy;

/**
 * @author Krzysztof Praszmo
 */

public abstract class C4_5DecisionNode
{
	protected List<Sample> source;
	
	public C4_5DecisionNode(Samples samples)
	{
		source = samples;
	}
	//creates new DecisionNode - Numeric or Nominal
	
	
	public static C4_5DecisionNode createNode(Samples samples)
	{
		double bestEntropy = -1000.0;
		double bestDivider = 0.0;
		Attribute bestAttribute = null;
		
		List<Attribute> attributes = samples.getAtts();
	
		
		for(Attribute a:attributes)
			if(!a.equals(samples.getClassAttribute()))
			{
				if (a instanceof Nominal) {
					Nominal nominal = (Nominal) a;
					double entropy = Entropy.nominalEntropy(samples, nominal);
					if(entropy>bestEntropy)
					{
						bestEntropy = entropy;
						bestAttribute = nominal;
					}	
				}
				else if (a instanceof Numeric) {
					Numeric numeric = (Numeric) a;
					for (Sample sample : samples) {
	
						double div = (Double)sample.get(sample.getSamples().getAtts().indexOf(numeric));
						double entropy = Entropy.numericEntropy(samples, numeric, div);
						if(entropy>bestEntropy)
						{
							bestDivider = div;
							bestEntropy = entropy;
							bestAttribute = numeric;
						}
						
					}
					
				}
			}
		if(bestAttribute==null)
		{
			return new C4_5DecisionLeaf(samples);
		}
		
		if (bestAttribute instanceof Nominal) {
			Nominal nominal = (Nominal) bestAttribute;
			return new C4_5NominalNode(samples,nominal);
		}
		else if (bestAttribute instanceof Numeric) {
			Numeric numeric = (Numeric) bestAttribute;
			return new C4_5NumericNode(samples,numeric,bestDivider);			
		}
		
		throw new IllegalArgumentException("Unknown attribute type");
		
	}

	public abstract Object classify(Sample x);
	
	public abstract LinkedList<String> getRules();
}
