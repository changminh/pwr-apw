package apw.kohonenSom.neighborhoods;

public interface SOMNeighbourhoodFunction {
	public abstract double getNeighbourhood(double dist, double time);
	public abstract String getNeighbourhoodName();
	public abstract int getNeighbourhoodNum();
}
