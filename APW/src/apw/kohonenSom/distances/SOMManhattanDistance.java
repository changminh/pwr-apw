package apw.kohonenSom.distances;

public class SOMManhattanDistance implements SOMDistanceFunction {

	@Override
	public double getDistance(double[] w, double[] v) {
		double distance = 0;
		
		for(int i=0; i<w.length; i++)
			distance += java.lang.Math.abs(v[i] - w[i]);
			
		return java.lang.Math.sqrt(distance);
	}

	@Override
	public String getDistanceName() {
		return "Manhattan distance";
	}

	@Override
	public int getDistanceNum() {
		return 2;
	}

}
