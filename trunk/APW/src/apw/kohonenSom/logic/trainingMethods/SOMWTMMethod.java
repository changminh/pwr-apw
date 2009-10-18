package apw.kohonenSom.logic.trainingMethods;

import java.awt.Point;

import apw.kohonenSom.learningFactors.SOMLearningFactor;
import apw.kohonenSom.logic.topology.SOMTopology;
import apw.kohonenSom.neighborhoods.SOMNeighbourhoodFunction;

public class SOMWTMMethod implements SOMTrainingMethod {
	
	SOMLearningFactor eta;
	SOMNeighbourhoodFunction neighType;
	SOMTopology nDist;
	
	public SOMWTMMethod(SOMLearningFactor eta, SOMNeighbourhoodFunction neighType, 
			SOMTopology nDist)
	{
		this.eta = eta;
		this.neighType = neighType;
		this.nDist = nDist;
	}
	
	@Override
	public double[][][] adaptWeights(double[] vector, double[][][] weights,
			Point winner, double time) {
		
		int XMax = weights.length;
		int YMax = weights[0].length;
		
		for(int x=0; x<XMax; x++)
			for(int y=0; y<YMax; y++)
			{
				double d = nDist.calcDistance(winner, new Point(x,y));
							
				for(int i=0; i<vector.length; i++)
				{
					weights[x][y][i] = weights[x][y][i] + 
					eta.getEta(time)*neighType.getNeighbourhood(d, time)*
					(vector[i] - weights[x][y][i]);
				}
			}
		
		return weights;
	}
}
