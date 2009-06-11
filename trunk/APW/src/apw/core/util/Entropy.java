package apw.core.util;

import java.util.Arrays;
import java.util.List;

import apw.core.Nominal;
import apw.core.Numeric;
import apw.core.Sample;

public abstract class Entropy {

	public static double nominalEntropy(List<Sample> samples,Nominal attribute)
	{
		double entropy = 0.0;
		if(samples.size()==0)return 0.0;
		
		//System.out.println(attribute);
		int att_num = samples.get(0).getSamples().getAtts().indexOf(attribute);

		String[] keys = attribute.getSortedIKeys();
		double[] counts = new double[keys.length];
		if(counts.length<2)
			return 0.0;
		
		double sum = 0.0;
		for (Sample s : samples) 
		{
			if(s.get(att_num)!=null)
			{
				counts[Arrays.binarySearch(keys,s.get(att_num))]++;
				sum++;
			}
		}
		for (int i = 0; i < counts.length; i++) {
			counts[i]/=sum;
		}

		for (double d : counts) 
		{	
			if(d>0.0)
				entropy += d*(Math.log(1/d)/Math.log((double)counts.length));
		}
		
		return entropy;
	}
	
	public static double numericEntropy(List<Sample> samples,Numeric attribute,double divider)
	{
		double entropy = 0.0;
		if(samples.size()==0)return 0.0;
		
		int att_num = samples.get(0).getSamples().getAtts().indexOf(attribute);
		Nominal classAttribute;
		try
		{
		classAttribute = (Nominal)samples.get(0).getSamples().getClassAttribute();
		}
		catch(ClassCastException ex)
		{
			throw new IllegalArgumentException("Class attribute must be nominal");
		}
		int class_num = samples.get(0).getSamples().getAtts().indexOf(classAttribute);
		
		String[] keys = classAttribute.getSortedIKeys();
		
		double[] counts1 = new double[keys.length];
		double[] counts2 = new double[keys.length];
		
		double sum1 = 0.0;
		double sum2 = 0.0;
		for (Sample s : samples) 
		{
			Double d;
			if((d=(Double)s.get(att_num))!=null)
			{
				if(d<divider)
				{
					counts1[Arrays.binarySearch(keys,s.get(class_num))]++;
					sum1++;	
				}
				else
				{
					counts2[Arrays.binarySearch(keys,s.get(class_num))]++;
					sum2++;					
				}
			}
			else
			{
				throw new NullPointerException();
			}
		}
		for (int i = 0; i < counts1.length; i++) {
			counts1[i]/=sum1;
			counts2[i]/=sum2;
		}

		double entropy1 = 0.0;
		double entropy2 = 0.0;
		
		for (double d : counts1) 
		{	
			if(d>0.0)
				entropy1 += d*(Math.log(1/d)/Math.log((double)counts1.length));
		}
		for (double d : counts2) 
		{	
			if(d>0.0)
				entropy2 += d*(Math.log(1/d)/Math.log((double)counts2.length));
		}
		

		
		if(sum1<0.5)
			entropy1=Double.MAX_VALUE;
		else
			entropy1/=sum1;

		if(sum2<0.5)
			entropy2=Double.MAX_VALUE;
		else
			entropy2/=sum2;

		entropy += (sum1/samples.size())*(Math.log(samples.size()/sum1)/Math.log(2.0));
		entropy += (sum2/samples.size())*(Math.log(samples.size()/sum2)/Math.log(2.0));

		
		return entropy1+entropy2-entropy;
	}
}
