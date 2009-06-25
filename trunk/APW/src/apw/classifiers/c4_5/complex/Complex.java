package apw.classifiers.c4_5.complex;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import apw.core.Sample;
import apw.core.Samples;

public class Complex 
{
	private int total = 0;
	private int correct = 0;
	private int covered = 0;
	private int notCovered = 0;

	private Selector[] selectors;
	
	private Object classObject;
	
	public void clear()
	{
		total = correct = covered = notCovered = 0;
	}
	
	public void evaluate(Samples s)
	{
		for (Sample sample : s) {
			total++;
			if(check(sample))
			{
				covered++;
				if(sample.get(s.getClassAttributeIndex()).equals(classObject))
					correct++;
			}
			else
			{
				if(sample.get(s.getClassAttributeIndex()).equals(classObject))
					notCovered++;
			}
		}
	}
	
	public double getAccuracy()
	{
		return (double)correct/(double)covered;
	}
	
	public double getCoverage()
	{
		return (double)covered/(double)total;
	}
	
	public Complex(int size,Object classObject)
	{
		selectors = new Selector[size];
		this.classObject = classObject;
	}
	
	public Object getClassObject() {
		return classObject;
	}
	
	public boolean check(Sample s)
	{
		for (int i = 0; i < selectors.length; i++) 
		{
			
			if(selectors[i]!=null)
			{
				boolean result = selectors[i].check(s.get(i));
//				System.out.println(result);
				if(!result)return false;
			}
//			else
//			{
//				System.out.println("NULL OK");
//			}
		}
		return true;
	}
	
	public Selector getSelector(int index) 
	{
		return selectors[index];
	}
	
	public void setSelector(Selector selector,int index) {
		this.selectors[index] = selector;
	}
	
	public void trim(Samples s)
	{
		clear();
		for (int i = 0; i < selectors.length; i++) 
			if(selectors[i]!=null)
			{
				clear();
				evaluate(s);
				double acc1 = getAccuracy();
				Selector temp = selectors[i];
				selectors[i] = null;
				clear();
				evaluate(s);
				double acc2 = getAccuracy();
				if(acc1>acc2)
				{
					selectors[i]=temp;
				}
					
			}
	}
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		for (Selector s : selectors) 
		{
			if(s==null)
				result.append("?, ");
			else
				result.append(s.toString()+", ");
		}
		return "[ "+result.toString()+" class="+classObject.toString()+" ] COV=" +getCoverage()+" ACC="+getAccuracy() ;
	}
}
