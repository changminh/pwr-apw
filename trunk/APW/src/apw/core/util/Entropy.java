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
		
		String[] keys_class = classAttribute.getSortedIKeys();
		
		String[] keys = attribute.getSortedIKeys();
		
		double[]sums = new double[keys.length];
		double[][] counts = new double[keys.length][keys_class.length];

		if(counts.length<2)
			return -222220.0;
		

		for (Sample s : samples) 
		{
			if(s.get(att_num)!=null)
			{
				int t_att = Arrays.binarySearch(keys,s.get(att_num)); 
				int t_class = Arrays.binarySearch(keys_class,s.get(class_num));
				
				counts[t_att][t_class]++;
				sums[t_att]++;
			}
		}
		int not_zero = 0;
		double total_sum = (double)samples.size();
		for (int i = 0; i < counts.length; i++) {
			double sum = sums[i];
				if(sum>0)
				{
					not_zero++;
					entropy += sum/total_sum*Math.log(total_sum/sum)/Math.log(2);				
					//System.out.println(sum/total_sum*Math.log(total_sum/sum)/Math.log(2));
					for (int j = 0; j < counts[i].length; j++) {
						double d = counts[i][j];
							if(d>0.0)
							{
								entropy -= d/total_sum*(Math.log(sum/d)/Math.log(2));
								//System.out.println(d/total_sum*(Math.log(sum/d)/Math.log(2)));
							}
					}
				}			
		}
		if(not_zero<2)
		{
			return -10000;
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
		//for (int i = 0; i < counts1.length; i++) {
			//counts1[i]/=sum1;
			//counts2[i]/=sum2;
		//}

		
		for (double d : counts1) 
		{	
			if(d>0.0)
				entropy -= (d/samples.size())*(Math.log(sum1/d)/Math.log(2.0));
		}
		for (double d : counts2) 
		{	
			if(d>0.0)
				entropy -= (d/samples.size())*(Math.log(sum2/d)/Math.log(2.0));
		}
		

		entropy += (sum1/samples.size())*(Math.log(samples.size()/sum1)/Math.log(2.0));
		entropy += (sum2/samples.size())*(Math.log(samples.size()/sum2)/Math.log(2.0));

		
		return entropy;
	}
}
