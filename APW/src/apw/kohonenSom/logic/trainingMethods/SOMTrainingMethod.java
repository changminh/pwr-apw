package apw.kohonenSom.logic.trainingMethods;

import java.awt.Point;

public interface SOMTrainingMethod {
	public abstract double[][][] adaptWeights(double[] vector, 
			double[][][] weights, Point winner, double time);
}
