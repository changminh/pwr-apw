package apw.classifiers.c4_5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import apw.classifiers.Classifier;
import apw.classifiers.RuleClassifier;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;
import apw.core.Samples;
import apw.core.meta.ClassifierCapabilities;

/**
 * @author Krzysztof Praszmo
 */
@ClassifierCapabilities(multiClass=true, handlesNumericAtts=true, handlesNominalAtts=true)
public class C4_5 extends RuleClassifier {

	private C4_5DecisionNode<Object> tree;
	
	
	public C4_5(Samples s) 
	{	
		super(s);
		LinkedList<Nominal> nominals = new LinkedList<Nominal>();
		LinkedList<Numeric> numerics = new LinkedList<Numeric>();
		for (Attribute attribute : s.getAtts()) {
			if (attribute instanceof Nominal) {
				Nominal nom = (Nominal) attribute;
				if(nom!=s.getClassAttribute())
					nominals.add(nom);
			}
			else
			{
				Numeric num = (Numeric) attribute;
				numerics.add(num);
			}
		}
		
			
		if(nominals.size()==0)
		{
			tree = new C4_5NumericNode<Object>(s,numerics);			
		}
		else
		{
			tree = new C4_5NominalNode<Object>(s,nominals,numerics);
		}

		for (String str : getRules()) {
			System.out.println(str);
		}
	}

	@Override
	public String[] getRules() {
		LinkedList<String> list = tree.getRules();
		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = "if "+list.get(i).substring(2);
		}
		return result;
	}

	@Override
	public void addSample(Sample s) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();	
	}

	@Override
	public void addSamples(Samples s) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}


	@Override
	public double[] classifySample(Sample s) 
	{	
		Object sampleClass = tree.classify(s);
		Attribute att = s.getSamples().getClassAttribute();
		if (att instanceof Nominal) {
			Nominal nominal = (Nominal) att;
			double[] result = new double[nominal.getSortedIKeys().length];
			result[Arrays.binarySearch(nominal.getSortedIKeys(),sampleClass)] = 1.0;
			return result;
		}
		throw new IllegalArgumentException("Sample class must be Nominal");
	}

	@Override
	public Classifier copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rebuild() {
		// TODO Auto-generated method stub

	}

}
