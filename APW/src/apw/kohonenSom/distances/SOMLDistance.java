package apw.kohonenSom.distances;

public class SOMLDistance implements SOMDistanceFunction {

	@Override
	public double getDistance(double[] w, double[] v) {
		double distance = java.lang.Math.abs(v[0] - w[0]);
		
		for(int i=1; i<w.length; i++)
			distance = java.lang.Math.max(distance, (java.lang.Math.abs(v[0] - w[0])));
			
		return distance;
	}

	@Override
	public String getDistanceName() {
		return "L distance";
	}

	@Override
	public int getDistanceNum() {
		return 3;
	}

}
