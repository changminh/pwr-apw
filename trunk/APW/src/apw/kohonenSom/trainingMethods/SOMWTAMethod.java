package apw.kohonenSom.trainingMethods;

import apw.kohonenSom.learningFactors.SOMLearningFactor;

import java.awt.Point;

public class SOMWTAMethod implements SOMTrainingMethod {

	private SOMLearningFactor eta;
	
	public SOMWTAMethod(SOMLearningFactor eta)
	{
		this.eta = eta;
	}
	
	@Override
	public double[][][] adaptWeights(double[] vector, double[][][] weights,
			Point winner, double time) {
        double[][][] w = weights;

        int x = winner.x;
        int y = winner.y;

		for(int i=0; i<vector.length; i++){
			double wi = w[x][y][i];
            double etai = eta.getEta(time);
            double vi = vector[i];

            w[x][y][i] = wi + etai*(vi - wi);
        }
		
		return w;
	}

}
