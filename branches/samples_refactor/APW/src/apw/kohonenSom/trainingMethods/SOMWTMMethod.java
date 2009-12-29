package apw.kohonenSom.trainingMethods;

import java.awt.Point;

import apw.kohonenSom.learningFactors.SOMLearningFactor;
import apw.kohonenSom.topology.SOMTopology;
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
	public double[][][] adaptWeights(
            double[] vector, double[][][] weights, Point winner, double time) {

		int XMax = weights.length;
		int YMax = weights[0].length;
        int VMax = vector.length;

        double[][][] nWeights = new double[XMax][][];
        for(int ix=0; ix<XMax; ix++){
            nWeights[ix] = new double[YMax][];
            for(int iy=0; iy<YMax; iy++){
                nWeights[ix][iy] = new double[VMax];
            }
        }

        double e = eta.getEta(time);
        double d, n, wi, vi;

		for(int ix=0; ix<XMax; ix++)
			for(int iy=0; iy<YMax; iy++)
			{
				d = nDist.calcDistance(winner, new Point(ix,iy));
                n = neighType.getNeighbourhood(d, time);
							
				for(int iv=0; iv<VMax; iv++)
				{
                    wi = weights[ix][iy][iv];
                    vi = vector[iv];

					nWeights[ix][iy][iv] = wi + e*n*(vi - wi);
				}
			}
		
		return nWeights;
	}
}
