package apw.kohonenSom.util;

public class SOMNormalizer {
	
	public static double[] normalizeVector(double[] vector)
	{
		double[] nv = new double[vector.length];
		
		double sum = 0;
		
		for(int i=0; i<vector.length; i++)
			sum += vector[i]*vector[i];
		
		sum = java.lang.Math.sqrt(sum);
		
		for(int i=0; i< nv.length; i++)
			nv[i] = vector[i]/sum;
		
		return nv;
	}
}
