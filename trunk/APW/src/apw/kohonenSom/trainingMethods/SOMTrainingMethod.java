package apw.kohonenSom.trainingMethods;

import java.awt.Point;

public interface SOMTrainingMethod {
	public abstract double[][][] adaptWeights(double[] vector, 
			double[][][] weights, Point winner, double time);
}
