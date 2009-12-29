package apw.classifiers.c4_5;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import apw.classifiers.c4_5.complex.Complex;
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
	public static final int DIV_POINTS_COUNT = 200;
	protected Samples source;
	
	protected int total = 0;
	protected int correct = 0;
	
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
					if(samples.size()<=DIV_POINTS_COUNT)
						for (Sample sample : samples) 
						{
							Object o = sample.get(samples.getAtts().indexOf(numeric));
							if(o!=null)
							{
								double div = (Double)sample.get(samples.getAtts().indexOf(numeric));
								double entropy = Entropy.numericEntropy(samples, numeric, div);
								if(entropy>bestEntropy)
								{
									bestDivider = div;
									bestEntropy = entropy;
									bestAttribute = numeric;
								}
							}
							
						}
					else
					{
						Random r = new Random(0);
						int i = DIV_POINTS_COUNT;
						while(i-->0)
						{
							Sample sample = samples.get(r.nextInt(samples.size())); 
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


	public abstract boolean evaluate(Sample sample);

	public abstract boolean evaluateButIgnore(Sample sample);

	public abstract void prune();


	public void reevaluate() 
	{
		total = 0;
		correct = 0;
	}


	public abstract List<Complex> generateComplexList();

}
