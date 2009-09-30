package apw.kohonenSom.distances;

public class SOMEuclideanDistance implements SOMDistanceFunction{

	@Override
	public double getDistance(double[] w, double[] v) {
		double distance = 0;
		
		for(int i=0; i<w.length; i++)
			distance += java.lang.Math.pow((v[i] - w[i]), 2.0);
			
		return java.lang.Math.sqrt(distance);
	}

	@Override
	public String getDistanceName() {
		return "Euclidean distance";
	}

	@Override
	public int getDistanceNum() {
		// TODO Auto-generated method stub
		return 0;
	}

}
