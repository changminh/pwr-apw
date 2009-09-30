package apw.kohonenSom.timeFactors;

public class SOMLinearTimeFactor implements SOMTimeFactor {

	private int tMax;
	
	public SOMLinearTimeFactor(int tMax)
	{
		this.tMax = tMax;
	}
	
	@Override
	public double getTimeModifier(double t) {
		return (1 - t/tMax);
	}

}
