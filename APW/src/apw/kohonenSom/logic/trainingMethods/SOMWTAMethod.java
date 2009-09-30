package apw.kohonenSom.logic.trainingMethods;

import apw.kohonenSom.learningFactors.SOMLearningFactor;

import java.awt.Point;

public class SOMWTAMethod implements SOMTrainingMethod {

	private SOMLearningFactor eta;
	
	public SOMWTAMethod(SOMLearningFactor eta, int xMax, int yMax)
	{
		this.eta = eta;
	}
	
	@Override
	public double[][][] adaptWeights(double[] vector, double[][][] weights,
			Point winner, double time) {
		for(int i=0; i<vector.length; i++)
			weights[winner.x][winner.y][i] = weights[winner.x][winner.y][i] + 
				eta.getEta(time)*(vector[i] - weights[winner.x][winner.y][i]); 
		
		return weights;
	}

}
