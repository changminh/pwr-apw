package apw.kohonenSom.weightsInitialization;

import java.util.ArrayList;
import java.util.Random;

import apw.kohonenSom.util.SOMOrderRandomizer;

public class SOMPatternsBasedWeightsInit implements SOMWeightsInitializer {

	private int x;
	private int y;
	private int n;
	private ArrayList<double[]> patterns;	
	private boolean randPatterns;
	
	private SOMOrderRandomizer ordRand;
	private Random rand;
	
	public SOMPatternsBasedWeightsInit(int x, int y, 
			ArrayList<double[]> patterns, boolean randPatterns)
	{
		this.x = x;
		this.y = y;
		this.n= x*y;
		this.patterns = patterns;
		this.randPatterns = randPatterns;
		
		rand = new Random();
		ordRand = new SOMOrderRandomizer();
	}
	
	@Override
	public double[][][] initializeWeights() {
		double[][][] w;
		
		ArrayList<Integer> order = genOrder();
		
		w = new double[x][][];
		
		int l = 0;
		for(int i=0; i<x; i++)
		{
			w[i] = new double[y][];
			
			for(int j=0; j<y; j++, l++)
				w[i][j] = patterns.get(order.get(l));
		}
		
		return w;
	}

	private ArrayList<Integer> genOrder() {
		ArrayList<Integer> result;
		
		int l = java.lang.Math.min(patterns.size(), n);
		
		if(randPatterns)
			result = ordRand.randomizeOrder(l);
		else{
			result = new ArrayList<Integer>();
			for(int i=0; i<l; i++)
				result.add(i);
		}
		
		if(l<n){
			int diff = n - l;
			double perN = (double)n/(double)diff;
			
			ArrayList<Integer> temp = new ArrayList<Integer>();
			
			double k=0;
			
			for(int i=0; i<result.size(); i++)
			{
				temp.add(result.get(i));
				k += perN;
				
				while(k>0)
				{
					k = java.lang.Math.max(0, k-1);
					temp.add(result.get(i));
				}			
			}
			
			result = temp;
		}
		
		return result;
	}

}
