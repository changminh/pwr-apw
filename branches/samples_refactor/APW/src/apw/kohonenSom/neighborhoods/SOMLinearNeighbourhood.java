package apw.kohonenSom.neighborhoods;

import apw.kohonenSom.timeFactors.SOMTimeFactor;

public class SOMLinearNeighbourhood implements SOMNeighbourhoodFunction {
	private SOMTimeFactor timeType;
	private double maxR;
	
	public SOMLinearNeighbourhood(SOMTimeFactor timeType, double maxR)
	{
		this.timeType = timeType;
		this.maxR = maxR;
	}
	
	@Override
	public double getNeighbourhood(double dist, double time) {
		return dist*maxR*timeType.getTimeModifier(time);
	}

	@Override
	public String getNeighbourhoodName() {
		return "Linear function neighbourhood";
	}

	@Override
	public int getNeighbourhoodNum() {
		return 2;
	}

}
