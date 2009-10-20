package apw.kohonenSom.timeFactors;

public class SOMLinearTimeFactor implements SOMTimeFactor {

	private int tMax;

    private double endModif;
	
	public SOMLinearTimeFactor(int tMax, double minModif)
	{
		this.tMax = tMax;
        this.endModif = minModif;
	}
	
	@Override
	public double getTimeModifier(double t) {
        double modif = tMax - t/tMax;
		return java.lang.Math.max(modif, endModif);
	}

}
