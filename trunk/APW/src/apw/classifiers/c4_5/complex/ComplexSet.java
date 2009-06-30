package apw.classifiers.c4_5.complex;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import apw.classifiers.Classifier;
import apw.classifiers.RuleClassifier;
import apw.classifiers.c4_5.C4_5;
import apw.core.Attribute;
import apw.core.Nominal;
import apw.core.Sample;
import apw.core.Samples;

public class ComplexSet extends RuleClassifier {

	private List<Complex> complexes;
	
	public List<Complex> getComplexes() {
		return complexes;
	}
	
	public ComplexSet(Samples s) 
	{
		super(s);
		C4_5 c45 = new C4_5(s);
		this.complexes = c45.generateComplexList();
		trim(s);
		System.out.println("REMOVED RULES:"+removeCoveredRules(s)+" RULES LEFT:"+complexes.size());
	}

	
	public ComplexSet(List<Complex> complexes) 
	{
		super(null);
		this.complexes = complexes;
	}

	@Override
	public String[] getRules() 
	{
		String[] result = new String[complexes.size()];
		for (int i = 0; i < result.length; i++) {
			result[i]=complexes.get(i).toString();
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
		String[] keys = ((Nominal)s.getSamples().getClassAttribute()).getSortedIKeys();			
		double[] result = new double[keys.length];
//		boolean b = false;
		for (Complex complex : complexes) 
		{
//			System.out.println("COMPLEX");
			if(complex.check(s))
				{
//					b = true;
					result[Arrays.binarySearch(keys, complex.getClassObject())]+=1.0;
				}
		}
	
//		if(!b)
//			{
//				System.out.println("----------------------------------------------------NO_RULE");
//				System.out.println(s);
//			}
		return result;
	}

	public int removeCoveredRules(Samples s)
	{
		Samples temp = s.copyStructure();
		temp.addAll(s);
		int size = complexes.size();
        for (int i = 0; i < complexes.size(); i++) {
			Complex cplx = complexes.get(i);
			int count = 0;
			for (int  j = 0;  j < temp.size(); j++) {
				Sample sample = temp.get(j);
            	if(cplx.check(sample))
            	{
            		count++;
            		j--;
            		temp.remove(sample);
            	}
			}
        	if(count==0)
        	{
        		complexes.remove(cplx);
        		i--;
        	}
        }
        return size - complexes.size();
	}
	
	@Override
	public Classifier copy() {
		return null;
	}

	@Override
	public void rebuild() {}

	public void trim(Samples s) 
	{
        for (int i = 0; i < complexes.size(); i++) {
			Complex cplx = complexes.get(i);
			cplx.clear();
        	cplx.evaluate(s);
        	if(cplx.getCoverage()<0.00000001)
        	{
        		complexes.remove(cplx);
        		i--;
        	}
			cplx.trim(s);
			cplx.clear();
        	cplx.evaluate(s);
		}
        
        Collections.sort(complexes, new Comparator<Complex>(){

			@Override
			public int compare(Complex o1, Complex o2) {
				return (int)(10000*o2.getAccuracy()-10000*o1.getAccuracy()+1000*o2.getCoverage()-1000*o1.getCoverage());
			}});
        		
	}

}
