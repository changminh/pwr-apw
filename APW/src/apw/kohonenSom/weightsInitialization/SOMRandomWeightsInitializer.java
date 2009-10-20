package apw.kohonenSom.weightsInitialization;

import apw.kohonenSom.patterns.SOMSamplesLoader;
import java.util.Random;

public class SOMRandomWeightsInitializer implements SOMWeightsInitializer {

	private int x;
	private int y;
	private int inpNum;
	
	private Random rand;
	
	public SOMRandomWeightsInitializer(){
		rand = new Random();
	}
	
	@Override
	public double[][][] initializeWeights(
            SOMSamplesLoader samples, int width, int height) {
		double[][][] weights = new double[width][][];
        double[] minValues = samples.getMinNumValues();
        double[] maxValues = samples.getMaxNumValues();
		
		for(int ix=0; ix<width; ix++)
		{
			weights[ix] = new double[height][];
			
			for(int iy=0; iy<height; iy++)
			{
				weights[ix][iy] = new double[inpNum];
				
				for(int iw=0; iw<inpNum; iw++){
                    double wMax = maxValues[iw];
                    double wMin = minValues[iw];
                    double w = 
                            rand.nextDouble()*(wMax - wMin) + wMin;
                    weights[ix][iy][iw] = w;
                }
			}
		}
		
		return weights;
	}

}
