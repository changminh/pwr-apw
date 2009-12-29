package apw.kohonenSom.neighborhoods;

import apw.kohonenSom.timeFactors.SOMTimeFactor;

public class SOMGaussianNeighbourhood implements SOMNeighbourhoodFunction {
	private SOMTimeFactor timeType;
	private double maxR;
	
	public SOMGaussianNeighbourhood(SOMTimeFactor timeType, double maxR)
	{
		this.timeType = timeType;
		this.maxR = maxR;
	}
	
	@Override
	public double getNeighbourhood(double dist, double time) {
		
		double lambda = timeType.getTimeModifier(time)*maxR;
		
		return java.lang.Math.exp(-((dist*dist)/(2*lambda*lambda)));
	}

	@Override
	public String getNeighbourhoodName() {
		return "Gaussian function neighbourhood";
	}

	@Override
	public int getNeighbourhoodNum() {
		return 1;
	}

}
