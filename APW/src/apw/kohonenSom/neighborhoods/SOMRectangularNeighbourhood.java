package apw.kohonenSom.neighborhoods;

public class SOMRectangularNeighbourhood implements SOMNeighbourhoodFunction {
	private double maxR;
	private double decrease;
	
	public SOMRectangularNeighbourhood(double maxR, double decrease)
	{	
		this.maxR = maxR;
		this.decrease = decrease;
	}
	
	@Override
	public double getNeighbourhood(double dist, double time) {
		int n = 0;

        double lambda = java.lang.Math.max(maxR - decrease*time*maxR, 0);

		if(dist <= (lambda))
			n=1;
		
		return n;
	}

	@Override
	public String getNeighbourhoodName() {
		return "Rectangular neighbourhood";
	}

	@Override
	public int getNeighbourhoodNum() {
		return 0;
	}

}
