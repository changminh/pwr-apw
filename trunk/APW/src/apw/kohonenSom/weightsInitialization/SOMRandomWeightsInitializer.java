package apw.kohonenSom.weightsInitialization;

import apw.kohonenSom.patterns.SOMSamplesLoader;
import java.util.Random;

public class SOMRandomWeightsInitializer implements SOMWeightsInitializer {
	private Random rand;
	
	public SOMRandomWeightsInitializer(){
		rand = new Random();
	}
	
	@Override
	public double[][][] initializeWeights(
            SOMSamplesLoader samples, int width, int height) {
		double[] minValues = samples.getMinNumValues();
        double[] maxValues = samples.getMaxNumValues();

        double[][][] weights = new double[width][][];
        int inputsNumber = samples.getNumericAttrNumber();
		
		for(int ix=0; ix<width; ix++)
		{
			weights[ix] = new double[height][];
			
			for(int iy=0; iy<height; iy++)
			{
				weights[ix][iy] = new double[inputsNumber];
				
				for(int iw=0; iw<inputsNumber; iw++){
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
