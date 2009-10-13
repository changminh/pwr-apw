package apw.kohonenSom.weightsInitialization;

import java.util.Random;

public class SOMRandomWeightsInitializer implements SOMWeightsInitializer {

	private int x;
	private int y;
	private int inpNum;
	
	private double WMax;
	private double WMin;
	
	private Random rand;
	
	public SOMRandomWeightsInitializer(int x, int y, int inpNum, double WMax, double WMin)
	{
		this.x = x;
		this.y = y;
		this.inpNum = inpNum;
		this.WMax = WMax;
		this.WMin = WMin;
		
		rand = new Random();
	}
	
	@Override
	public double[][][] initializeWeights() {
		double[][][] w;
		
		w = new double[x][][];
		
		for(int i=0; i<x; i++)
		{
			w[i] = new double[y][];
			
			for(int j=0; j<y; j++)
			{
				w[i][j] = new double[inpNum];
				
				for(int k=0; k<inpNum; k++){
                    double number = rand.nextDouble()*(WMax - WMin) + WMin;
                    w[i][j][k] = number;
                }
			}
		}
		
		return w;
	}

}
