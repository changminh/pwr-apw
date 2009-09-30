package apw.kohonenSom.distances;

public class SOMScalarDistance implements SOMDistanceFunction {

	@Override
	public double getDistance(double[] w, double[] v) {
		double distance = 0;
		
		for(int i=0; i<w.length; i++)
			distance += (v[i] * w[i]);
		
		return distance;
	}

	@Override
	public String getDistanceName() {
		return "Scalar distance";
	}

	@Override
	public int getDistanceNum() {
		return 1;
	}

}
