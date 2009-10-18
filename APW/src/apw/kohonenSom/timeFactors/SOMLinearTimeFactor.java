package apw.kohonenSom.timeFactors;

public class SOMLinearTimeFactor implements SOMTimeFactor {

	private int tMax;

    private static final double endModif = 0.0001;
	
	public SOMLinearTimeFactor(int tMax)
	{
		this.tMax = tMax;
	}
	
	@Override
	public double getTimeModifier(double t) {
		return java.lang.Math.max(1 - t/tMax, endModif);
	}

}
