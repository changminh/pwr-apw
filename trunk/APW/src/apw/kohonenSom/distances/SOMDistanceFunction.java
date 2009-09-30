package apw.kohonenSom.distances;

public interface SOMDistanceFunction {
	public abstract double getDistance(double[] w, double[] v);
	public abstract String getDistanceName();
	public abstract int getDistanceNum();
}
