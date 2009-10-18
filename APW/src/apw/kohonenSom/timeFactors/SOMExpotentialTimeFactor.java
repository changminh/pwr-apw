package apw.kohonenSom.timeFactors;

public class SOMExpotentialTimeFactor implements SOMTimeFactor {

	private double C;

    private static final double endModif = 0.0001;
	
	public SOMExpotentialTimeFactor(double C)
	{
		this.C = C;
	}
	
	@Override
	public double getTimeModifier(double t) {
        double modifier = java.lang.Math.exp(-1*t*C);
		return java.lang.Math.max(modifier, endModif);
	}

}
