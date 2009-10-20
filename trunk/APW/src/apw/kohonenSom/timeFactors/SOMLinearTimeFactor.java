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
        double modif = tMax - t/tMax;
		return java.lang.Math.max(modif, endModif);
	}

}
